import mongoose from "mongoose";
import {MONGO_DB_URL} from  "../constants/secrets.js";

export function connectMongoDB(){

    mongoose.connect(MONGO_DB_URL, {useNewUrlParser: true, serverSelectionTimeoutMS: 1000000});

    mongoose.connection.on('connected', function (){console.info('The connect to MongoDB was successfully.')});
    mongoose.connection.on('error', function (){console.error('The connect to MongoDB was failed.')});
}