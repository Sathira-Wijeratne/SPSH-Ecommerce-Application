import React, { useState } from "react";
import { HiShoppingCart } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";
import './MenuBar.css';

const MenuBar = () => {
    const [showSubMenu, setShowSubMenu] = useState(false);
    const navigate = useNavigate(); // Initialize the useNavigate hook

    const toggleSubMenu = () => {
        setShowSubMenu(!showSubMenu);
    };

    // Function to navigate to the New User page
    const onClickNewUser = (e) => {
        e.preventDefault();
        navigate("/Admin/UserManagement/NewUser");
    };

     // Function to navigate to the New User page
     const onClickDashboard = (e) => {
        e.preventDefault();
        navigate("/Admin/AdminDashboard");
    };


    return (
        <div className="menu">
            <div className="logo">
                <HiShoppingCart className="logo-icon" />
                <h2>SPSH</h2>
            </div>

            <div className="menulist">
                <a href="#" className="item" onClick={onClickDashboard}>Dashboard</a>

                {/* User Management Menu with Sub-Menus */}
                <div className="item" onClick={toggleSubMenu}>
                    User Management
                </div>
                
                {showSubMenu && (
                    <div className="submenu">
                        <a href="#" className="subitem" onClick={onClickNewUser}>
                            Create New Users
                        </a>
                        <a href="#" className="subitem">Edit User</a>
                    </div>
                )}

                <a href="#" className="item">Vendor Management</a>
                <a href="#" className="item">Product Management</a>
                <a href="#" className="item">Order Management</a>
                <a href="#" className="item">CSR Management</a>
            </div>
        </div>
    );
};

export default MenuBar;
