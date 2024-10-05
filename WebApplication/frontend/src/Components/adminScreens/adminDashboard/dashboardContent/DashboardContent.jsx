import React from "react";
import { BiNotification } from 'react-icons/bi';
import './DashboardContent.css';

const DashboardContent = ({ pendingOrders, lowStockItems }) => {
  // Check if there's any pending orders or low stock items
  const hasNotifications = pendingOrders.length > 0 || lowStockItems.length > 0;

  return (
    <div className="content--header">
      <h1 className="hello">Dashboard</h1>
      <div className="header--activity">
        <div className="profile"></div>
        <div className="notify">
          <BiNotification className="notification-icon" />
          {hasNotifications && <span className="notification-badge"></span>}
        </div>
      </div>
    </div>
  );
};

export default DashboardContent;
