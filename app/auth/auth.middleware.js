const jwt = require('jsonwebtoken');
const moment = require('moment');
const AuthService = require('../auth/auth.service');

module.exports = authMiddleware;

async function authMiddleware(req, res, next) {
  const authorization = req.headers['authorization'];
  if (!authorization) {
    res.status(400).json({ message: "É necessario uma autorização" });
    return;
  }
  const token = getToken(authorization);
  try {
    const validToken = await validateToken(token);
    const isValid = await verifySession(validToken);
    if (isValid) {
      next();
    } else {
      res.status(403).json({ message: "Token expirado" });
    }
  } catch (err) {
    console.log(err);
    if (err.name == 'JsonWebTokenError') {
      res.status(403).json({ message: "Token inválido" });
    }
  }
}

function validateToken(token) {
  return new Promise(async (resolve, reject) => {
    jwt.verify(token, process.env.SECRET, (err, decoded) => {
      if (decoded) {
        resolve(decoded);
      }
      reject(err);
    });
  });
}

async function verifySession(token) {
  userId = token.sub;
  try {
    const session = await AuthService.loadSessionByUser(userId);
    let isValid = false;
    if (session) {
      if (!isExpired(session)) {
        isValid = true;
      }
    }
    return isValid;
  } catch (err) {
    return err;
  }
}

function isExpired(session) {
  const expiresAt = moment(session.expiresAt);
  const now = moment();
  return now.isAfter(expiresAt);
}

function getToken(authorization) {
  return authorization.split(' ')['1'];
}
