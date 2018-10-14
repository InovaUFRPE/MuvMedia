const express = require('express');

const middlewares = require('./infra/middlewares/index');
const database = require('./infra/mongo.config');
const router = require('./router');

const app = express();

middlewares(app);
database();
router(app);

module.exports = app;
