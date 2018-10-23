const express = require('express');

const router = express.Router();
const controller = require('./muver.controller');

router.get('/', controller.load);
router.post('/', controller.createMuver);
router.get('/:id', controller.loadMuver);
router.put('/:id', controller.updateMuver);
router.delete('/:id', controller.removeMuver);
router.get('/user/:id', controller.loadMuverByUser);

module.exports = router;
