import React, { useEffect, useState } from "react";
import AdminMenuBar from "../adminDashboard/menuBar/MenuBar";
import "./OrderManagement.css"; // Import CSS for this screen
import axios from "axios";

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    axios.get("http://192.168.137.1:2030/api/Orders").then((res) => {
      console.log(res.data);
      setOrders(res.data);
    });
  }, []);

  // Sample data for orders, you can replace this with your fetched data.
  // const [orders, setOrders] = useState([
  //     {
  //         OrderId: "0001",
  //         ProductName: "Galaxy Z Fold 6",
  //         CustomerEmail: "customer@spsh.lk",
  //         Status: "Processing",
  //         ProductUnitPrice: 578900,
  //     },
  //     {
  //         OrderId: "0002",
  //         ProductName: "Asus Zenbook 14x OLED",
  //         CustomerEmail: "customer2@spsh.lk",
  //         Status: "Cancelled",
  //         ProductUnitPrice: 678900,
  //     },
  // ]);

  // Function to mark an order as "Delivered"
  const markAsCompleted = (orderId) => {
    var userRes = window.confirm(
      `Are you sure you want to mark ${orderId} order as completed?`
    );
    if (userRes === true) {
      axios
        .patch(
          `http://192.168.137.1:2030/api/Orders/manage/${orderId}?status=Completed`
        )
        .then((res) => {
          if (res.status === 200) {
            window.location.reload();
          } else {
            alert("Network error!");
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
  };

  // Function to mark an order as "Cancelled"
  const markAsCancelled = (orderId) => {
    var userRes = window.confirm(
      `Are you sure you want cancel ${orderId} order?`
    );
    if (userRes === true) {
      axios
        .patch(
          `http://192.168.137.1:2030/api/Orders/manage/${orderId}?status=Cancelled`
        )
        .then((res) => {
          if (res.status === 200) {
            window.location.reload();
          } else {
            alert("Network error!");
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
  };

  return (
    <div className="order-management">
      <AdminMenuBar />

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
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.productName}</td>
                <td>{order.customerEmail}</td>
                <td>{order.status}</td>
                <td>Rs. {order.productUnitPrice}</td>
                <td>
                  {order.status === "Processing" ||
                  order.status === "Delivered" ? (
                    <button
                      className="deliver-btn"
                      onClick={() => markAsCompleted(order.orderId)}
                    >
                      Mark as Completed
                    </button>
                  ) : order.status === "Requested to cancel" ? (
                    <button
                      className="cancel-btn"
                      onClick={() => markAsCancelled(order.orderId)}
                    >
                      Cancel Order
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
