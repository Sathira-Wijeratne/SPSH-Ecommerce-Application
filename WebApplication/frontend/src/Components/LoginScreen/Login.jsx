import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

import { FaUser, FaLock } from "react-icons/fa";
import backgroundImage from "../Assets/loginimage.jpg";
import axios from "axios";

const Login = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [userDetails, setUserDetails] = useState({});

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
    axios
      .get(`http://192.168.137.1:2030/api/Users/${username}`)
      .then((res) => {
        console.log(res.data);
        setUserDetails(res.data);
        if (res.data.password === password) {
          if (res.data.role === "Admin") {
            sessionStorage.setItem("email", res.data.email);
            navigate("/Admin/AdminDashboard");
          } else if (res.data.role === "CSR") {
            sessionStorage.setItem("email", res.data.email);
            navigate("/CSR/CSRDashboard");
          } else if (res.data.role === "Vendor") {
            sessionStorage.setItem("email", res.data.email);
            navigate("/Vendor/VendorDashboard");
          } else {
            alert("Unauthorized User!");
          }
        } else {
          alert("Invalid Credentials!");
        }
      })
      .catch((err) => {
        alert("Invalid Credentials!");
      });
  };

  return (
    <div style={myStyle}>
      <div className="Wrapper">
        <form action="">
          <h1>Login</h1>
          {/* username */}
          <div className="input-box">
            <input
              type="text"
              placeholder="Username"
              onChange={(e) => {
                setUsername(e.target.value);
              }}
              required
            />
            <FaUser className="icon" />
          </div>
          {/* password */}
          <div className="input-box">
            <input
              type="password"
              placeholder="Password"
              required
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            />
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
