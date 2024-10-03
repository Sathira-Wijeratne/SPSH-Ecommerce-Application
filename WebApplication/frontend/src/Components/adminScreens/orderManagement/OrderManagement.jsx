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

  // Function to mark an order as "Delivered"
  const markAsCompleted = (order) => {
    var userRes = window.confirm(
      `Are you sure you want to mark ${order.orderId} order as completed?`
    );
    if (userRes === true) {
      if (order.status === "Processing") {
        axios
          .get(`http://192.168.137.1:2030/api/Products/${order.productId}`)
          .then((res1) => {
            if (order.productQuantity <= res1.data.stock) {
              const updatedProduct = {
                id: res1.data.id,
                productId: res1.data.productId,
                productCategory: res1.data.productCategory,
                vendorEmail: res1.data.vendorEmail,
                name: res1.data.name,
                description: res1.data.description,
                price: res1.data.price,
                stock: res1.data.stock - order.productQuantity,
                imageBase64: res1.data.imageBase64,
              };
              axios
                .put(
                  `http://192.168.137.1:2030/api/Products/${res1.data.productId}`,
                  updatedProduct
                )
                .then((res2) => {
                  if (res2.status === 200) {
                    axios
                      .patch(
                        `http://192.168.137.1:2030/api/Orders/manage/${order.orderId}?status=Completed`
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
                });
            } else {
              alert("Insufficient Stocks!");
            }
          });
      } else {
        axios
          .patch(
            `http://192.168.137.1:2030/api/Orders/manage/${order.orderId}?status=Completed`
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
                      onClick={() => markAsCompleted(order)}
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
