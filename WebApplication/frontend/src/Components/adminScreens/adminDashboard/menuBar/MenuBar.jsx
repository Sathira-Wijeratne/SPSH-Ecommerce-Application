import React, { useState } from "react";
import { HiShoppingCart } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";
import "./MenuBar.css";

const MenuBar = () => {
  const role = sessionStorage.getItem("role");
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

  //function to navigate to Product Management
  const onClickProductManagement = (e) => {
    e.preventDefault();
    navigate("/Admin/ProductManagement");
  };

  // Function to handle logout
  const onClickLogout = () => {
    sessionStorage.clear();
    navigate("/"); // Redirect to the login page after logout
  };

  //Function to navigate to Inventory management 
  const onClickInventoryManagement=(e) =>{
    e.preventDefault();
    navigate("/Admin/InventoryManagement");
  };

  return (
    <div className="menu">
      <div className="logo">
        <HiShoppingCart className="logo-icon" />
        <h2>SPSH</h2>
      </div>

      <div className="menulist">
        {role !== "CSR" ? (
          <a href="#" className="item" onClick={onClickDashboard}>
            Dashboard
          </a>
        ) : (
          <></>
        )}

        <a href="#" className="item" onClick={onClickLoadAllUSers}>
          User Management
        </a>
        <a href="#" className="item" onClick={onClickAllOrderManagement}>
          Order Management
        </a>

        {role !== "CSR" ? (
          <>
            <div className="item" onClick={toggleSubMenuForVendor}>
              Vendor Management
            </div>

            {showSubForVendorMenu && (
              <div className="submenu">
                <a
                  href="#"
                  className="subitem"
                  onClick={onClickVendorManagement}
                >
                  Create New Vendor
                </a>
                <a
                  href="#"
                  className="subitem"
                  onClick={onClickAllVendorManagement}
                >
                  Existing Vendor
                </a>
              </div>
            )}
          </>
        ) : (
          <></>
        )}

        {role !== "CSR" ? (
          <a href="#" className="item" onClick={onClickProductManagement}>
            Product Management
          </a>
        ) : (
          <></>
        )}

        

        <a href="#" className="item" onClick={onClickInventoryManagement}>
          Inventory Management
        </a>


      </div>

      {/* Logout option */}
      <div className="logout" onClick={onClickLogout}>
        Logout
      </div>
    </div>
  );
};

export default MenuBar;
