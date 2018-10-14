const express = require('express');

const router = express.Router();
const controller = require('./user.controller');

router.get('/', controller.load);
router.post('/', controller.createUser);
router.get('/:id', controller.loadUser);
router.put('/:id', controller.updateUser);
router.delete('/:id', controller.removeUser);

module.exports = router;
