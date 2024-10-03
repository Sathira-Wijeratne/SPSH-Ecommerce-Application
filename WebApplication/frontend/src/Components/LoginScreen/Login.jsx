import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import { FaUser, FaLock } from "react-icons/fa";
import backgroundImage from "../assets/loginimage.jpg";
import axios from "axios";

const Login = () => {
  const navigate = useNavigate();
  
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [userDetails, setUserDetails] = useState({});
  const [isLoading, setIsLoading] = useState(false);  // Loading state

  // Background Image related inline css
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

  // Function to handle login
  const onClickLogin = (e) => {
    e.preventDefault();
    setIsLoading(true);  // Start loading
    axios
      .get(`http://192.168.137.1:2030/api/Users/${username}`)
      .then((res) => {
        setUserDetails(res.data);
        setIsLoading(false);  // Stop loading
        if (res.data.password === password) {
          sessionStorage.setItem("email", res.data.email);
          sessionStorage.setItem("role", res.data.role);
          if (res.data.role === "Admin") {
            navigate("/Admin/AdminDashboard");
          } else if (res.data.role === "CSR") {
            navigate("/CSR/CSRDashboard");
          } else if (res.data.role === "Vendor") {
            navigate("/Vendor/VendorDashboard");
          } else {
            alert("Unauthorized User!");
          }
        } else {
          alert("Invalid Credentials!");
        }
      })
      .catch((err) => {
        setIsLoading(false);  // Stop loading on error
        alert("Invalid Credentials!");
      });
  };

  return (
    <div style={myStyle}>
      <div className="Wrapper">
        <form action="">
          <h1>Login</h1>
          
          {/* Display loading spinner if loading */}
          {isLoading ? (
            <div className="loading-spinner">Please wait until we log you in...</div>
          ) : (
            <>
              {/* Username Input */}
              <div className="input-box">
                <input
                  type="text"
                  placeholder="Username"
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
                <FaUser className="icon" />
              </div>

              {/* Password Input */}
              <div className="input-box">
                <input
                  type="password"
                  placeholder="Password"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                />
                <FaLock className="icon" />
              </div>

              {/* Login Button */}
              <button type="submit" onClick={onClickLogin}>
                Login
              </button>
            </>
          )}
        </form>
      </div>
    </div>
  );
};

export default Login;
