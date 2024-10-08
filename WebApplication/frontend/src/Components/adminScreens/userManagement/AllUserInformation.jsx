import React, { useState, useEffect } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa"; // Icons for actions
import { useNavigate } from "react-router-dom";
import "./AllUserInformation.css";
import MenuBar from "../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const UserList = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [showDialog, setShowDialog] = useState(false);  // To toggle the dialog
  const [selectedUserEmail, setSelectedUserEmail] = useState("");  // Store the email of the selected user

  useEffect(() => {
    axios
      .get("http://192.168.137.1:2030/api/Users/get-by-category/Customer")
      .then((res) => {
        setUsers(res.data);
      })
      .catch((err) => {
        alert("Network error!");
      });
  }, []);

  const handleView = (email) => {
    navigate(`/Admin/UserManagement/ViewUser/${email}`);
  };
  
  const handleEdit = (email) => {
    navigate(`/Admin/UserManagement/ViewUser/${email}`);
  };

  const confirmDelete = (email) => {
    setSelectedUserEmail(email);
    setShowDialog(true);  // Show the dialog box
  };

  const handleDelete = () => {
    axios
      .delete(`http://192.168.137.1:2030/api/Users/${selectedUserEmail}`)
      .then((res) => {
        if (res.status === 200) {
          alert("Customer account deleted!");
          setShowDialog(false);  // Close the dialog box
          window.location.reload();
        }
      })
      .catch((err) => {
        alert(err);
        setShowDialog(false);  // Close the dialog box on error
      });
  };

  return (
    <div className="allinfocont">
      <h1 className="hello">User Management</h1>
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
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.activated ? "Yes" : "No"}</td>
              <td className="action-icons">
                <FaEye
                  className="icon view-icon"
                  title="View"
                  onClick={() => handleView(user.email)}
                />
                <FaEdit
                  className="icon edit-icon"
                  title="Edit"
                  onClick={() => handleEdit(user.email)}
                />
                <FaTrash
                  className="icon delete-icon"
                  title="Delete"
                  onClick={() => confirmDelete(user.email)}
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
            <p>Are you sure you want to delete the user with email <strong>{selectedUserEmail}</strong>?</p>
            <div className="dialog-buttons">
              <button onClick={handleDelete}>Yes, Delete</button>
              <button onClick={() => setShowDialog(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserList;
