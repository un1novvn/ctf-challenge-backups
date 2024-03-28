import crypto from 'node:crypto'

export const sha256 = (plain: string) =>
  crypto.createHash("sha256").update(plain.toString()).digest("hex");

export const randomBytes = (size: number) =>
  crypto.randomBytes(size).toString();

export const genNonce = () =>
  crypto
    .randomBytes(16)
    .toString("base64")
    .replace(/[^a-zA-Z0-9]/g, "");
