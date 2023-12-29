import UserService from "../service/UserService.js";

class UserController{

    async getAccessToken(request, response){

        let accessToken = await UserService.getAccessToken(request);
        return response.status(accessToken.status).json(accessToken);
    };

    async findByEmail(request, response){

        let user = await UserService.findByEmail(request);
        return response.status(user.status).json(user);
    }
}

export default new UserController();