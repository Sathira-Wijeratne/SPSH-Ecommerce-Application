import React from "react";
import MenuBar from '../vendorMenuBar/VendorMenuBar'; 
import Content from './VendorDashboardContent';
import Card from './VendorDashbordCard'
import './VendorDashboard.css';



const VendorDashboard = () => {

    return (
        <div className="content">
            <div className="dashboard">
                <MenuBar />
                <div className="dashboard--content">
                    <Content />
                    <Card/>
                    
                    
                </div>
                
            </div>
        </div>

    )
}
export default VendorDashboard;