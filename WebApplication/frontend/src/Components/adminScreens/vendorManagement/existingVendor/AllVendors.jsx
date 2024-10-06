import React, { useState, useEffect } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa"; // Icons for actions
import { useNavigate } from "react-router-dom";
import "./AllVendor.css";
import MenuBar from "../../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const AllVendor = () => {
  const navigate = useNavigate();
  const [vendors, setVendors] = useState([]);
  const [showDialog, setShowDialog] = useState(false);  // To toggle the dialog
  const [selectedUserEmail, setSelectedUserEmail] = useState(null); // To store selected user's email

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

  // Handle the view action
  const handleView = (email) => {
    navigate(`/Admin/UserManagement/ViewUser/${email}`);
  };

  // Handle the edit action
  const handleEdit = (email) => {
    navigate(`/Admin/UserManagement/EditUser/${email}`);
  };

  // Function to show dialog on delete icon click
  const confirmDelete = (email) => {
    setSelectedUserEmail(email); // Store the selected vendor's email
    setShowDialog(true); // Show the confirmation dialog
  };

  // Function to handle actual deletion after confirmation
  const handleDelete = () => {
    axios
      .delete(`http://192.168.137.1:2030/api/Users/${selectedUserEmail}`)
      .then((res) => {
        if (res.status === 200) {
          alert("Vendor account deleted!");
          setShowDialog(false); // Hide the dialog
          setVendors(vendors.filter((vendor) => vendor.email !== selectedUserEmail)); // Remove the deleted vendor from the list
        }
      })
      .catch((err) => {
        alert(err);
        setShowDialog(false); // Hide the dialog if an error occurs
      });
  };

  return (
    <div className="user-list-container">
      <h1 className="hello">Vendor Management</h1>
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
                  onClick={() => confirmDelete(vendor.email)} // Show dialog instead of alert
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <MenuBar />

      {/* Confirmation Dialog Box */}
      {showDialog && (
        <div className="dialog-overlay">
          <div className="dialog-box">
            <h3>Confirm Deletion</h3>
            <p>
              Are you sure you want to delete the vendor with email{" "}
              <strong>{selectedUserEmail}</strong>?
            </p>
            <div className="dialog-buttons">
              <button className="btn btn-danger" onClick={handleDelete}>
                Yes, Delete
              </button>
              <button className="btn btn-secondary" onClick={() => setShowDialog(false)}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AllVendor;
