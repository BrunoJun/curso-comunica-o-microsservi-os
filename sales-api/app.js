import express, { request, response } from "express";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

app.get('/api/status', (request, response) => {
    return response.status(200).json({

        service: 'sales-api',
        status: 'up',
        httpStatus: 200
    })
})

app.listen(PORT, () => (

    console.info(`Server Start at port ${PORT}`)
))