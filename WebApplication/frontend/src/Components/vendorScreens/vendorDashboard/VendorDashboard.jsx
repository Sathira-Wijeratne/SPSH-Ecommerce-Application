import React, { useEffect, useState } from "react";
import MenuBar from "../vendorMenuBar/VendorMenuBar";
import Content from "./VendorDashboardContent";
import Card from "./VendorDashbordCard";
import "./VendorDashboard.css";
import axios from "axios";

const VendorDashboard = () => {
  const vendorEmail = sessionStorage.getItem("email");
  const [recentOrders, setRecentOrders] = useState([]);

  useEffect(() => {
    axios
      .get(
        `http://192.168.137.1:2030/api/Orders/vendor-recent-orders/${vendorEmail}`
      )
      .then((res) => {
        console.log(res.data);
        setRecentOrders(res.data);
      })
      .catch((err) => {});
  }, []);

  return (
    <div className="content">
      <div className="dashboard">
        <MenuBar />
        <div className="dashboard--content">
          <Content />
          <Card />

          {/* Adding the Table below the cards */}
          <div className="recent-orders">
            <h3>Recent Orders</h3>
            {recentOrders.length > 0 ? (
              <table className="orders-table">
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Status</th>
                    {/* <th>Quantity</th> */}
                    <th>Unit Price</th>
                  </tr>
                </thead>
                <tbody>
                  {recentOrders.map((order) => (
                    <tr>
                      <td>{order.productName}</td>
                      <td>
                        {order.status === "Processing" ? (
                          <span className="pending">{order.status}</span>
                        ) : order.status === "Delivered" ? (
                          <span className="delivered">{order.status}</span>
                        ) : order.status === "Completed" ? (
                          <span className="delivered">{order.status}</span>
                        ) : order.status === "Cancelled" ? (
                          <span className="cancelled">{order.status}</span>
                        ) : order.status === "Requested to cancel" ? (
                          <span className="cancelled">{order.status}</span>
                        ) : (
                          <></>
                        )}
                      </td>
                      {/* <td>{order.productQuantity}</td> */}
                      <td>Rs. {order.productUnitPrice.toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <center>No Recent Orders</center>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default VendorDashboard;
