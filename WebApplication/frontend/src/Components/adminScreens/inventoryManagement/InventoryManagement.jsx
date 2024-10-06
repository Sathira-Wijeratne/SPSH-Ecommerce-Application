import React, { useEffect, useState } from "react";
import "./InventoryManagement.css"; // Import the CSS
import axios from "axios";
import { Button, Table, FormControl } from "react-bootstrap"; // Import Bootstrap components
import MenuBar from "../adminDashboard/menuBar/MenuBar";

const InventoryManagement = () => {
  const [stock, setStock] = useState([]);
  const [searchTerm, setSearchTerm] = useState(""); // Search term state
  const [showDialog, setShowDialog] = useState(false);  // To toggle the dialog
  const [selectedProductId, setSelectedProductId] = useState(null); // To store selected product ID

  useEffect(() => {
    axios.get(`http://192.168.137.1:2030/api/Products`).then((res) => {
      console.log(res.data);
      setStock(res.data);
    });
  }, []);

  // Function to show dialog and store selected product ID
  const confirmRemoveStock = (productId) => {
    setSelectedProductId(productId);
    setShowDialog(true); // Show the dialog
  };

  // Function to remove stock
  const handleRemoveStock = () => {
    axios
      .get(
        `http://192.168.137.1:2030/api/Orders/get-by-status-prodid/Processing/${selectedProductId}`
      )
      .then((res) => {
        if (res.data.length > 0) {
          alert("Cannot remove stocks due to some processing orders!");
          setShowDialog(false); // Hide dialog if there are processing orders
        } else {
          axios
            .delete(`http://192.168.137.1:2030/api/Products/${selectedProductId}`)
            .then((res) => {
              if (res.status === 200) {
                alert("Stocks Removed!");
                setShowDialog(false); // Hide dialog after deletion
                window.location.reload();
              }
            })
            .catch((err) => {
              alert(err);
              setShowDialog(false); // Hide dialog if there is an error
            });
        }
      })
      .catch((err) => {
        alert(err);
        setShowDialog(false); // Hide dialog if there is an error
      });
  };

  // Function to filter stock based on search term
  const filteredStock = stock.filter(
    (product) =>
      product.productId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="stock-management">
      <MenuBar />
      <div className="stock-management--content">
        <h1 className="hello">Manage Stock</h1>

        {/* Search Bar */}
        <FormControl
          type="text"
          placeholder="Search by Product ID or Name"
          className="search-bar"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        <Table striped bordered hover className="stock-table">
          {" "}
          {/* Bootstrap Table */}
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Product Name</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredStock.map((product) => (
              <tr key={product.productId}>
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
                    onClick={() => confirmRemoveStock(product.productId)} // Show dialog when remove is clicked
                    className="remove-btn"
                  >
                    Remove
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>

        {/* Confirmation Dialog Box */}
        {showDialog && (
          <div className="dialog-overlay">
            <div className="dialog-box">
              <h3>Confirm Deletion</h3>
              <p>
                Are you sure you want to Remove all the stocks of product{" "}
                <strong>{selectedProductId}</strong>?
              </p>
              <div className="dialog-buttons">
                <Button variant="danger" onClick={handleRemoveStock}>
                  Yes, Remove
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => setShowDialog(false)} // Hide dialog when cancel is clicked
                >
                  Cancel
                </Button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default InventoryManagement;
