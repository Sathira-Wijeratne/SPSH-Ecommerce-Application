import React, { useEffect, useState } from "react";
import MenuBar from "../vendorMenuBar/VendorMenuBar";
import "./StockManagement.css"; // Import the CSS

const StockManagement = () => {
  const [stock, setStock] = useState([]);

  useEffect(() => {
    // Simulate fetching stock data from backend (replace with actual API call)
    const fetchedStock = [
      {
        ProductId: "P001",
        Name: "Galaxy Z Fold 6",
        Price: 578900,
        Stock: 15,
      },
      {
        ProductId: "P002",
        Name: "Asus Zenbook 14x OLED",
        Price: 678900,
        Stock: 8,
      },
    ];
    setStock(fetchedStock);
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
              <th>Product Name</th>
              <th>Stock</th>
              <th>Price</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {stock.map((product) => (
              <tr key={product.ProductId}>
                <td>{product.Name}</td>
                <td>
                  {product.Stock < 10 ? (
                    <span className="low-stock">{product.Stock}</span>
                  ) : (
                    <span>{product.Stock}</span>
                  )}
                </td>
                <td>Rs. {product.Price.toLocaleString()}</td>
                <td>
                  <button
                    className="remove-btn"
                    onClick={() => handleRemoveStock(product.ProductId)}
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
