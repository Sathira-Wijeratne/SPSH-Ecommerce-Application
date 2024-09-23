import React from "react";
import MenuBar from './menuBar/MenuBar';
import Content from './dashboardContent/DashboardContent';
import './AdminDashboardScreen.css';

const AdminDashboard = () => {

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
export default AdminDashboard;