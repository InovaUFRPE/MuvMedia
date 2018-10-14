const http = require('http');

const app = require('./app/app');

const PORT = process.env.PORT || 5001;
const server = http.createServer(app);

server.listen(PORT, () => {
  console.log(`Server listening at ${PORT}`);
});

server.on('error', (err) => {
  console.log(err.message);
});
