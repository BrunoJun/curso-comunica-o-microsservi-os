import mongoose, { model } from "mongoose";

const Schema = mongoose.Schema;
const MODEL = mongoose.model;

const OrderSchema = new Schema({

  products: {
    type: Array,
    required: true
  },
  user: {
    type: Object,
    required: true
  },
  status: {
    type: String,
    required: true
  },
  createdAt: {
    type: Date,
    required: true
  },
  updatedAt: {
    type: Date,
    required: true
  },
  transactionid: {

    type: String,
    required: true
  },
  serviceid: {

    type: String,
    required: true
  }
});

export default MODEL('Order', OrderSchema);