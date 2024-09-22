import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import './index.css';
import Login from './Components/loginScreen/Login';
import AdminDashbord from './Components/adminScreens/AdminDashboardScreen';


function App() {
  return(
    <Router>
      <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/Admin/AdminDashboard" element={<AdminDashbord />} />
      </Routes>
    </Router>
  );
  
}

export default App;
