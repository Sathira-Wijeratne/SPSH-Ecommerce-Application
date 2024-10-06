import React, { useState } from "react";
import './NotificationManagement.css';
import MenuBar from "../adminDashboard/menuBar/MenuBar";

const NotificationPanel = () => {
    // Sample notification data
    const [notifications, setNotifications] = useState([
        { id: 1, message: 'Order #005 Cancelled', type: 'order', route: '/order-management', timestamp: '2023-10-06 10:30' },
        { id: 2, message: 'Low Stock: Galaxy S10', type: 'inventory', route: '/inventory-management', timestamp: '2023-10-05 09:20' },
        { id: 3, message: 'Pending Order: Order #001', type: 'order', route: '/order-management', timestamp: '2023-10-06 12:15' },
    ]);

    // Function to clear all notifications
    const clearAllNotifications = () => {
        setNotifications([]);
    };

    // Sort notifications by timestamp (newest first)
    const sortedNotifications = notifications.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));

    return (
        <div>
            <MenuBar />

            <div className="container notification-panel">
                <h1 className="hello">Notifications</h1>
                <button className="btn btn-danger clear-all-btn" onClick={clearAllNotifications}>Clear All</button>
                <div className="notifications-list">
                    {sortedNotifications.length > 0 ? (
                        sortedNotifications.map((notification) => (
                            <div className="card notification-card" key={notification.id} onClick={() => window.location.href = notification.route}>
                                <div className="card-body">
                                    <h5 className="card-title">Notification #{notification.id}</h5>
                                    <p className="card-text">{notification.message}</p>
                                    <p className="notification-time">{new Date(notification.timestamp).toLocaleString()}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>No notifications available.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default NotificationPanel;
