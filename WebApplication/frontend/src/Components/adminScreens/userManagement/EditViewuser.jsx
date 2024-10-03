import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./AllUserInformation.css"; // Using the same CSS
import MenuBar from "../adminDashboard/menuBar/MenuBar";

const EditViewuser = () => {
  const { email } = useParams(); // Get the user's email from the route parameters
  const navigate = useNavigate();

  // State to toggle between view and edit mode
  const [isEditing, setIsEditing] = useState(false);

  // User state to hold the user data
  const [user, setUser] = useState({
    id: "",
    name: "",
    email: "",
    role: "",
    password: "",
    activated: false,
  });

  useEffect(() => {
    // Fetch user data by email
    axios
      .get(`http://192.168.137.1:2030/api/Users/${email}`)
      .then((res) => setUser(res.data))
      .catch((err) => console.error("Failed to fetch user data", err));
  }, [email]);

  // Handle input change for editable fields
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  // Handle input change for select options
  const handleBoolInputChange = (e) => {
    const { name, value } = e.target;
    if (value === "true") {
      setUser({ ...user, [name]: Boolean(true) });
    } else {
      setUser({ ...user, [name]: Boolean(false) });
    }
  };

  // Submit the updated user data
  const handleSubmit = (e) => {
    e.preventDefault();
    // Only submit if we're in edit mode
    console.log(user);
    if (isEditing === true) {
      axios
        .put(`http://192.168.137.1:2030/api/Users/${email}`, user)
        .then(() => {
          alert("User updated successfully");
          setIsEditing(false); // Switch back to view mode after saving
        })
        .catch((err) => console.error("Failed to update user", err));
    } else {
      setIsEditing(true);
    }
  };

  return (
    <div className="user-details-container">
      <MenuBar />
      <h1>{isEditing ? "Edit User" : "View User"}</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={user.name}
            onChange={handleInputChange}
            readOnly={!isEditing}
            className={!isEditing ? "readonly-field" : ""}
          />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleInputChange}
            readOnly
            className="readonly-field" // Email is always readonly
          />
        </div>
        <div className="form-group">
          <label>Role:</label>
          <input
            type="text"
            name="role"
            value={user.role}
            onChange={handleInputChange}
            readOnly={true}
            className="readonly-field"
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={user.password}
            onChange={handleInputChange}
            readOnly={!isEditing}
            className={!isEditing ? "readonly-field" : ""}
          />
        </div>
        <div className="form-group">
          <label>Activated:</label>
          <select
            name="activated"
            value={user.activated}
            onChange={handleBoolInputChange}
            disabled={!isEditing}
            className={!isEditing ? "readonly-field" : ""}
          >
            <option value={true}>Yes</option>
            <option value={false}>No</option>
          </select>
        </div>

        {/* Wrapper div to align buttons */}
        <div className="button-group">
          <button onClick={() => navigate(-1)} type="button">
            Back
          </button>

          {isEditing === true ? (
            <button type="submit">Save Changes</button>
          ) : (
            <button type="submit">Edit User</button>
          )}
        </div>
      </form>
    </div>
  );
};

export default EditViewuser;
