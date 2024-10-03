import React, { useState, useEffect } from 'react';
import axios from 'axios'; // Assuming you're fetching data from an API
import { FaClipboardList, FaCheckCircle, FaDollarSign } from "react-icons/fa";
import './Cards.css'; // Assuming there's a CSS file to style the dashboard

const Cards = () => {
  // States to hold order and sales data
  const [pendingOrders, setPendingOrders] = useState(0);
  const [completedOrders, setCompletedOrders] = useState(0);
  const [totalSales, setTotalSales] = useState(0);

  useEffect(() => {
    // Fetch data from an API endpoint for orders and sales
    axios.get('http://your-api-url.com/orders')
      .then((response) => {
        const orders = response.data;

        // Calculate pending, completed orders and total sales
        const pending = orders.filter(order => order.status === 'Pending').length;
        const completed = orders.filter(order => order.status === 'Completed');
        const sales = completed.reduce((total, order) => total + order.amount, 0);

        // Update state with fetched data
        setPendingOrders(pending);
        setCompletedOrders(completed.length);
        setTotalSales(sales);
      })
      .catch((error) => {
        console.error('Error fetching orders data', error);
      });
  }, []);

  return (
    <div className="card-container">
      <div className="card">
        <div className="card-cover">
          <FaClipboardList />
        </div>
        <div className="card-title">
          <h2>Pending Orders</h2>
          <p>{pendingOrders}</p>
        </div>
      </div>

      <div className="card">
        <div className="card-cover">
          <FaCheckCircle />
        </div>
        <div className="card-title">
          <h2>Completed Orders</h2>
          <p>{completedOrders}</p>
        </div>
      </div>

      <div className="card">
        <div className="card-cover">
          <FaDollarSign />
        </div>
        <div className="card-title">
          <h2>Total Sales</h2>
          <p>${totalSales}</p>
        </div>
      </div>
    </div>
  );
}

export default Cards;
