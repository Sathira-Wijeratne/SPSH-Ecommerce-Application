import React, { useState, useEffect } from 'react';
import { FaEye, FaEdit, FaTrash } from 'react-icons/fa'; // Icons for actions
import { useNavigate } from 'react-router-dom';
import './AllVendor.css';
import MenuBar from '../../adminDashboard/menuBar/MenuBar';

const users = [
    {
        id: 1,
        name: 'John Doe',
        email: 'john.doe@example.com',
        role: 'Admin',
        activated: true,
    },
    {
        id: 2,
        name: 'Jane Smith',
        email: 'jane.smith@example.com',
        role: 'User',
        activated: false,
    },
    // Add more users here for demonstration
];

const AllVendor = () => {
    const navigate = useNavigate();

    const handleView = (id) => {
        navigate(`/Admin/UserManagement/ViewUser/${id}`);
    };

    const handleEdit = (id) => {
        navigate(`/Admin/UserManagement/EditUser/${id}`);
    };

    const handleDelete = (id) => {
        // Logic to delete user (API call or local state update)
        console.log('Delete user with id: ', id);
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
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.name}</td>
                            <td>{user.email}</td>
                            <td>{user.role}</td>
                            <td>{user.activated ? 'Yes' : 'No'}</td>
                            <td className="action-icons">
                                <FaEye
                                    className="icon view-icon"
                                    title="View"
                                    onClick={() => handleView(user.id)}
                                />
                                <FaEdit
                                    className="icon edit-icon"
                                    title="Edit"
                                    onClick={() => handleEdit(user.id)}
                                />
                                <FaTrash
                                    className="icon delete-icon"
                                    title="Delete"
                                    onClick={() => handleDelete(user.id)}
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <MenuBar/>
        </div>
    );
};

export default AllVendor;
