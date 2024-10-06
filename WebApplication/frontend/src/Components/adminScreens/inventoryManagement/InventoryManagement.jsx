import React, { useEffect, useState } from "react";
import MenuBar from "../vendorMenuBar/VendorMenuBar";
import "./InventoryManagement.css"; // Import the CSS
import axios from "axios";
import { Button, Table } from 'react-bootstrap'; // Import Bootstrap components

const InventoryManagement = () => {
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

  const handleRemoveStock = (productId) => {
    var userRes = window.confirm(
      `Are you sure you want to remove all the stocks of product ${productId}?`
    );
    if (userRes === true) {
      axios
        .get(
          `http://192.168.137.1:2030/api/Orders/get-by-status-prodid/Processing/${productId}`
        )
        .then((res) => {
          if (res.data.length > 0) {
            alert("Cannot remove stocks due to some processing orders!");
          } else {
            axios
              .delete(`http://192.168.137.1:2030/api/Products/${productId}`)
              .then((res) => {
                if (res.status === 200) {
                  alert("Stocks Removed!");
                  window.location.reload();
                }
              })
              .catch((err) => {
                alert(err);
              });
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
  };

  return (
    <div className="stock-management">
      <MenuBar /> {/* Sidebar Menu */}
      <div className="stock-management--content">
        <h1 className="header-title">Manage Stock</h1>

        <Table striped bordered hover className="stock-table"> {/* Bootstrap Table */}
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
                  <Button
                    variant="danger" // Bootstrap variant for red button
                    onClick={() => handleRemoveStock(product.productId)}
                    className="remove-btn"
                  >
                    Remove Stock
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </div>
  );
};

export default InventoryManagement;
