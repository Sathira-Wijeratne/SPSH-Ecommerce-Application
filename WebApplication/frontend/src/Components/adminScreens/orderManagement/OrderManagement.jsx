import React, { useEffect, useState } from "react";
import AdminMenuBar from "../adminDashboard/menuBar/MenuBar";
import "./OrderManagement.css"; // Import CSS for this screen
import axios from "axios";
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from "@mui/material";
import { styled } from "@mui/material/styles"; // For custom styles

const StyledDialog = styled(Dialog)(({ theme }) => ({
  "& .MuiDialog-paper": {
    backgroundColor: "#ADCBE3", // Navy blue background for dialog
    color: "#002147", // White text color
  },
}));

const StyledButton = styled(Button)(({ theme }) => ({
  backgroundColor: "#ffffff", // White background
  color: "#002147", // Navy blue text color
  "&:hover": {
    backgroundColor: "#cccccc", // Light gray on hover
  },
}));

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [dialogType, setDialogType] = useState(""); // To differentiate between Cancel or Complete

  useEffect(() => {
    axios.get("http://192.168.137.1:2030/api/Orders").then((res) => {
      console.log(res.data);
      setOrders(res.data);
    });
  }, []);

  const handleDialogOpen = (order, type) => {
    setSelectedOrder(order);
    setDialogType(type);
    setOpenDialog(true);
  };

  const handleDialogClose = () => {
    setOpenDialog(false);
    setSelectedOrder(null);
    setDialogType("");
  };

  // Function to handle order completion
  const handleCompleteOrder = () => {
    const order = selectedOrder;
    if (order.status === "Processing") {
      axios
        .get(`http://192.168.137.1:2030/api/Products/${order.productId}`)
        .then((res1) => {
          if (order.productQuantity <= res1.data.stock) {
            const updatedProduct = {
              ...res1.data,
              stock: res1.data.stock - order.productQuantity,
            };
            axios
              .put(`http://192.168.137.1:2030/api/Products/${res1.data.productId}`, updatedProduct)
              .then((res2) => {
                if (res2.status === 200) {
                  axios
                    .patch(`http://192.168.137.1:2030/api/Orders/manage/${order.orderId}?status=Completed`)
                    .then((res) => {
                      if (res.status === 200) {
                        window.location.reload();
                      } else {
                        alert("Network error!");
                      }
                    })
                    .catch((err) => {
                      alert(err);
                    });
                }
              });
          } else {
            alert("Insufficient Stocks!");
          }
        });
    } else {
      axios
        .patch(`http://192.168.137.1:2030/api/Orders/manage/${order.orderId}?status=Completed`)
        .then((res) => {
          if (res.status === 200) {
            window.location.reload();
          } else {
            alert("Network error!");
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
    handleDialogClose();
  };

  // Function to handle order cancellation
  const handleCancelOrder = () => {
    const orderId = selectedOrder.orderId;
    axios
      .patch(`http://192.168.137.1:2030/api/Orders/manage/${orderId}?status=Cancelled`)
      .then((res) => {
        if (res.status === 200) {
          window.location.reload();
        } else {
          alert("Network error!");
        }
      })
      .catch((err) => {
        alert(err);
      });
    handleDialogClose();
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
              <th>Customer Note</th>
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
                <td>{order.note}</td>
                <td>
                  {order.status === "Processing" || order.status === "Delivered" ? (
                    <button
                      className="deliver-btn"
                      onClick={() => handleDialogOpen(order, "complete")}
                    >
                      Mark as Completed
                    </button>
                  ) : order.status === "Requested to cancel" ? (
                    <button
                      className="cancel-btn"
                      onClick={() => handleDialogOpen(order, "cancel")}
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
      </div>

      {/* Styled Confirmation Dialog */}
      <StyledDialog open={openDialog} onClose={handleDialogClose}>
        <DialogTitle>{dialogType === "cancel" ? "Cancel Order" : "Complete Order"}</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {dialogType === "cancel"
              ? `Customer Cancellation Note: ${selectedOrder?.note}`
              : `Are you sure you want to complete order ${selectedOrder?.orderId}?`}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <StyledButton onClick={handleDialogClose} color="primary">
            No
          </StyledButton>
          <StyledButton
            onClick={dialogType === "cancel" ? handleCancelOrder : handleCompleteOrder}
            color="primary"
            autoFocus
          >
            Yes
          </StyledButton>
        </DialogActions>
      </StyledDialog>
    </div>
  );
};

export default OrderManagement;
