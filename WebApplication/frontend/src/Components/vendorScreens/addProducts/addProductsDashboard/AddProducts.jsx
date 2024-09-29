import React, { useEffect, useState } from "react";
import MenuBar from "../../vendorMenuBar/VendorMenuBar";
import "./AddProducts.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const AddProducts = () => {
  const navigate = useNavigate();
  const [product, setProduct] = useState({
    id: "",
    productId: "",
    productCategory: "",
    vendorEmail: sessionStorage.getItem("email"),
    name: "",
    description: "",
    price: "",
    stock: "",
    imageBase64: "",
  });

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    axios
      .get("http://192.168.137.1:2030/api/ProductCategories/active")
      .then((res) => {
        // default category
        setProduct({
          ...product,
          productCategory: res.data[0].categoryName,
        });
        var cat = [];
        for (var i = 0; i < res.data.length; i++) {
          cat.push(res.data[i].categoryName);
          setCategories(cat);
        }
      });
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct({
      ...product,
      [name]: value,
    });
  };

  const handleNumberInputChange = (e) => {
    const { name, value } = e.target;
    setProduct({
      ...product,
      [name]: Number(value),
    });
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    const reader = new FileReader();
    reader.onloadend = () => {
      setProduct({ ...product, imageBase64: reader.result });
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://192.168.137.1:2030/api/Products", product)
      .then((res) => {
        if (res.status === 201) {
          alert("Product Added Successfully!");
          navigate("/Vendor/VendorDashboard");
        }
      })
      .catch((err) => {
        if (err.status === 409) {
          alert("Product ID exists. Please enter another Product ID!");
        } else {
          alert(err);
        }
      });
  };

  return (
    <div className="content">
      <MenuBar />
      <div className="add-product-content">
        <h2 className="title">Add New Product</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Product ID</label>
            <input type="text" name="productId" onChange={handleInputChange} />
          </div>

          <div className="form-group">
            <label>Product Name</label>
            <input
              type="text"
              name="name"
              value={product.name}
              onChange={handleInputChange}
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              name="description"
              value={product.description}
              onChange={handleInputChange}
            ></textarea>
          </div>

          <div className="form-group">
            <label>Category</label>
            <select
              name="productCategory"
              value={product.productCategory}
              onChange={handleInputChange}
            >
              {categories.map((category, index) => (
                <option key={index} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Price</label>
            <input
              type="number"
              step={"0.01"}
              name="price"
              value={product.price}
              onChange={handleNumberInputChange}
            />
          </div>

          <div className="form-group">
            <label>Stock</label>
            <input
              type="number"
              min={1}
              name="stock"
              value={product.stock}
              onChange={handleNumberInputChange}
            />
          </div>

          <div className="form-group">
            <label>Product Image</label>
            <input type="file" onChange={handleImageUpload} />
            {product.imageBase64 && (
              <img
                src={product.imageBase64}
                alt="Product Preview"
                className="image-preview"
              />
            )}
          </div>

          <button type="submit" className="btn-submit">
            Add Product
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddProducts;
