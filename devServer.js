const nodemon = require('nodemon');
require('dotenv').config();

nodemon({
  script: 'index.js',
  ext: 'js json yaml',
});

nodemon.on('start', () => {
  console.log('App has started...');
}).on('quit', () => {
  console.log('App has quit');
}).on('restart', (files) => {
  console.log('App has restarted due to: ', files);
});
