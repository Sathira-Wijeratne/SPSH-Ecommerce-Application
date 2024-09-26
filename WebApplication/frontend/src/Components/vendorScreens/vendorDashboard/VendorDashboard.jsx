import React from "react";
import MenuBar from '../vendorMenuBar/VendorMenuBar'; 
import Content from './VendorDashboardContent';
import Card from './VendorDashbordCard';
import './VendorDashboard.css';

const VendorDashboard = () => {
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
            <table className="orders-table">
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Status</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>boAt 220 Wireless</td>
                  <td><span className="pending">Pending</span></td>
                  <td>₹1,399</td>
                </tr>
                <tr>
                  <td>Fitbit Inspire</td>
                  <td><span className="delivered">Delivered</span></td>
                  <td>₹4,499</td>
                </tr>
                <tr>
                  <td>Solid Men Polo Neck Blue T-Shirt</td>
                  <td><span className="delivered">Delivered</span></td>
                  <td>₹1,174</td>
                </tr>
                <tr>
                  <td>Fitbit Inspire</td>
                  <td><span className="pending">Pending</span></td>
                  <td>₹1,399</td>
                </tr>
                <tr>
                  <td>Mirrored Aviator Sunglasses</td>
                  <td><span className="delivered">Delivered</span></td>
                  <td>₹6,831</td>
                </tr>
              </tbody>
            </table>
          </div>
          
        </div>
      </div>
    </div>
  );
};

export default VendorDashboard;
