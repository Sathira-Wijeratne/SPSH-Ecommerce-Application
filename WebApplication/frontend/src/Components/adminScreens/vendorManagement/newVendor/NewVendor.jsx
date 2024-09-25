import React, { useState } from 'react';
import './NewVendor.css';
import MenuBar from '..../adminDashboard/menuBar/MenuBar';

const NewVendor = () => {
    const [formData, setFormData] = useState({
        name: '',
        role: '',
        email: '',
        password: '',
        activated: false,
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === 'checkbox' ? checked : value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(formData);
        // Add form submission logic here (e.g., API call)
    };

    return (
        <div className="form-container">
            <h2>Create New User</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Activated:</label>
                    <input
                        type="checkbox"
                        name="activated"
                        checked={formData.activated}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit" className="submit-btn">Submit</button>
            </form>
            <MenuBar/>
        </div>
    );
};

export default NewVendor;

























