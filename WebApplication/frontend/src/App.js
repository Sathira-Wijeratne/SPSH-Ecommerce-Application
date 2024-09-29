import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import "./index.css";
import Login from "./Components/loginScreen/Login";
import AdminDashbord from "./Components/adminScreens/adminDashboard/AdminDashboardScreen";
import NewUser from "./Components/adminScreens/userManagement/NewUser";
import EditUser from "./Components/adminScreens/userManagement/editUser/AllUserInformation";
import NewVendor from "./Components/adminScreens/vendorManagement/newVendor/NewVendor";
import ExisitingVendor from "./Components/adminScreens/vendorManagement/existingVendor/AllVendors";

// Vendor Management
import VendorDashboard from "./Components/vendorScreens/vendorDashboard/VendorDashboard";
import AddProducts from "./Components/vendorScreens/addProducts/addProductsDashboard/AddProducts";
import ShipmentManagement from "./Components/vendorScreens/shipmentManagement/ShipmentDashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/Admin/AdminDashboard" element={<AdminDashbord />} />
        <Route path="/Admin/UserManagement/NewUser" element={<NewUser/>}/>
        <Route path="/Admin/UserManagement/EditUser/Dashboard" element={<EditUser/>}/>
        <Route path="/Admin/VendorManagement/NewVendor" element={<NewVendor/>}/>
        <Route path="/Admin/VendorManagement/AllVendor" element={<ExisitingVendor/>}/>

        {/* Vendor Management  */}
        <Route path="/Vendor/VendorDashboard" element={<VendorDashboard/>}/>
        <Route path="/Vendor/AddNewProducts" element={<AddProducts/>}/>
        <Route path="/Vendor/OrderManagement" element={<ShipmentManagement/>}/>


      </Routes>
    </Router>
  );
}

export default App;
