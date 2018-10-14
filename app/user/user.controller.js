const UserService = require('./user.service');
const User = require('./user.model');

module.exports = {
  load,
  loadUser,
  createUser,
  updateUser,
  removeUser
}

async function createUser(req, res) {
  const data = req.body;
  const user = new User(data);

  try {
    const newUser = await UserService.createUser(user);
    res.status(201).json(newUser);
  } catch (err) {
    console.log(err);
    res.status(400).json({ err: err });
  }
}

async function load(req, res) {
  const params = req.query;

  try {
    const pagination = await UserService.load(params);

    if (pagination.total <= pagination.limit) {
      res.status(200).json(pagination);
    } else {
      res.status(206).json(pagination);
    }

  } catch (err) {
    console.log(err);
    res.status(400).json({ err: err });
  }
}

async function loadUser(req, res) {
  const id = req.params.id;

  try {
    const user = await UserService.loadUser(id);
    res.status(200).json(user);
  } catch (err) {
    console.log(err);
    res.status(400).json({ err: err });
  }
}

async function updateUser(req, res) {
  const id = req.params.id;
  const data = req.body;

  try {
    const user = await UserService.updateUser(id, data);
    res.status(200).json(user);
  } catch (err) {
    console.log(err);
    res.status(400).json({ err: err });
  }
}

async function removeUser(req, res) {
  const id = req.params.id;

  try {
    await UserService.removeUser(id);
    res.status(200).json({ msg: "Usuário removido" });
  } catch (err) {
    console.log(err);
    res.status(400).json({ err: err });
  }
}
