import React from "react";
import { BiNotification } from 'react-icons/bi';
import './DashboardContent.css'

const DashboardContent = () => {

    return (

        <div className="content--header">
            <h1 className="header-title">Dashboard</h1>
            <div className="header--activity">
                <div className="profile">
                </div>
                <div className="notify">
                    <BiNotification />
                </div>
            </div>
            </div>

    );
};
export default DashboardContent;