import OrderService from "../service/OrderService.js";

class OrderController{

    async createOrder(request, response){

        let order = await OrderService.createOrder(request);
        return response.status(order.status).json(order);
    };

    async findById(request, response){

        let order = await OrderService.findById(request);
        return response.status(order.status).json(order);
    }

    async findAll(request, response){

        let order = await OrderService.findAll();
        return response.status(order.status).json(order);
    }

    async findByProductId(request, response){

        let order = await OrderService.findByProductId(request);
        return response.status(order.status).json(order);
    }
}

export default new OrderController();