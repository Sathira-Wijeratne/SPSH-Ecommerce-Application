import React from "react";
import { BiNotification } from "react-icons/bi";
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation
import "./DashboardContent.css";

const DashboardContent = ({ pendingOrders, lowStockItems }) => {
  const navigate = useNavigate(); // Hook for navigation

  // Check if there's any pending orders or low stock items
  const hasNotifications = pendingOrders.length > 0 || lowStockItems.length > 0;

  // Function to handle notification icon click
  const handleNotificationClick = () => {
    navigate("/Admin/NotificationPannel"); // Navigate to the notification panel
  };

  return (
    <div className="content--header">
      <h1 className="hello">Dashboard</h1>
      <div className="header--activity">
        <div className="profile"></div>
        <div
          className="notify"
          // onClick={handleNotificationClick}
        >
          {" "}
          {/* Add onClick to navigate */}
          <BiNotification className="notification-icon" />
          {hasNotifications && <span className="notification-badge"></span>}
        </div>
      </div>
    </div>
  );
};

export default DashboardContent;
