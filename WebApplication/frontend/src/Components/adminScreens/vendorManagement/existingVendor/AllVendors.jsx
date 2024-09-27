import React, { useState, useEffect } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa"; // Icons for actions
import { useNavigate } from "react-router-dom";
import "./AllVendor.css";
import MenuBar from "../../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const AllVendor = () => {
  const navigate = useNavigate();
  const [vendors, setVendors] = useState([]);

  useEffect(() => {
    axios
      .get("http://192.168.137.1:2030/api/Users/get-by-category/Vendor")
      .then((res) => {
        setVendors(res.data);
      })
      .catch((err) => {
        alert("Network error!");
      });
  }, []);

  const handleView = (email) => {
    navigate(`/Admin/UserManagement/ViewUser/${email}`);
  };

  const handleEdit = (email) => {
    navigate(`/Admin/UserManagement/EditUser/${email}`);
  };

  const handleDelete = (email) => {
    // Logic to delete user (API call or local state update)
    console.log("Delete user with id: ", email);
  };

  return (
    <div className="user-list-container">
      <h2>Vendor Management</h2>
      <table className="user-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Activated</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {vendors.map((vendor) => (
            <tr key={vendor.id}>
              <td>{vendor.name}</td>
              <td>{vendor.email}</td>
              <td>{vendor.role}</td>
              <td>{vendor.activated ? "Yes" : "No"}</td>
              <td className="action-icons">
                <FaEye
                  className="icon view-icon"
                  title="View"
                  onClick={() => handleView(vendor.email)}
                />
                <FaEdit
                  className="icon edit-icon"
                  title="Edit"
                  onClick={() => handleEdit(vendor.email)}
                />
                <FaTrash
                  className="icon delete-icon"
                  title="Delete"
                  onClick={() => handleDelete(vendor.email)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <MenuBar />
    </div>
  );
};

export default AllVendor;
