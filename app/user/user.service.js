const bcrypt = require('bcrypt');
const User = require('./user.model');

module.exports = {
  createUser,
  load,
  loadUser,
  loadUserByEmail,
  updateUser,
  removeUser
}

function createUser(user) {
  return new Promise(async (resolve, reject) => {
    try {
      user.password = await hashPassword(user.password);
      user.save((err, data) => {
        if (err) {
          console.log(err);
          reject(err);
        } else {
          resolve(data);
        }
      });
    } catch (err) {
      console.log(err);
      reject(err);
    }
  });
}

function load(params) {
  params.page = parseInt(params.page) || 1;
  params.limit = parseInt(params.limit) || 10;
  params.sort = {'createdAt': 'desc'};

  return new Promise(async (resolve, reject) => {
    User.paginate({ deleted: false },
      { page: params.page, limit: params.limit, sort: params.sort },
      (err, result) => {
        if (err) {
          reject(err);
        }
        resolve(result);
      });
  });
}

function loadUser(id) {
  return new Promise((resolve, reject) => {
    User.findById(id, (err, user) => {
      if (err) {
        console.log(err);
        reject(err);
      } else {
        resolve(user);
      }
    });
  });
}

function loadUserByEmail(email) {
  return new Promise((resolve, reject) => {
    User.findOne({ email: email, deleted: false })
      .select('+password')
      .exec((err, user) => {
        if (err) {
          console.log(err);
          reject(err);
        } else {
          resolve(user);
        }
      });
  });
}

function updateUser(id, data) {
  return new Promise(async (resolve, reject) => {
    try {
      if (data.password) {
        data.password = await hashPassword(data.password);
      }

      const query = { _id: id, deleted: false };
      const res = await User.findOneAndUpdate(query, data);
      resolve(res);
    } catch (err) {
      console.log(err);
      reject(err);
    }
  });
}

function removeUser(id) {
  return new Promise(async (resolve, reject) => {
    const query = { _id: id };
    const data = { deleted: true };

    try {
      const res = User.findOneAndUpdate(query, data);
      resolve(res);
    } catch (err) {
      console.log(err);
      reject(err);
    }
  });
}

function hashPassword(password) {
  return new Promise((resolve, reject) => {
    bcrypt.hash(password, 10, (err, hash) => {
      if (err) {
        return reject(err);
      }
      return resolve(hash);
    });
  });
}
