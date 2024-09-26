import React from "react";
import MenuBar from '../vendorMenuBar/VendorMenuBar'; 
import Content from './VendorDashboardContent';
import './VendorDashboard.css';


const VendorDashboard = () => {

    return (
        <div className="content">
            <div className="dashboard">
                <MenuBar />
                <div className="dashboard--content">
                    <Content />
                    
                </div>
                
            </div>
        </div>

    )
}
export default VendorDashboard;