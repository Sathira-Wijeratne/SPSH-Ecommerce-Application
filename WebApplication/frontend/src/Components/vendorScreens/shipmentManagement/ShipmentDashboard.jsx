import React, { useEffect, useState } from "react";
import VendorMenuBar from "../vendorMenuBar/VendorMenuBar"; // Import the VendorMenuBar
import "./ShipmentDashboard.css"; // Import CSS for this screen
import axios from "axios";

const ShipmentDashboard = () => {
  const vendorEmail = sessionStorage.getItem("email");
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    axios
      .get(`http://192.168.137.1:2030/api/Orders/vendor/${vendorEmail}`)
      .then((res) => {
        console.log(res.data);
        setOrders(res.data);
      });
  }, []);

  // Function to mark an order as "Delivered"
  const markAsDelivered = (orderId) => {
    var userRes = window.confirm(
      `Are you sure you want to mark ${orderId} order as delivered?`
    );
    if (userRes === true) {
      axios
        .patch(
          `http://192.168.137.1:2030/api/Orders/manage/${orderId}?status=Delivered`
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
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.productName}</td>
                <td>{order.customerEmail}</td>
                <td>{order.status}</td>
                <td>Rs. {order.productUnitPrice}</td>
                <td>
                  {order.status === "Processing" ? (
                    <button
                      className="deliver-btn"
                      onClick={() => markAsDelivered(order.orderId)}
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

export default ShipmentDashboard;
