import OrderRepository from "../repository/OrderRepository.js";
import {sendProductToStockUpdateQueue} from '../../product/rabbitmq/productStockUpdateSender.js';
import {PENDING, ACCEPTED, REJECTED} from '../status/OrderStatus.js';
import OrderException from '../exception/OrderException.js';
import {BAD_REQUEST, INTERNAL_SERVER_ERROR, SUCCESS} from '../../../config/constants/HttpStatus.js';
import ProductClient from "../../product/client/ProductClient.js";

class OrderService{

    async createOrder(request){

        try {
            let orderData = request.body;

            const {transactionid, serviceid} = request.headers;
            console.info(`Request to Post new order with data ${JSON.stringify(orderData)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);
            
            this.validateOrderData(orderData);
            const {authUser} = request;
            const {authorization} = request.headers;
            let order = {

                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData.products,
                transactionid,
                serviceid
            }

            await this.validateProductStock(order, authorization, transactionid);

            let createdOrder = await OrderRepository.save(order);
            this.sendMessage(createdOrder, transactionid);

            let response = {

                status: SUCCESS,
                createdOrder
            }

            console.info(`Response to Post new order with data ${JSON.stringify(response)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

            return response;

        } catch (error) {
            
            return {

                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    async updateOrder(orderMessage){

        try {
            
            const order = JSON.parse(orderMessage);

            if (order.status && order.salesId){

                let existingOrder = await OrderRepository.findById(order.salesId);
    
                if (existingOrder && order.status !== existingOrder.status){
    
                    existingOrder.status = order.status;
                    existingOrder.updatedAt = new Date();
                    await OrderRepository.save(existingOrder);
                }
            } else {

                console.warn(`The order message was not complete. TransactionId: ${orderMessage.transactionid}`);
            }

        } catch (error) {
            
            console.error("Could not parse order message from queue");
            console.error(error.message);
        }
    }

    validateOrderData(data){

        if (!data || !data.products){

            throw new OrderException(BAD_REQUEST, 'the order data or products must be informed');
        }
    }

    async validateProductStock(order, token, transactionid){

        let stockIsOK = await ProductClient.checkProductStock(order, token, transactionid);

        if (!stockIsOK){

            throw new OrderException(BAD_REQUEST, 'The stock is out.');
        }
    }

    sendMessage(order, transactionid){

        const message = {

            salesId: order.id,
            products: order.products,
            transactionid
        }
        
        sendProductToStockUpdateQueue(message);
    }

    async findById(request){

        
        const {id} = request.params;
        const {transactionid, serviceid} = request.headers;
        console.info(`Request to Get sale by ID ${id} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        this.validateInformedId(id);
        const existingOrder = await OrderRepository.findById(id);

        if (!existingOrder){

            return {

                status: BAD_REQUEST,
                message: 'Order not found'
            }
        }

        let response = {

            status: SUCCESS,
            existingOrder
        }

        console.info(`Response to Get sale by ID ${id}: Response: ${JSON.stringify(response)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        return response
    }

    async findAll(){

        const orders = await OrderRepository.findAll();
        const {transactionid, serviceid} = request.headers;
        console.info(`Request to Get all sales | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        if (!orders){

            return {

                status: BAD_REQUEST,
                message: 'Orders not found'
            }
        }

        let response = {

            status: SUCCESS,
            orders
        }

        console.info(`Response to Get all sales : Response: ${response} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        return response;
    }

    async findByProductId(request){

        const {productId} = request.params;
        this.validateInformedProductId(productId);
        const orders = await OrderRepository.findByProductId(productId);
        const {transactionid, serviceid} = request.headers;

        console.info(`Request to Get all sales by Product ID ${productId} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        if (!orders){

            return {

                status: BAD_REQUEST,
                message: 'Orders not found'
            }
        }

        let response = {

            status: SUCCESS,
            salesIds: orders.map((order) => {
                return order.id;
            })
        }

        console.info(`Response to Get all sales by Product ID ${productId}: Response: ${JSON.stringify(response)} | TransactionId: ${transactionid} | ServiceId: ${serviceid}`);

        return response;
    }

    validateInformedId(id){

        if (!id){

            throw new OrderException(BAD_REQUEST, 'The order ID must be informed.');
        }
    }

    validateInformedProductId(id){

        if (!id){

            throw new OrderException(BAD_REQUEST, 'The product ID must be informed.');
        }
    }
}

export default new OrderService();