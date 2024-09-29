import React, { useState } from 'react';
import VendorMenuBar from '../vendorMenuBar/VendorMenuBar'; // Import the VendorMenuBar
import './OrderManagement.css'; // Import CSS for this screen

const OrderManagement = () => {
    // Sample data for orders, you can replace this with your fetched data.
    const [orders, setOrders] = useState([
        {
            OrderId: "0001",
            ProductName: "Galaxy Z Fold 6",
            CustomerEmail: "customer@spsh.lk",
            Status: "Processing",
            ProductUnitPrice: 578900,
        },
        {
            OrderId: "0002",
            ProductName: "Asus Zenbook 14x OLED",
            CustomerEmail: "customer2@spsh.lk",
            Status: "Cancelled",
            ProductUnitPrice: 678900,
        },
    ]);

    // Function to mark an order as "Delivered"
    const markAsDelivered = (orderId) => {
        const updatedOrders = orders.map(order => {
            if (order.OrderId === orderId && order.Status === "Processing") {
                return { ...order, Status: "Delivered" };
            }
            return order;
        });
        setOrders(updatedOrders);
    };

    return (
        <div className="order-management">
            <VendorMenuBar /> 

            <div className="order-management--content">
                <h1 className="header-title">Manage Orders</h1> 
                <table className="order-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Product Name</th>
                            <th>Customer Email</th>
                            <th>Status</th>
                            <th>Price</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((order) => (
                            <tr key={order.OrderId}>
                                <td>{order.OrderId}</td>
                                <td>{order.ProductName}</td>
                                <td>{order.CustomerEmail}</td>
                                <td>{order.Status}</td>
                                <td>Rs. {order.ProductUnitPrice.toLocaleString()}</td>
                                <td>
                                    {order.Status === "Processing" ? (
                                        <button 
                                            className="deliver-btn" 
                                            onClick={() => markAsDelivered(order.OrderId)}
                                        >
                                            Mark as Delivered
                                        </button>
                                    ) : (
                                        <span>-</span>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrderManagement;
