import express, { request, response } from "express";
import {connectMongoDB} from './src/config/db/mongoDbConfig.js';
import {createInitialData} from "./src/config/db/initialData.js";
import CheckToken from "./src/config/auth/CheckToken.js";
import {connectRabbitmq} from './src/config/rabbitmq/rabbitConfig.js';
import {sendProductToStockUpdateQueue} from './src/modules/product/rabbitmq/productStockUpdateSender.js';
import orderRoutes from './src/modules/sales/routes/OrderRoutes.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDB();
//createInitialData();
connectRabbitmq();

app.use(express.json());
app.use(CheckToken);
app.use(orderRoutes);

app.get('/teste', (request, response) => {

    try {
        
        sendProductToStockUpdateQueue([
            {
                productId: 1000,
                quantity: 10
            },
            {
                productId: 1001,
                quantity: 12
            },
            {
                productId: 1002,
                quantity: 25
            }
        ]);
        return response.status(200).json({status:  200});
    } catch (error) {
        
        console.log(error)
        return response.status(500).json({error:  true});
    }
})

app.get('/api/status', async (request, response) => {

    return response.status(200).json({

        service: 'sales-api',
        status: 'up',
        httpStatus: 200
    })
})

app.listen(PORT, () => (

    console.info(`Server Start at port ${PORT}`)
))