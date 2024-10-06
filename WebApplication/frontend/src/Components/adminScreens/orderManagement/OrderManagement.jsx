import React, { useEffect, useState } from "react";
import AdminMenuBar from "../adminDashboard/menuBar/MenuBar";
import "./OrderManagement.css"; // Import CSS for this screen
import axios from "axios";

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [selectedOrderID, setSelectedOrderID] = useState(""); // Selected Order ID state
  const [showDialog, setShowDialog] = useState(false); // Show cancel dialog state
  const [showCompleteDialog, setShowCompleteDialog] = useState(false); // Show complete dialog state
  const [cancelReason, setCancelReason] = useState(""); // Cancel reason state
  const [customReason, setCustomReason] = useState(""); // Custom cancel reason state

  const predefinedReasons = [
    "Customer asked to cancel",
    "Stock not available",
    "Incorrect order placed",
    "Other", // Add "Other" as an option
  ];

  useEffect(() => {
    axios.get("http://192.168.137.1:2030/api/Orders").then((res) => {
      console.log(res.data);
      setOrders(res.data);
    });
  }, []);

  // Open the cancel confirmation dialog
  const openConfirmCancelDialog = (orderId) => {
    setSelectedOrderID(orderId);
    setShowDialog(true);
  };

  // Open the completion confirmation dialog
  const openConfirmCompleteDialog = (orderId) => {
    setSelectedOrderID(orderId);
    setShowCompleteDialog(true);
  };

  // Function to handle cancel submission
  const handleCancelSubmit = () => {
    const reason = cancelReason === "Other" ? customReason : cancelReason; // Use custom reason if "Other" is selected
    if (!reason) {
      alert("Please provide a reason for cancellation.");
      return;
    }
    axios
      .patch(
        `http://192.168.137.1:2030/api/Orders/manage/${selectedOrderID}?status=Cancelled&reason=${reason}`
      )
      .then((res) => {
        if (res.status === 200) {
          setShowDialog(false); // Close the dialog
          window.location.reload(); // Reload to reflect the changes
        } else {
          alert("Network error!");
        }
      })
      .catch((err) => {
        alert(err);
      });
  };

  // Function to handle order completion
  const handleCompleteSubmit = () => {
    axios
      .patch(
        `http://192.168.137.1:2030/api/Orders/manage/${selectedOrderID}?status=Completed`
      )
      .then((res) => {
        if (res.status === 200) {
          setShowCompleteDialog(false); // Close the dialog
          window.location.reload(); // Reload to reflect the changes
        } else {
          alert("Network error!");
        }
      })
      .catch((err) => {
        alert(err);
      });
  };

  return (
    <div className="order-management">
      <AdminMenuBar />
      <div className="order-management--content">
        <h1 className="header">Manage Orders</h1>
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
                  {order.status === "Processing" || order.status === "Delivered" ? (
                    <button
                      className="deliver-btn"
                      onClick={() => openConfirmCompleteDialog(order.orderId)}
                    >
                      Completed
                    </button>
                  ) : order.status === "Requested to cancel" ? (
                    <button
                      className="cancel-btn"
                      onClick={() => openConfirmCancelDialog(order.orderId)}
                    >
                      Cancel
                    </button>
                  ) : (
                    <span>-</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* Cancel Confirmation Dialog Box */}
        {showDialog && (
          <div className="dialog-overlay">
            <div className="dialog-box">
              <h3>Cancel Order {selectedOrderID}</h3>
              <p>Please Provide Cancelation Notes:</p>
              <select
                onChange={(e) => setCancelReason(e.target.value)}
                value={cancelReason}
              >
                <option value="">Select a reason</option>
                {predefinedReasons.map((reason, index) => (
                  <option key={index} value={reason}>
                    {reason}
                  </option>
                ))}
              </select>

              {/* Conditionally render custom reason field if "Other" is selected */}
              {cancelReason === "Other" && (
                <textarea
                  rows="3"
                  placeholder="Type your custom reason here..."
                  onChange={(e) => setCustomReason(e.target.value)}
                />
              )}

              <div className="dialog-buttons">
                <button className="confirm-btn" onClick={handleCancelSubmit}>
                  Cancel
                </button>
                <button className="close-btn" onClick={() => setShowDialog(false)}>
                  Back
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Completion Confirmation Dialog Box */}
        {showCompleteDialog && (
          <div className="dialog-overlay">
            <div className="dialog-box">
              <h3>Complete Order {selectedOrderID}</h3>
              <p>Are you sure you want to mark this order as completed?</p>
              <div className="dialog-buttons">
                <button className="confirm-btn" onClick={handleCompleteSubmit}>
                  Yes, Complete
                </button>
                <button className="close-btn" onClick={() => setShowCompleteDialog(false)}>
                  Back
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default OrderManagement;
