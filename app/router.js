const auth = require('./auth/auth.middleware');

const rootRouter = require('./root/root.router');
const publicRouter = require('./root/public.router');
const authRouter = require('./auth/auth.router');
const usersRouter = require('./user/user.router');

const muverRouter = require('./muver/muver.router');

module.exports = (app) => {
  app.use('/', rootRouter);
  app.use('/public', publicRouter)
  app.use('/auth', authRouter);
  app.use('/users', auth, usersRouter);
  app.use('/muvers', auth, muverRouter);
}
