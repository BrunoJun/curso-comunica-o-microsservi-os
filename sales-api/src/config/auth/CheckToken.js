import Jwt from "jsonwebtoken";
import {promisify} from "util";

import {API_SECRET} from "../../config/constants/secrets.js";
import {UNAUTHORIZED, INTERNAL_SERVER_ERROR} from "../constants/HttpStatus.js";
import AccessTokenException from "./AccessTokenException.js";

const bearer = "bearer  ";

const emptySpace = " ";

export default async(request, response, next) => {

    try {
        
        const {authorization} = request.headers;

        if(!authorization){
    
            throw new AccessTokenException(UNAUTHORIZED, "Access Token not informed.");
        }
        
        let accessToken = authorization;

        if (accessToken.includes(emptySpace)){

            accessToken = accessToken.split(emptySpace)[1];
        } else {

            accessToken = authorization;
        }

        const decoded = await promisify(Jwt.verify)(accessToken, API_SECRET);
        request.authUser = decoded.authUser;
        return next();
    } catch (error) {

        const status = error.status ? error.status : INTERNAL_SERVER_ERROR;
        return response.status(status).json({status, message: error.message})
    }
};