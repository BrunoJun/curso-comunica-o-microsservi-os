import { v4 } from "uuid";
import { BAD_REQUEST } from "./constants/HttpStatus.js";

export default (request, response, next) => {

    let {transactionid} = request.headers;
    
    if(!transactionid){

        return response.status(BAD_REQUEST).json({

            status: BAD_REQUEST,
            message: 'The transactionId is required'
        });
    }

    request.headers.serviceid = v4();
    return next();
}