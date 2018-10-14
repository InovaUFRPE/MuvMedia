
const fs = require('fs');
const path = require('path');
const swaggerTools = require('swagger-tools');
const jsyaml = require('js-yaml');

module.exports = (app) => {

    const spec = fs.readFileSync(path.resolve(__dirname, '../../../docs/swagger.yaml'), 'utf8');
    const swaggerDoc = jsyaml.safeLoad(spec);

    swaggerTools.initializeMiddleware(swaggerDoc, (middleware) => {
        // Interpret Swagger resources and attach metadata to request - must be first in swagger-tools middleware chain
        app.use(middleware.swaggerMetadata());

        // Serve the Swagger documents and Swagger UI
        app.use(middleware.swaggerUi());
    });
};