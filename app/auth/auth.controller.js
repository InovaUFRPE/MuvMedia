const AuthService = require('./auth.service');
const User = require('../user/user.model');

module.exports = {
  login,
}

async function login(req, res) {
  const data = req.body;
  const user = new User(data);
  try {
    const session = await AuthService.login(user)
    res.status(200).json(session);
  } catch (err) {
    console.log(err);
    res.status(401).json({ message: "Usuário ou senha incorreto" });
  }
}
