import express, { request, response } from "express";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

app.get('/api/status', (request, response) => {

    return response.status(200).json({

        service: 'Auth-API',
        statusSTATUS: "up",
        httpStatus: 200
    })
})

app.listen(PORT, () => {

    console.info(`Server Started! at port ${PORT}` )
});