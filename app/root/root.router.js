const express = require('express');

const router = express.Router();
const controller = require('./root.controller');

router.get('/', controller.root);
router.get('/info', controller.info);

module.exports = router;
