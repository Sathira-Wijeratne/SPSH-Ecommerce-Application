import React from "react";
import './Login.css';

const Login =()=>{
    return(
        <div className="Wrapper">
            <form action="">
                <h1>Login</h1>
                {/* username */}
                <div className="input-box">
                    <input type="text" placeholder="Username" required/>
                </div>
                {/* password */}
                <div className="input-box">
                    <input type="password" placeholder="Password" required/>
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