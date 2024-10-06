import React from "react";
import "./NotificationPage.css"; // Import the CSS file

const NotificationPage = ({ pendingOrders, lowStockItems }) => {
  return (
    <div className="notification-page">
      <h1>Notifications</h1>

      {pendingOrders.length > 0 && (
        <div className="notification-section">
          <h3>Pending Orders</h3>
          {pendingOrders.map((order) => (
            <div key={order.orderId} className="notification-card">
              <p><strong>Order ID:</strong> {order.orderId}</p>
              <p><strong>Product Name:</strong> {order.productName}</p>
              <p><strong>Status:</strong> {order.status}</p>
            </div>
          ))}
        </div>
      )}

      {lowStockItems.length > 0 && (
        <div className="notification-section">
          <h3>Low Stock Items</h3>
          {lowStockItems.map((product) => (
            <div key={product.productId} className="notification-card">
              <p><strong>Product ID:</strong> {product.productId}</p>
              <p><strong>Product Name:</strong> {product.name}</p>
              <p><strong>Stock:</strong> {product.stock}</p>
            </div>
          ))}
        </div>
      )}

      {/* Add other notification types as needed */}
    </div>
  );
};

export default NotificationPage;
