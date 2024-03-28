import { launch } from "puppeteer";

import { randomBytes } from "./utils";

if (!process.env.FLAG) {
  console.error("FLAG is not set");
  process.exit(1);
}

export default async function visit(id, username) {
  const browser = await launch({
    args: ["--no-sandbox", "--headless"],
    executablePath: "/usr/bin/chromium",
  });
  try {
    console.log(`Visiting ${id} as ${username}`);
    let page = await browser.newPage();

    await page.goto(`http://localhost/login`);

    await page.waitForSelector("#username");
    await page.focus("#username");
    await page.keyboard.type(randomBytes(10), { delay: 10 });

    await page.waitForSelector("#password");
    await page.focus("#password");
    await page.keyboard.type(randomBytes(20), { delay: 10 });

    await new Promise((resolve) => setTimeout(resolve, 300));
    await page.click("#submit");
    await new Promise((resolve) => setTimeout(resolve, 300));

    page.setCookie({
      name: "FLAG",
      value: process.env.FLAG!,
      domain: "localhost",
      path: "/",
      httpOnly: false,
      sameSite: "Strict",
    });

    await page.goto(
      `http://localhost/share/read#id=${id}&username=${username}`,
      { timeout: 5000 },
    );
    await new Promise((resolve) => setTimeout(resolve, 30000));
    await page.close();
    await browser.close();
  } catch (e) {
    console.log(e);
    await browser.close();
  }
}
