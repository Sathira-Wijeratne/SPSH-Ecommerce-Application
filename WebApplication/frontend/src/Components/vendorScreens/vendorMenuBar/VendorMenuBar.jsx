import React, { useState } from "react";
import { HiShoppingCart } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";
import './VendorMenuBar.css';

const VendorMenuBar = () => {
    const [showSubMenu, setShowSubMenu] = useState(false);
    const [showSubForVendorMenu, setshowSubForVendorMenu] = useState(false);
    const navigate = useNavigate(); // Initialize the useNavigate hook

    const toggleSubMenu = () => {
        setShowSubMenu(!showSubMenu);
    };

    const toggleSubMenuForVendor = () => {
        setshowSubForVendorMenu(!showSubForVendorMenu);
    };




     // Function to navigate to the Dashboard
     const onClickDashboard = (e) => {
        e.preventDefault();
        navigate("/Vendor/VendorDashboard");
    };

     // Function to navigate to add product page 
     const onClickAddProduct = (e) => {
        e.preventDefault();
        navigate("/Vendor/AddNewProducts");
    };




    return (
        <div className="menu">
            <div className="logo">
                <HiShoppingCart className="logo-icon" />
                <h2>SPSH</h2>
            </div>

            <div className="menulist">
                <a href="#" className="item" onClick={onClickDashboard}>Dashboard</a>
                <a href="#" className="item" onClick={onClickAddProduct}>Add Products</a>
                <a href="#" className="item" onClick={onClickDashboard}>Shipment Management</a>
                
            </div>
        </div>
    );
};

export default VendorMenuBar;
