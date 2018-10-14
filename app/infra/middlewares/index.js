const cors = require('cors');
const bodyParser = require('body-parser');
const swagger = require('./swagger.middleware');

const jsonParser = bodyParser.json();

module.exports = (app) => {
  app.use(cors({origin: true}));
  app.use(jsonParser);
  swagger(app);
}
