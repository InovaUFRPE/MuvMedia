const mongoose = require('mongoose');
const mongoosePaginate = require('mongoose-paginate');

const Schema = mongoose.Schema;

const User = new Schema({
  email: {
    type: String,
    unique: true,
    required: true,
    trim: true
  },
  password: { type: String, required: true, select: false },
  level: { type: Number, required: true },
  status: { type: Boolean, default: true },
  deleted: { type: Boolean, default: false },
}, {
    timestamps: true,
  });

User.plugin(mongoosePaginate);

module.exports = mongoose.model('User', User);
