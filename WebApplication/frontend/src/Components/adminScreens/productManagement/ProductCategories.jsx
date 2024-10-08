import React, { useEffect, useState } from "react";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./ProductCategories.css"; // Import the updated CSS
import MenuBar from "../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const ProductCategory = () => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState("");

  useEffect(() => {
    axios.get("http://192.168.137.1:2030/api/ProductCategories").then((res) => {
      console.log(res.data);
      setCategories(res.data);
    });
  }, []);

  const handleAddCategory = (e) => {
    e.preventDefault();
    axios
      .post("http://192.168.137.1:2030/api/ProductCategories", {
        id: "",
        categoryName: newCategory,
        isActive: true,
      })
      .then((res) => {
        if (res.status === 201) {
          alert("Product Category Added!");
          window.location.reload();
        }
      })
      .catch((err) => {
        alert(err);
      });
  };

  const toggleCategoryStatus = (id, categoryName, newStatus) => {
    axios
      .put(`http://192.168.137.1:2030/api/ProductCategories/${categoryName}`, {
        id,
        categoryName,
        isActive: newStatus,
      })
      .then((res) => {
        if (res.status === 200) {
          window.location.reload();
        }
      });
  };

  return (
    <div>
      <MenuBar />
      <div className="category-container">
        <div className="add-category-form">
          <h1 className="hello">Add New Category</h1>
          <form onSubmit={handleAddCategory}>
            <input
              type="text"
              value={newCategory}
              onChange={(e) => setNewCategory(e.target.value)}
              placeholder="Enter category name"
              required
              style={{ marginBottom: "20px" }}
            />
           
            <button type="submit">Add Category</button>
          </form>
        </div>

        <h1 className="category-title">Product Categories</h1>
        <table className="category-table">
          <thead>
            <tr>
              <th>Category Name</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => (
              <tr key={category.id}>
                <td>{category.categoryName}</td>
                {category.isActive === true ? <td>Active</td> : <td>Inactive</td>}
                <td>
                  {category.isActive === true ? (
                    <FaToggleOn
                      className="toggle-icon"
                      onClick={() =>
                        toggleCategoryStatus(
                          category.id,
                          category.categoryName,
                          false
                        )
                      }
                    />
                  ) : (
                    <FaToggleOff
                      className="toggle-icon"
                      onClick={() =>
                        toggleCategoryStatus(
                          category.id,
                          category.categoryName,
                          true
                        )
                      }
                    />
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ProductCategory;
