import React, { useState, useEffect } from "react";
import MenuBar from "./menuBar/MenuBar";
import Content from "./dashboardContent/DashboardContent";
import "./AdminDashboardScreen.css";
import axios from "axios";

import Cards from "./cards/Cards";

const AdminDashboard = () => {
  const [pendingOrders, setPendingOrders] = useState([]);
  const [lowStockItems, setLowStockItems] = useState([]);

  useEffect(() => {
    // Fetch Pending Orders
    axios
      .get("http://192.168.137.1:2030/api/Orders")
      .then((response) => {
        const orders = response.data;
        const pending = orders.filter(
          (order) =>
            order.status === "Processing" || order.status === "Delivered"
        );
        setPendingOrders(pending);
      })
      .catch((error) => {
        console.error("Error fetching orders:", error);
      });

    // Fetch Products with Low Stock
    axios
      .get("http://192.168.137.1:2030/api/Products")
      .then((response) => {
        const products = response.data;
        const lowStock = products.filter((product) => product.stock <= 10); // Assuming 10 is the threshold
        setLowStockItems(lowStock);
      })
      .catch((error) => {
        console.error("Error fetching products:", error);
      });
  }, []);

  return (
    <div className="content">
      <MenuBar />
      <div className="dashboard--content">
        <Content pendingOrders={pendingOrders} lowStockItems={lowStockItems} />
        <Cards />

        {/* Conditionally render Pending Orders table */}
        {pendingOrders.length > 0 && (
          <>
            <h2>Pending Orders</h2>
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Product Name</th>
                  <th>Customer Email</th>
                  <th>Quantity</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {pendingOrders.map((order) => (
                  <tr key={order.orderId}>
                    <td>{order.orderId}</td>
                    <td>{order.productName}</td>
                    <td>{order.customerEmail}</td>
                    <td>{order.productQuantity}</td>
                    <td>{order.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}

        {/* Conditionally render Low Stock Items table */}
        {lowStockItems.length > 0 && (
          <>
            <h2>Low Stock Items</h2>
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Product ID</th>
                  <th>Product Name</th>
                  <th>Stock</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                {lowStockItems.map((product) => (
                  <tr key={product.productId}>
                    <td>{product.productId}</td>
                    <td>{product.name}</td>
                    <td>{product.stock}</td>
                    <td>${product.price}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;
