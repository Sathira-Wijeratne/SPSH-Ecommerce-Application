import React from "react";
import { HiShoppingCart } from "react-icons/hi2";

import './MenuBar.css';

const MenuBar = () => {

    return (

        <div className="menu">
            <div className="logo">
                <HiShoppingCart className="logo-icon" />
                    <h2>SPSH</h2>
            </div>
            <div className="menulist">

                <a href="#" className="item">Dashboard</a>

                <a href="#" className="item">User Management</a>

                <a href="#" className="item">Vendor Management</a>

                <a href="#" className="item">Product Management</a>

                <a href="#" className="item">Order Management</a>

                <a href="#" className="item">CSR Management</a>
            </div>
        </div>


    )
}
export default MenuBar;