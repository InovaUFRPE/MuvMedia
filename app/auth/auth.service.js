const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const moment = require('moment');
const UserService = require('../user/user.service');
const Session = require('./session.model');

module.exports = {
  login,
  loadSessionByUser,
}

function login(userLogin) {
  return new Promise(async (resolve, reject) => {
    try {
      let user = await UserService.loadUserByEmail(userLogin.email);
      const isAuth = await verifyPassword(userLogin, user);
      if (isAuth) {
        user.password = '';
        const session = createSession(user);
        resolve(session);
      } else {
        reject(false);
      }
    } catch (err) {
      reject(err);
    }
  });
}

async function verifyPassword(userLogin, user) {
  const password = userLogin.password;
  const userPassword = user.password;
  return new Promise(async (resolve, reject) => {
    try {
      const isAuth = await bcrypt.compare(password, userPassword);
      resolve(isAuth);
    } catch (err) {
      reject(err);
    }
  });
}

function createSession(user) {
  return new Promise(async (resolve, reject) => {
    const expires = moment().add('1', 'h').toDate();
    let session = new Session({ user: user, expiresAt: expires });
    try {
      const token = await createToken(session);
      disableOldSession(user._id);
      session.set({ token: token });
      const newSession = await session.save();
      resolve(newSession);
    } catch (err) {
      reject(err);
    }
  });
}

function createToken(session) {
  const secret = process.env.SECRET || 'secret';
  const user = session.user;
  return new Promise(async (resolve, reject) => {
    try {
      const token = await jwt.sign({
        exp: Math.floor(moment(session.expiresAt).valueOf()),
        iat: Math.floor(moment().valueOf()),
        sub: user._id,
      }, secret);
      resolve(token);
    } catch (err) {
      reject(err);
    }
  });
}

function loadSessionByUser(userId) {
  return new Promise(async (resolve, reject) => {
    try {
      const session = await Session
                              .findOne({ user: userId, active: true })
                              .sort({ createdAt: -1 })
                              .exec();
      resolve(session);
    } catch (err) {
      reject(err);
    }
  });
}

async function disableOldSession(userId) {
  try {
    const query = { user: userId, active: true };
    const data = { active: false };
    await Session.findOneAndUpdate(query, data);
  } catch (err) {
    console.log(err);
  } 
}
