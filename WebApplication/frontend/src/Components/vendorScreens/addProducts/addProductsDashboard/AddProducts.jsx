import React, { useEffect, useState } from "react";
import MenuBar from "../../vendorMenuBar/VendorMenuBar";
import "./AddProducts.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const AddProducts = () => {
  const navigate = useNavigate();

  const vendorEmail = sessionStorage.getItem("email");

  const [productId, setProductId] = useState("");
  const [productCategory, setProductCategory] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [stock, setStock] = useState(0);
  const [imageBase64, setImageBase64] = useState("");

  const [newStock, setNewStock] = useState(0);

  const [categories, setCategories] = useState([]);
  const [firstActiveCategory, setFirstActiveCategory] = useState("");
  const [isProductExisting, setIsProductExisting] = useState(false);

  useEffect(() => {
    axios
      .get("http://192.168.137.1:2030/api/ProductCategories/active")
      .then((res) => {
        // default category
        setFirstActiveCategory(res.data[0].categoryName);
        setProductCategory(res.data[0].categoryName);

        var cat = [];
        for (var i = 0; i < res.data.length; i++) {
          cat.push(res.data[i].categoryName);
          setCategories(cat);
        }
      });
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    const product = {
      id: "",
      productId,
      productCategory,
      name,
      description,
      price,
      stock: Number(stock) + Number(newStock),
      vendorEmail,
      imageBase64,
    };
    if (isProductExisting === false) {
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
    } else {
      axios
        .put(`http://192.168.137.1:2030/api/Products/${productId}`, product)
        .then((res) => {
          if (res.status === 200) {
            alert("Product Updated Successfully!");
            navigate("/Vendor/VendorDashboard");
          }
        })
        .catch((err) => {
          alert(err);
        });
    }
  };

  function searchExistingItem(prodID) {
    axios
      .get(
        `http://192.168.137.1:2030/api/Products/vendor-prodid/${vendorEmail}/${prodID}`
      )
      .then((res) => {
        setIsProductExisting(true);
        setProductCategory(res.data.productCategory);
        setName(res.data.name);
        setDescription(res.data.description);
        setPrice(res.data.price);
        setStock(res.data.stock);
        setImageBase64(res.data.imageBase64);
      })
      .catch((err) => {
        // 404
        setIsProductExisting(false);
        setProductCategory(firstActiveCategory);
        setName("");
        setDescription("");
        setPrice("");
        setStock(0);
        setImageBase64("");
      });
  }

  return (
    <div className="content">
      <MenuBar />
      <div className="add-product-content">
        <h2 className="title">Add New Product</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Product ID</label>
            <input
              type="text"
              name="productId"
              onChange={(e) => {
                setProductId(e.target.value);
                searchExistingItem(e.target.value);
              }}
            />
          </div>

          <div className="form-group">
            <label>Product Name</label>
            <input
              type="text"
              name="name"
              value={name}
              onChange={(e) => {
                setName(e.target.value);
              }}
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              name="description"
              value={description}
              onChange={(e) => {
                setDescription(e.target.value);
              }}
            ></textarea>
          </div>

          <div className="form-group">
            <label>Category</label>
            <select
              name="productCategory"
              value={productCategory}
              onChange={(e) => {
                setProductCategory(e.target.value);
              }}
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
              value={price}
              onChange={(e) => {
                setPrice(Number(e.target.value));
              }}
            />
          </div>

          <div className="form-group">
            <label>Stock</label>
            <input
              type="number"
              min={1}
              name="stock"
              value={newStock}
              onChange={(e) => {
                setNewStock(Number(e.target.value));
              }}
            />
          </div>

          <div className="form-group">
            <label>Product Image</label>
            <input
              type="file"
              onChange={(e) => {
                const file = e.target.files[0];
                const reader = new FileReader();
                reader.onloadend = () => {
                  setImageBase64(reader.result);
                };
                reader.readAsDataURL(file);
              }}
            />
            {imageBase64 && (
              <img
                src={imageBase64}
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
