import React, { useState } from "react";
import MenuBar from '../../vendorMenuBar/VendorMenuBar';
import './AddProducts.css';

const AddProducts = () => {
    const [product, setProduct] = useState({
        name: '',
        description: '',
        category: 'Phones', // default category
        price: '',
        stock: '',
        image: '',
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setProduct({
            ...product,
            [name]: value
        });
    };

    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
        reader.onloadend = () => {
            setProduct({ ...product, image: reader.result });
        };
        reader.readAsDataURL(file);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Here you can send the product object to the backend or save it in MongoDB
        console.log(product);
    };

    return (
        <div className="add-product-container">
            <MenuBar />
            <div className="add-product-content">
                <h2>Add New Product</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Product Name</label>
                        <input 
                            type="text" 
                            name="name" 
                            value={product.name} 
                            onChange={handleInputChange} 
                            placeholder="Enter product name" 
                        />
                    </div>

                    <div className="form-group">
                        <label>Description</label>
                        <textarea
                            name="description"
                            value={product.description}
                            onChange={handleInputChange}
                            placeholder="Enter product description"
                        />
                    </div>

                    <div className="form-group">
                        <label>Category</label>
                        <select name="category" value={product.category} onChange={handleInputChange}>
                            <option value="Phones">Phones</option>
                            <option value="Laptops">Laptops</option>
                            <option value="Speakers">Speakers</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Price</label>
                        <input 
                            type="number" 
                            name="price" 
                            value={product.price} 
                            onChange={handleInputChange} 
                            placeholder="Enter product price" 
                        />
                    </div>

                    <div className="form-group">
                        <label>Stock</label>
                        <input 
                            type="number" 
                            name="stock" 
                            value={product.stock} 
                            onChange={handleInputChange} 
                            placeholder="Enter product stock" 
                        />
                    </div>

                    <div className="form-group">
                        <label>Product Image</label>
                        <input type="file" onChange={handleImageUpload} />
                        {product.image && <img src={product.image} alt="Product Preview" className="image-preview" />}
                    </div>

                    <button type="submit" className="btn-submit">Add Product</button>
                </form>
            </div>
        </div>
    );
};

export default AddProducts;
