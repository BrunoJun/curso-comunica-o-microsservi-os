import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/HttpStatus.js";
import UserException from "../exception/UserException.js";
import e, { request } from "express";
import * as secrets from "../../../config/constants/Secrets.js"

import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

class UserService{

    async findByEmail(request){

        try {
            
            const {email} = request.params;
            const {authUser} = request;
            this.checkDatas(email);

            let user = await UserRepository.findByEmail(email);
            this.checkUserNotFound(user);
            this.checkAuthenticatedUser(user, authUser);

            return {

                status: httpStatus.SUCCESS,
                user: {

                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (error) {
            
            return {

                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    checkDatas(email){

        if (!email){

            throw new UserException(httpStatus.BAD_REQUEST, 'Email was not informed.');
        }
    }

    checkUserNotFound(user){

        if(!user){

            throw new Error(httpStatus.BAD_REQUEST, 'User not found.');
        }
    }

    checkAuthenticatedUser(user, authUser){

        if (!authUser || user.id !== authUser.id){

            throw new UserException(httpStatus.FORBIDDEN, "You cannot user data.")
        }
    }

    async getAccessToken(request){

        try {

            const {transactionid, serviceid} = request.headers;

            console.info(`Request to Post order with data ${JSON.stringify(request.body)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);
            
            const {email, password} = request.body;
            this.checkAccessTokenData(email, password);
            let user = await UserRepository.findByEmail(email);
            this.checkUserNotFound(user);
            await this.checkPassword(password, user.password);
            const authUser =  {id: user.id, name: user.name, email: user.email};
            const accessToken = jwt.sign({authUser}, secrets.API_SECRET,{expiresIn: '1d'});

            let response = {
                status: httpStatus.SUCCESS,
                accessToken
            };

            console.info(`Response to Post login with data ${JSON.stringify(response)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

            return response;

        } catch (error) {
            
            return {

                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    checkAccessTokenData(email, password){

        if (!email || !password){

            throw new UserException(httpStatus.UNAUTHORIZED, "Email or password must be informed.");
        }
    }

    async checkPassword(password, hashPassword){

        if (!await bcrypt.compare(password, hashPassword)){

            throw new UserException(httpStatus.UNAUTHORIZED, "Wrong password.");
        }
    }
};

export default new UserService();