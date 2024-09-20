import React from "react";
import './Login.css';
import { FaUser,FaLock} from "react-icons/fa";


const Login =()=>{
    return(
        <div className="Wrapper">
            <form action="">
                <h1>Login</h1>
                {/* username */}
                <div className="input-box">
                    <input type="text" placeholder="Username" required/>
                    <FaUser className="icon"/>

                </div>
                {/* password */}
                <div className="input-box">
                    <input type="password" placeholder="Password" required/>
                    <FaLock className="icon"/>
                </div>
                <button type="submit">Login</button>

                <div className="register-Link">
                    <p>Don't have an account? <a href="#">Register</a></p>
                </div>
            </form>

        </div>
    )
}
export default Login;