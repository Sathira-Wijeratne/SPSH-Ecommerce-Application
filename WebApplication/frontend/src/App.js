import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import "./index.css";
import 'bootstrap/dist/css/bootstrap.min.css';

//Admin Screens
import Login from "./Components/loginScreen/Login";
import AdminDashbord from "./Components/adminScreens/adminDashboard/AdminDashboardScreen";
import EditUser from "./Components/adminScreens/userManagement/AllUserInformation";
import NewVendor from "./Components/adminScreens/vendorManagement/newVendor/NewVendor";
import ExisitingVendor from "./Components/adminScreens/vendorManagement/existingVendor/AllVendors";
import OrderManagement from "./Components/adminScreens/orderManagement/OrderManagement";
import ProductCategory from "./Components/adminScreens/productManagement/ProductCategories";
import UserDetails from "./Components/adminScreens/userManagement/EditViewuser";
import NotificationPage from "./Components/adminScreens/notificationManagement/NotificationManagement";




// Vendor Screens
import VendorDashboard from "./Components/vendorScreens/vendorDashboard/VendorDashboard";
import AddProducts from "./Components/vendorScreens/addProducts/addProductsDashboard/AddProducts";
import ShipmentManagement from "./Components/vendorScreens/shipmentManagement/ShipmentDashboard";
import StockManagement from "./Components/vendorScreens/vendorStockManagement/StockManagement";
import InventoryManagement from "./Components/adminScreens/inventoryManagement/InventoryManagement";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />

        {/* Admin Management  */}
        <Route path="/Admin/AdminDashboard" element={<AdminDashbord />} />

        <Route
          path="/Admin/UserManagement/EditUser/Dashboard"
          element={<EditUser />}
        />
        <Route
          path="/Admin/VendorManagement/NewVendor"
          element={<NewVendor />}
        />
        <Route
          path="/Admin/VendorManagement/AllVendor"
          element={<ExisitingVendor />}
        />
        <Route path="/Admin/OrderManagement" element={<OrderManagement />} />
        <Route path="/Admin/ProductManagement" element={<ProductCategory />} />
        <Route path="/Admin/UserManagement/ViewUser/:email" element={<UserDetails />}/>
        <Route path="/Admin/InventoryManagement" element = {<InventoryManagement/>}/>
        <Route path="/Admin/NotificationPannel" element = {<NotificationPage/>}/>
        


        {/* Vendor Management  */}
        <Route path="/Vendor/VendorDashboard" element={<VendorDashboard />} />
        <Route path="/Vendor/AddNewProducts" element={<AddProducts />} />
        <Route
          path="/Vendor/OrderManagement"
          element={<ShipmentManagement />}
        />
        <Route path="/Vendor/StockManagement" element={<StockManagement />} />

        {/* CSR Management  */}
        <Route path="/CSR/CSRDashboard" element={<OrderManagement />} />
      </Routes>
    </Router>
  );
}

export default App;
