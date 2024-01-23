import Order from '../../modules/sales/model/Order.js';

export async function createInitialData(){

    await Order.collection.drop();

    await Order.create({

        products: [
            {

                productId: 1000,
                quantity: 10
            },
            {

                productId: 1001,
                quantity: 5
            },
            {

                productId: 1002,
                quantity: 25
            }
        ],
        user: {

            id: 'abc',
            name: 'Bruno',
            email: 'bruno@gmail.com'
        },
        status: 'APROVED',
        createdAt: new Date(),
        updatedAt: new Date()
    });

    await Order.create({

        products: [
            {

                productId: 1000,
                quantity: 3
            },
            {

                productId: 1001,
                quantity: 2
            },
            {

                productId: 1002,
                quantity: 11
            }
        ],
        user: {

            id: 'def',
            name: 'Ana',
            email: 'ana@gmail.com'
        },
        status: 'REJECTED',
        createdAt: new Date(),
        updatedAt: new Date()
    });
    let inicialData = await Order.find();
    console.info(`Initial data: ${JSON.stringify(inicialData, undefined, 4)}`)
}