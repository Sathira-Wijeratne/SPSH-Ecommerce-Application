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
      .get("http://192.168.137.1:2030/api/Products/last-product")
      .then((res1) => {
        setProductId(
          "P" +
          (Number(res1.data[0].productId.substring(1)) + 1)
            .toString()
            .padStart(3, "0")
        );

        axios
          .get("http://192.168.137.1:2030/api/ProductCategories/active")
          .then((res) => {
            setFirstActiveCategory(res.data[0].categoryName);
            setProductCategory(res.data[0].categoryName);

            const cat = res.data.map(item => item.categoryName);
            setCategories(cat);
          });
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
    if (!isProductExisting) {
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

  const searchExistingItem = (prodID) => {
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
        setIsProductExisting(false);
        setProductCategory(firstActiveCategory);
        setName("");
        setDescription("");
        setPrice("");
        setStock(0);
        setImageBase64("");
      });
  };

  return (
    <div className="content">
      <MenuBar />
      <div className="container mt-5"> {/* Use Bootstrap container */}
        <h2 className="text-center mb-4">Add New Product</h2>
        <form onSubmit={handleSubmit} className="row g-3"> {/* Bootstrap grid for form layout */}
          <div className="col-md-6">
            <label className="form-label">Product ID</label>
            <input
              type="text"
              className="form-control"
              name="productId"
              value={productId}
              onChange={(e) => {
                setProductId(e.target.value);
                searchExistingItem(e.target.value);
              }}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label">Product Name</label>
            <input
              type="text"
              className="form-control"
              name="name"
              value={name}
              onChange={(e) => {
                setName(e.target.value);
              }}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label">Description</label>
            <textarea
              className="form-control"
              name="description"
              value={description}
              onChange={(e) => {
                setDescription(e.target.value);
              }}
            ></textarea>
          </div>

          <div className="col-md-6">
            <label className="form-label">Category</label>
            <select
              className="form-select"
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

          <div className="col-md-6">
            <label className="form-label">Price</label>
            <input
              type="number"
              step={"0.01"}
              className="form-control"
              name="price"
              value={price}
              onChange={(e) => {
                setPrice(Number(e.target.value));
              }}
            />
          </div>

          <div className="col-md-6">
            <label className="form-label">Stock</label>
            <input
              type="number"
              min={1}
              className="form-control"
              name="stock"
              value={newStock}
              onChange={(e) => {
                setNewStock(Number(e.target.value));
              }}
            />
          </div>

          <div className="col-md-12">
            <label className="form-label">Product Image</label>
            <input
              type="file"
              className="form-control"
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
                className="image-preview mt-3"
              />
            )}
          </div>

          <div className="col-12">
            <button
              type="submit"
              className="btn btn-primary w-100"
              style={{ backgroundColor: "#001f3f", borderColor: "#001f3f" }}
            >
              Add Product
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddProducts;
