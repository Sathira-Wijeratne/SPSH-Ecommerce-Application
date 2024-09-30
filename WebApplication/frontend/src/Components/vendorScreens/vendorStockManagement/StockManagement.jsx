import React, { useEffect, useState } from "react";
import MenuBar from "../vendorMenuBar/VendorMenuBar";
import "./StockManagement.css"; // Import the CSS
import axios from "axios";

const StockManagement = () => {
  const [stock, setStock] = useState([]);
  const vendorEmail = sessionStorage.getItem("email");

  useEffect(() => {
    axios
      .get(
        `http://192.168.137.1:2030/api/Products/stocks-vendor/${vendorEmail}`
      )
      .then((res) => {
        console.log(res.data);
        setStock(res.data);
      });
  }, []);

  // Handle removing a product from stock (example)
  const handleRemoveStock = (productId) => {
    // Remove stock logic
    const updatedStock = stock.filter((item) => item.ProductId !== productId);
    setStock(updatedStock);
  };

  return (
    <div className="stock-management">
      <MenuBar /> {/* Sidebar Menu */}
      <div className="stock-management--content">
        <h1>Manage Stock</h1>

        <table className="stock-table">
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Product Name</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {stock.map((product) => (
              <tr key={product.ProductId}>
                <td>{product.productId}</td>
                <td>{product.name}</td>
                <td>
                  {product.stock < 10 ? (
                    <span className="low-stock">{product.stock}</span>
                  ) : (
                    <span>{product.stock}</span>
                  )}
                </td>

                <td>
                  <button
                    className="remove-btn"
                    onClick={() => handleRemoveStock(product.productId)}
                  >
                    Remove Stock
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default StockManagement;
