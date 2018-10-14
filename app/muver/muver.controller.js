const MuverService = require('./muver.service');
const Muver = require('./muver.model');

module.exports = {
  createMuver,
  load,
  loadMuver,
  updateMuver,
  removeMuver
}

async function createMuver(req, res) {
  const data = req.body;
  try {
    const muver = new Muver(data);
    const newMuver = await MuverService.createMuver(muver);
    res.status(201).json(newMuver);
  } catch (err) {
    console.log(err);
    if (err.code == 11000) {
      res.status(409).json({ code: "EMV01", message: "Muver já existe" });
    }
    res.status(400).json({ code: "EMV02", message: "Dados fornecidos inválidos" });
  }
}

async function load(req, res) {
  const params = req.query;

  try {
    const pagination = await MuverService.load(params);

    if (pagination.total == 0) {
      res.status(404).json({ code: "EMV03", message: "Nenhum resultado encontrado com os parâmetros fornecidos" });
    } else if (pagination.total <= pagination.limit) {
      res.status(200).json(pagination);
    } else {
      res.status(206).json(pagination);
    }

  } catch (err) {
    console.log(err);
    res.status(400).json({ code: "EMV04", message: "Dados fornecidos inválidos" });
  }
}

async function loadMuver(req, res) {
  const id = req.params.id;

  try {
    const muver = await MuverService.loadMuver(id);
    if (muver) {
      res.status(200).json(muver);
    } else {
      res.status(404).json({ code: "EMV05", message: "Muver não encontrado" });
    }
  } catch (err) {
    console.log(err);
    res.status(400).json({ code: "EMV06", message: "Dados fornecidos inválidos" });
  }
}

async function updateMuver(req, res) {
  const id = req.params.id;
  const data = req.body;

  try {
    const muver = await MuverService.updateMuver(id, data);
    if (muver) {
      res.status(200).json(muver);
    } else {
      res.status(404).json({ code: "EMV07", message: "Muver não encontrado" });
    }
  } catch (err) {
    console.log(err);
    res.status(400).json({ code: "EMV08", message: "Dados fornecidos inválidos" });
  }
}

async function removeMuver(req, res) {
  const id = req.params.id;

  try {
    const result = await MuverService.removeMuver(id);
    if (result) {
      res.status(200).json({ msg: "Muver removido" });
    } else {
      res.status(404).json({ code: "EMV09", message: "Muver não encontrado" });
    }
  } catch (err) {
    console.log(err);
    res.status(400).json({ code: "EMV10", message: "Dados fornecidos inválidos" });
  }
}
