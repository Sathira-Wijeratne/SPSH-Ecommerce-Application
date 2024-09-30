import React, { useState } from "react";
import { HiShoppingCart } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";
import './MenuBar.css';

const MenuBar = () => {
    const [showSubForVendorMenu, setshowSubForVendorMenu] = useState(false);
    const navigate = useNavigate(); // Initialize the useNavigate hook

    const toggleSubMenuForVendor = () => {
        setshowSubForVendorMenu(!showSubForVendorMenu);
    };

     // Function to navigate to the Dashboard
     const onClickDashboard = (e) => {
        e.preventDefault();
        navigate("/Admin/AdminDashboard");
    };

    // Function to navigate to the all users 
    const onClickLoadAllUSers = (e) => {
        e.preventDefault();
        navigate("/Admin/UserManagement/EditUser/Dashboard");
    };

    // Function to navigate to the vendor management 
    const onClickVendorManagement = (e) => {
        e.preventDefault();
        navigate("/Admin/VendorManagement/NewVendor");
    };

       // Function to navigate to the vendor management 
       const onClickAllVendorManagement = (e) => {
        e.preventDefault();
        navigate("/Admin/VendorManagement/AllVendor");
    };

    //function to navigate to ordermanagement
    const onClickAllOrderManagement = (e) => {
        e.preventDefault();
        navigate("/Admin/OrderManagement");
    };


    return (
        <div className="menu">
            <div className="logo">
                <HiShoppingCart className="logo-icon" />
                <h2>SPSH</h2>
            </div>

            <div className="menulist">
                <a href="#" className="item" onClick={onClickDashboard}>Dashboard</a>

             
                <a href="#" className="item" onClick={onClickLoadAllUSers}>User Management</a>
                
             

                  {/* Vendor Management Menu with Sub-Menus */}
                  <div className="item" onClick={toggleSubMenuForVendor}>
                    Vendor Management
                </div>
                
                {showSubForVendorMenu && (
                    <div className="submenu">
                        <a href="#" className="subitem" onClick={onClickVendorManagement}>
                            Create New Vendor
                        </a>
                        <a href="#" className="subitem"onClick={onClickAllVendorManagement}>Existing Vendor</a>
                    </div>
                )}



                {/* <a href="#" className="item">Vendor Management</a> */}
                <a href="#" className="item" >Product Management</a>
                <a href="#" className="item"onClick={onClickAllOrderManagement}>Order Management</a>
             
            </div>
        </div>
    );
};

export default MenuBar;
