import express, { request, response } from "express";
import * as db from "./src/config/db/initialData.js";
import UserRoutes from "./src/modules/user/routes/UserRoutes.js";
import CheckToken from "./src/config/auth/CheckToken.js";
import tracing from "./src/config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;
const CONTAINER_ENV = 'container';

app.get('/api/status', (request, response) => {

    return response.status(200).json({

        service: 'Auth-API',
        statusSTATUS: "up",
        httpStatus: 200
    })
})

app.use(express.json());

startAplication();

function startAplication(){

    if (env.NODE_ENV !== CONTAINER_ENV){

        db.createInitalData()
    }
}

app.get('/api/initial-data', (request, response) => {

    db.createInitalData();
    return response.json({message: 'Data created'});
})

app.use(tracing);
app.use(UserRoutes); 

app.listen(PORT, () => {

    console.info(`Server Started! at port ${PORT}` )
});