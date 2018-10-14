const mongoose = require('mongoose');
const mongoosePaginate = require('mongoose-paginate');

const Schema = mongoose.Schema;

const Muver = new Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  nome: { type: String, required: true },
  dataNascimento: { type: Date, required: true },
  cpf: {
    type: String,
    required: true,
    unique: true
  },
  deleted: { type: Boolean, default: false },
}, {
    timestamps: true,
});

Muver.plugin(mongoosePaginate);

module.exports = mongoose.model('Muver', Muver);
