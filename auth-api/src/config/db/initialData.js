import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export async function createInitalData(){

    try {
     
        await User.sync({force: true});

        let password = await bcrypt.hash('12345', 10);

        await User.create({

            name: "Bruno",
            email: "bruno@gmail.com",
            password: password
        })

        await User.create({

            name: "Ana",
            email: "ana@gmail.com",
            password: password
        })

    } catch (error) {
        
        console.log(error);
    }
}