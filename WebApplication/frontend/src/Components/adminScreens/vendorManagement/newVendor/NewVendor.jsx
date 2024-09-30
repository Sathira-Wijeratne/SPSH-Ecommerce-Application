import React, { useState } from "react";
import "./NewVendor.css";
import { useNavigate } from "react-router-dom";
import MenuBar from "../../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const NewVendor = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    id: "",
    name: "",
    role: "Vendor",
    email: "",
    password: "",
    activated: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(formData);
    axios
      .post("http://192.168.137.1:2030/api/Users", formData)
      .then((res) => {
        alert("Vendor Registration Successful!");
        navigate("/Admin/VendorManagement/AllVendor");
      })
      .catch((err) => {
        alert(err);
      });
  };

  return (
    <div className="dashboard">
      <MenuBar />
      <div className="form-container">
        <h2>Create New Vendor</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name:</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Password:</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Activated:</label>
            <input
              type="checkbox"
              name="activated"
              checked={formData.activated}
              onChange={handleChange}
            />
          </div>

          <button type="submit" className="submit-btn">
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default NewVendor;
