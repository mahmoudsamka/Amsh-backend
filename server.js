const express = require("express");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 3000;

/* ---------- بيانات تجريبية في الذاكرة (استبدلها بقاعدة بيانات حقيقية لو حبيت) ---------- */

const USERS = [
  { email: "mahmoud@amsh.com", password: "amsh1234", token: "amsh-demo-token-001" },
];

const POSTS = Array.from({ length: 20 }).map((_, i) => ({
  id: i + 1,
  title: `عنصر رقم ${i + 1}`,
  body: `ده وصف تجريبي للعنصر رقم ${i + 1}، جاي من سيرفر AMSH الحقيقي مش بيانات ثابتة جوه التطبيق.`,
}));

/* ---------- Routes ---------- */

app.get("/", (req, res) => {
  res.json({ status: "ok", service: "amsh-backend" });
});

app.post("/api/login", (req, res) => {
  const { email, password } = req.body || {};
  const user = USERS.find((u) => u.email === email && u.password === password);

  if (!user) {
    return res.status(400).json({ error: "بيانات الدخول غير صحيحة" });
  }

  res.json({ token: user.token });
});

app.get("/posts", (req, res) => {
  res.json(POSTS);
});

app.listen(PORT, () => {
  console.log(`AMSH backend listening on port ${PORT}`);
});
