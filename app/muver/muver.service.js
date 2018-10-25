const Muver = require('./muver.model');

module.exports = {
  createMuver,
  load,
  loadMuver,
  loadMuverByUser,
  updateMuver,
  removeMuver
}

function createMuver(muver) {
  muver.user = muver.user._id;

  return new Promise(async (resolve, reject) => {
    try {
      muver.save((err, data) => {
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

  const query = buildQuery(params);

  return new Promise((resolve, reject) => {
    Muver.paginate(query,
      { page: params.page, limit: params.limit, sort: params.sort, populate: 'user' },
      (err, result) => {
        if (err) {
          reject(err);
        }
        resolve(result);
      });
  });
}

function loadMuver(id) {
  return new Promise((resolve, reject) => {
    Muver.findById(id)
          .where({deleted: false})
          .populate('user')
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

function loadMuverByUser(id) {
  return new Promise((resolve, reject) => {
    Muver.findOne()
          .where({ deleted: false, user: id })
          .populate('user')
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

function updateMuver(id, data) {
  data.user = data.user._id;

  return new Promise(async (resolve, reject) => {
    try {
      const query = { _id: id, deleted: false };
      await Muver.updateOne(query, data);
      const muver = await loadMuver(id);
      resolve(muver);
    } catch (err) {
      console.log(err);
      reject(err);
    }
  });
}

function removeMuver(id) {
  return new Promise(async (resolve, reject) => {
    const query = { _id: id };
    const data = { deleted: true };

    try {
      const res = Muver.findOneAndUpdate(query, data);
      resolve(res);
    } catch (err) {
      console.log(err);
      reject(err);
    }
  });
}

function buildQuery(params) {
  let query = { deleted: false };
  if (params.user) {
    query.user = params.user;
  }
  console.log(query);
  return query;
}
