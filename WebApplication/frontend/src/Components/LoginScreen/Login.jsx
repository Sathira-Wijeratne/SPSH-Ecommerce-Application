import React from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

import { FaUser, FaLock } from "react-icons/fa";
import backgroundImage from "../assets/loginimage.jpg";

const Login = () => {
  const navigate = useNavigate();

  //Background Image related inline css
  const myStyle = {
    backgroundImage: `url(${backgroundImage})`,
    height: "100vh",
    width: "100vw",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundPosition: "center",
    backgroundSize: "cover",
    backgroundRepeat: "no-repeat",
  };

  //Function to navigate to Admin Dashboard Screen
  const onClickLogin = (e) => {
    e.preventDefault();
    navigate("/Admin/AdminDashboard");
  };

  return (
    <div style={myStyle}>
      <div className="Wrapper">
        <form action="">
          <h1>Login</h1>
          {/* username */}
          <div className="input-box">
            <input type="text" placeholder="Username" required />
            <FaUser className="icon" />
          </div>
          {/* password */}
          <div className="input-box">
            <input type="password" placeholder="Password" required />
            <FaLock className="icon" />
          </div>
          <button type="submit" onClick={onClickLogin}>
            Login
          </button>

          <div className="register-Link">
            {/* <p>Don't have an account? <a href="#">Register</a></p> */}
          </div>
        </form>
      </div>
    </div>
  );
};
export default Login;
