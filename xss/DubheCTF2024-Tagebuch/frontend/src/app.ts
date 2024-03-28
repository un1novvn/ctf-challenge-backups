import { urlencoded } from "body-parser";
import ejs from "ejs";
import express from "express";
import session from "express-session";
import { AsyncLocalStorage } from "node:async_hooks";

import visit from "./bot";
import { genNonce, randomBytes, sha256 } from "./utils";

declare module "express-session" {
  interface SessionData {
    username: string;
  }
}

declare global {
  namespace NodeJS {
    interface ProcessEnv {
      TURNSTILE_SITE_KEY?: string;
      TURNSTILE_SECRET_KEY?: string;
      FLAG?: string;
    }
  }
}

const app = express();

app.use("/static", express.static("./static"));
app.use(urlencoded({ extended: false }));
app.use(
  session({
    cookie: { maxAge: 600000 },
    secret: randomBytes(64),
    resave: true,
    saveUninitialized: true,
  }),
);

const nonceStorage = new AsyncLocalStorage<string>();

app.use((req, res, next) => {
  res.setHeader("X-Content-Type-Options", "nosniff");
  res.setHeader("Cache-Control", "no-cache, no-store");
  nonceStorage.run(genNonce(), next);
});

app.engine("html", async (path, data, cb) =>
  ejs.renderFile(path, data, (err, html) => {
    if (err) return cb(err);
    const nonce = nonceStorage.getStore();
    if (nonce) {
      const cspMeta = `<meta http-equiv="Content-Security-Policy" content="script-src 'nonce-${nonce}';">`;
      html = html.replace(/<head>/, `<head>\n${cspMeta}`);
    }
    cb(null, html);
  }),
);
app.set("view engine", "html");

interface Note {
  title: string;
  content: string;
  username: string;
}

const users = new Map<string, string>(),
  notes = new Map<string, Note[]>(),
  sharedNotes = new Map<string, Note[]>();

app.all("/", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/login");
  } else {
    return res.render("index", {
      username: req.session.username,
      notes: notes.get(req.session.username) || [],
      error: req.query.error,
    });
  }
});

app.get("/login", (req, res) => {
  if (req.session.username) {
    return res.redirect("/");
  }
  return res.render("login");
});

app.post("/login", (req, res) => {
  if (req.session.username) {
    return res.redirect("/");
  }
  const { username, password } = req.body;

  if (
    username.length < 4 ||
    username.length > 10 ||
    typeof username !== "string" ||
    password.length < 6 ||
    typeof password !== "string"
  ) {
    return res.render("login", { msg: "invalid data" });
  }

  if (users.has(username)) {
    if (users.get(username) === sha256(password)) {
      req.session.username = username;

      return res.redirect("/");
    } else {
      return res.render("login", { msg: "Invalid Password" });
    }
  } else {
    users.set(username, sha256(password));
    req.session.username = username;

    return res.redirect("/");
  }
});

app.post("/write", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/");
  }
  const username = req.session.username;
  const { title, content } = req.body;

  if (typeof title !== "string" || title.length >= 30) {
    return res.redirect(
      `/?${new URLSearchParams({ error: "Title is too long" }).toString()}`,
    );
  }
  if (typeof content !== "string" || content.length >= 10240) {
    return res.redirect(
      `/?${new URLSearchParams({ error: "Content is too long" }).toString()}`,
    );
  }

  const user_notes = notes.get(username) || [];
  user_notes.push({
    title,
    content,
    username,
  });
  notes.set(req.session.username, user_notes);

  return res.redirect("/");
});

app.get("/read", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/");
  }

  return res.render("read", { nonce: nonceStorage.getStore() });
});

app.get("/read/:id(\\d+)", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/");
  }

  const found = notes.get(req.session.username)?.[+req.params.id] ?? {
    title: "404 not found",
    content: "no such note",
  };
  return res.json({ title: found.title, content: found.content });
});

app.get("/share_diary/:id(\\d+)", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/");
  }

  const found = notes.get(req.session.username)?.[+req.params.id];
  if (found) {
    sharedNotes.set(
      req.session.username,
      (sharedNotes.get(req.session.username) ?? []).concat(found),
    );
    return res.redirect("/share");
  } else {
    return res.redirect(
      `/?${new URLSearchParams({ error: "No such note" }).toString()}`,
    );
  }
});

app.all("/share", (req, res) => {
  if (!req.session.username) {
    return res.redirect("/login");
  } else {
    return res.render("share", {
      notes: sharedNotes.get(req.session.username) ?? [],
    });
  }
});

app.get("/share/read", (req, res) => {
  return res.render("read_share", {
    nonce: nonceStorage.getStore(),
    sitekey: process.env.TURNSTILE_SITE_KEY,
  });
});

app.get("/share/read/:id", (req, res) => {
  const { id } = req.params;

  if (!/^\d+$/.test(id)) {
    return res.json({ status: 401, message: "Invalid parameter" });
  }
  const username =
    typeof req.query.username === "string"
      ? req.query.username
      : req.session.username;
  const found = username && sharedNotes.get(username);

  if (!found) {
    return res.json({ title: "404 not found", content: "no such note" });
  }

  return res.json({
    title: found[id].title,
    content: found[id].content,
    username: found[id].username,
  });
});

app.all("/logout", async (req, res) => {
  await new Promise((resolve) => req.session.destroy(resolve));
  return res.redirect("/");
});

app.post("/report", async (req, res) => {
  if (process.env.TURNSTILE_SECRET_KEY) {
    const turnstileToken = req.body["cf-turnstile-response"];
    if (!turnstileToken) {
      return res.status(400).json({ error: "bad request" });
    }
    const outcome = await fetch(
      "https://challenges.cloudflare.com/turnstile/v0/siteverify",
      {
        method: "POST",
        body: new URLSearchParams({
          secret: process.env.TURNSTILE_SECRET_KEY,
          response: turnstileToken,
        }),
      },
    ).then((resp) => resp.json());
    if (!outcome.success) {
      return res.status(400).json({ error: "invalid captcha" });
    }
  }
  const { id, username } = req.body;
  if (typeof id === "string" && /^\d+$/.test(id)) {
    try {
      await visit(id, username);
      return res.json({ msg: "visited" });
    } catch (e) {
      return res.status(500).json({ error: "failed" });
    }
  }
  res.status(400).json({ error: "bad url" });
});

app.listen(80);
console.log("Server running on 80....");
