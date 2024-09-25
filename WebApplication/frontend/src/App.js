import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import "./index.css";
import Login from "./Components/loginScreen/Login";
import AdminDashbord from "./Components/adminScreens/adminDashboard/AdminDashboardScreen";
import NewUser from "./Components/adminScreens/userManagement/NewUser";
import EditUser from "./Components/adminScreens/userManagement/editUser/AllUserInformation"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/Admin/AdminDashboard" element={<AdminDashbord />} />
        <Route path="/Admin/UserManagement/NewUser" element={<NewUser/>}/>
        <Route path="/Admin/UserManagement/EditUser/Dashboard" element={<EditUser/>}/>
      </Routes>
    </Router>
  );
}

export default App;
