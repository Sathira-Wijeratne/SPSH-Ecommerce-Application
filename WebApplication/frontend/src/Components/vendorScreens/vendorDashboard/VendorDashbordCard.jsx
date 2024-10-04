import React from 'react';
import { FaTruck, FaSpinner, FaDollarSign } from "react-icons/fa"; // You can use these icons or choose others

// Updated array with new titles and icons
const stockInfo = [
    {
        title: 'Sold Stocks',
        icon: <FaTruck />, // Icon representing delivered stocks
    },
    {
        title: 'Order Pending',
        icon: <FaSpinner />, // Icon representing processing stocks
    },
    {
        title: 'Total Sales',
        icon: <FaDollarSign />, // Icon representing total sales
    },
];

const VendorDashboardCard = () => {
    return (
        <div className="card-container">
            {stockInfo.map((item, index) => (
                <div className="card" key={index}>
                    <div className="card-cover">
                        {item.icon}
                    </div>
                    <div className="card-title">
                        <h2>{item.title}</h2>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default VendorDashboardCard;
