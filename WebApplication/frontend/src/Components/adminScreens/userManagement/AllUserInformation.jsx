import React, { useState, useEffect } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa"; // Icons for actions
import { useNavigate } from "react-router-dom";
import "./AllUserInformation.css";
import MenuBar from "../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const UserList = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);

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
  

  const handleDelete = (email) => {
    var userRes = window.confirm(
      `Are you sure you want to delete this customer?\n\nCustomer Email: ${email}`
    );
    if (userRes === true) {
      axios
        .delete(`http://192.168.137.1:2030/api/Users/${email}`)
        .then((res) => {
          if (res.status === 200) {
            alert("Customer account deleted!");
            window.location.reload();
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
  };

  return (
    <div className="user-list-container">
      <h2>User Management</h2>
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
                  onClick={() => handleDelete(user.email)}
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

export default UserList;
