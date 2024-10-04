import React from "react";
import { BiNotification } from "react-icons/bi";
import "./VendorDashboardContent.css";
import { useNavigate } from "react-router-dom";

const VendorDashboardContent = ({ lowStockItems }) => {
  const hasNotifications = lowStockItems.length > 0;
  const navigate = useNavigate();

  return (
    <div className="content--header">
      <h1 className="header-title">Vendor Dashboard</h1>
      <div className="header--activity">
        <div className="profile"></div>
        <div className="notify">
          <BiNotification
            className="notification-icon"
            onClick={(e) => {
              e.preventDefault();
              if (hasNotifications === true) {
                navigate("/Vendor/StockManagement");
              }
            }}
          />
          {hasNotifications && <span className="notification-badge"></span>}
        </div>
      </div>
    </div>
  );
};
export default VendorDashboardContent;
