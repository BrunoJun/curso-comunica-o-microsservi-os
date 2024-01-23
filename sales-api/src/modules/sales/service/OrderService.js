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
            this.validateOrderData(orderData);
            const {authUser} = request;
            const {authorization} = request.headers;
            let order = {

                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData.products
            }

            await this.validateProductStock(order, authorization);

            let createdOrder = await OrderRepository.save(order);
            this.sendMessage(createdOrder);

            return {

                status: SUCCESS,
                createdOrder
            }
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

                console.warn('The order message was not complete');
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

    async validateProductStock(order, token){

        let stockIsOK = await ProductClient.checkProductStock(order, token);

        if (!stockIsOK){

            throw new OrderException(BAD_REQUEST, 'The stock is out.');
        }
    }

    sendMessage(order){

        const message = {

            salesId: order.id,
            products: order.products
        }
        
        sendProductToStockUpdateQueue(message);
    }

    async findById(request){

        const {id} = request.params;
        this.validateInformedId(id);
        const existingOrder = await OrderRepository.findById(id);

        if (!existingOrder){

            return {

                status: BAD_REQUEST,
                message: 'Order not found'
            }
        }

        return {

            status: SUCCESS,
            existingOrder
        }
    }

    async findAll(){

        const orders = await OrderRepository.findAll();

        if (!orders){

            return {

                status: BAD_REQUEST,
                message: 'Orders not found'
            }
        }

        return {

            status: SUCCESS,
            orders
        }
    }

    async findByProductId(request){

        const {productId} = request.params;
        this.validateInformedProductId(productId);
        const orders = await OrderRepository.findByProductId(productId);

        if (!orders){

            return {

                status: BAD_REQUEST,
                message: 'Orders not found'
            }
        }

        return {

            status: SUCCESS,
            salesIds: orders.map((order) => {
                return order.id;
            })
        }
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