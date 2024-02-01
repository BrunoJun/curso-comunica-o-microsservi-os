import amqp from  'amqplib/callback_api.js';
import {listenToSalesConfirmationQueue} from '../../modules/sales/rabbitmq/salesConfirmationListerner.js'
import {PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTING_KEY} from './queue.js';
import {RABBIT_MQ_URL} from '../constants/secrets.js';

const TEN_SECONDS = 1000000;
const HALF_MINUTES = 30000;
const CONTAINER_ENV = 'container';

export async function connectRabbitmq(){

    const env = process.env.NODE_ENV;

    connectRabbitmqAndCreateQueues();
}

async function connectRabbitmqAndCreateQueues(){

    amqp.connect(RABBIT_MQ_URL, {timeout: 1000000}, (error, connection) => {

        if (error) {

            throw error;
        }

        console.info('Starting RabbitMQ');

        createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY, PRODUCT_TOPIC);
        createQueue(connection, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTING_KEY, PRODUCT_TOPIC);

        console.info('Queues and Topics were defined');

        setTimeout(function() {

            connection.close()
        }, TEN_SECONDS);
    });

    setTimeout(function() {

        listenToSalesConfirmationQueue();    
    }, TEN_SECONDS);
}

function createQueue(connection, queue, routingKey, topic){

    connection.createChannel((error, channel) => {

        if (error){

            throw error;
        }

        channel.assertExchange(topic, 'topic', {durable: true});
        channel.assertQueue(queue, {durable: true});
        channel.bindQueue(queue, topic, routingKey);
    });
}