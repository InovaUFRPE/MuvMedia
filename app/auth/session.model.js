const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Session = new Schema({
  token: String,
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  },
  expiresAt: Date,
  active: {
    type: Boolean,
    default: true
  }
}, {
    timestamps: true,
  });

module.exports = mongoose.model('Session', Session);
