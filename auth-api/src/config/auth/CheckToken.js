import Jwt from "jsonwebtoken";
import {promisify} from "util";

import * as secret from "../../config/constants/Secrets.js";
import * as httpStatus from "../../config/constants/HttpStatus.js";
import AccessTokenException from "./AccessTokenException.js";
import { request } from "http";
import { response } from "express";

const bearer = "bearer  ";

const emptySpace = " ";

export default async(request, response, next) => {

    try {
        
        const {authorization} = request.headers;

        if(!authorization){
    
            throw new AccessTokenException(httpStatus.UNAUTHORIZED, "Access Token not informed.");
        }
        
        let accessToken = authorization;

        if (accessToken.includes(emptySpace)){

            accessToken = accessToken.split(emptySpace)[1];
        } else {

            accessToken = authorization;
        }

        const decoded = await promisify(Jwt.verify)(accessToken, secret.API_SECRET);
        request.authUser = decoded.authUser;
        return next();
    } catch (error) {

        const status = error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR;
        return response.status(status).json({status, message: error.message})
    }
};