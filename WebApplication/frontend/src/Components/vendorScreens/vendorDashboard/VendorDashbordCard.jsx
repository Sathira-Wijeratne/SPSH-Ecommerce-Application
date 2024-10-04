import axios from "axios";
import React, { useEffect, useState } from "react";
import { FaTruck, FaSpinner, FaDollarSign } from "react-icons/fa"; // You can use these icons or choose others

const VendorDashboardCard = () => {
  // States to hold order and sales data
  const [pendingOrders, setPendingOrders] = useState(0);
  const [deliveredStocks, setDeliveredStocks] = useState(0);
  const [completedStocks, setCompletedStocks] = useState(0);
  const [deliveredSales, setDeliveredSales] = useState(0);
  const [completedSales, setCompletedSales] = useState(0);

  const vendorEmail = sessionStorage.getItem("email");

  useEffect(() => {
    axios
      .get(
        `http://192.168.137.1:2030/api/Orders/get-by-vendor-status/${vendorEmail}/Delivered`
      )
      .then((res) => {
        var soldCount = 0;
        var sales = 0;
        for (var i = 0; i < res.data.length; i++) {
          soldCount = soldCount + res.data[i].productQuantity;
          sales =
            sales + res.data[i].productUnitPrice * res.data[i].productQuantity;
        }
        setDeliveredStocks(soldCount);
        setDeliveredSales(sales);
      });

    axios
      .get(
        `http://192.168.137.1:2030/api/Orders/get-by-vendor-status/${vendorEmail}/Completed`
      )
      .then((res) => {
        var soldCount = 0;
        var sales = 0;
        for (var i = 0; i < res.data.length; i++) {
          soldCount = soldCount + res.data[i].productQuantity;
          sales =
            sales + res.data[i].productUnitPrice * res.data[i].productQuantity;
        }
        setCompletedStocks(soldCount);
        setCompletedSales(sales);
      });

    axios
      .get(
        `http://192.168.137.1:2030/api/Orders/get-by-vendor-status/${vendorEmail}/Processing`
      )
      .then((res) => {
        setPendingOrders(res.data.length);
      });
  }, []);

  return (
    <div className="card-container">
      <div className="card">
        <div className="card-cover">
          <FaTruck />
        </div>
        <div className="card-title">
          <h2>Sold Stocks</h2>
          <p>{deliveredStocks + completedStocks}</p>
        </div>
      </div>
      <div className="card">
        <div className="card-cover">
          <FaSpinner />
        </div>
        <div className="card-title">
          <h2>Order Pending</h2>
          <p>{pendingOrders}</p>
        </div>
      </div>
      <div className="card">
        <div className="card-cover">
          <FaDollarSign />
        </div>
        <div className="card-title">
          <h2>Total Sales</h2>
          <p>Rs. {deliveredSales + completedSales}</p>
        </div>
      </div>
    </div>
  );
};

export default VendorDashboardCard;
