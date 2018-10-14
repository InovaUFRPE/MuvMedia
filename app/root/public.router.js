const express = require('express');
const userController = require('../user/user.controller');
const muverController = require('../muver/muver.controller');

const router = express.Router();

router.post('/register/user', userController.createUser);
router.post('/register/muver', muverController.createMuver);

module.exports = router;
