import React, { useState, useEffect } from "react";
import MenuBar from './menuBar/MenuBar';
import Content from './dashboardContent/DashboardContent';
import './AdminDashboardScreen.css';
import axios from "axios";

import Cards from "./cards/Cards";

const AdminDashboard = () => {
    const [pendingOrders, setPendingOrders] = useState([]);
    const [lowStockItems, setLowStockItems] = useState([]);

    useEffect(() => {
      // Fetch Pending Orders
      axios.get("http://your-api-url.com/orders")
        .then(response => {
          const orders = response.data;
          const pending = orders.filter(order => order.status === "Pending");
          setPendingOrders(pending);
        })
        .catch(error => {
          console.error("Error fetching orders:", error);
        });

      // Fetch Products with Low Stock
      axios.get("http://your-api-url.com/products")
        .then(response => {
          const products = response.data;
          const lowStock = products.filter(product => product.stock <= 5); // Assuming 5 is the threshold
          setLowStockItems(lowStock);
        })
        .catch(error => {
          console.error("Error fetching products:", error);
        });
    }, []);

    return (
        <div className="content">
            <MenuBar />
            <div className="dashboard--content">
                <Content
                  pendingOrders={pendingOrders}
                  lowStockItems={lowStockItems}
                />
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
                          <tr key={order.OrderId}>
                            <td>{order.OrderId}</td>
                            <td>{order.ProductName}</td>
                            <td>{order.CustomerEmail}</td>
                            <td>{order.ProductQuantity}</td>
                            <td>{order.Status}</td>
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
                          <tr key={product.ProductId}>
                            <td>{product.ProductId}</td>
                            <td>{product.ProductName}</td>
                            <td>{product.Stock}</td>
                            <td>${product.Price}</td>
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
