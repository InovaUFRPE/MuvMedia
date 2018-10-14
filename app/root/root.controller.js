module.exports = {
  root,
  info,
}

function root(req, res) {
  res.status(200).json({ message: 'Hello to muvmedia-api by cletus enjoy' });
  return;
}


function info(req, res) {
  const packageJSON = require('../../package.json');
  res.status(200).json(packageJSON);
}
