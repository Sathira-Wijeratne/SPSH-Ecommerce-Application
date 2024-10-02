import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./ProductCategories.css"; // CSS file following the same design

const ProductCategory = () => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState("");

  useEffect(() => {
    // Fetch existing categories from the API
    axios
      .get("http://your-api-url.com/api/product-categories")
      .then((res) => {
        setCategories(res.data);
      })
      .catch((err) => {
        console.error("Failed to fetch categories", err);
      });
  }, []);

  // Function to handle adding a new category
  const handleAddCategory = (e) => {
    e.preventDefault();
    axios
      .post("http://your-api-url.com/api/product-categories", { name: newCategory, status: "Active" })
      .then((res) => {
        setCategories([...categories, res.data]);
        setNewCategory(""); // Reset input field
      })
      .catch((err) => {
        console.error("Failed to add category", err);
      });
  };

  // Function to toggle active/inactive status
  const toggleCategoryStatus = (id, currentStatus) => {
    const updatedStatus = currentStatus === "Active" ? "Inactive" : "Active";
    axios
      .put(`http://your-api-url.com/api/product-categories/${id}`, { status: updatedStatus })
      .then(() => {
        setCategories(categories.map((category) =>
          category.id === id ? { ...category, status: updatedStatus } : category
        ));
      })
      .catch((err) => {
        console.error("Failed to update category status", err);
      });
  };

  return (
    <div className="category-container">
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
              <td>{category.name}</td>
              <td>{category.status}</td>
              <td>
                {category.status === "Active" ? (
                  <FaToggleOn
                    className="toggle-icon"
                    onClick={() => toggleCategoryStatus(category.id, category.status)}
                  />
                ) : (
                  <FaToggleOff
                    className="toggle-icon"
                    onClick={() => toggleCategoryStatus(category.id, category.status)}
                  />
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="add-category-form">
        <h2>Add New Category</h2>
        <form onSubmit={handleAddCategory}>
          <input
            type="text"
            value={newCategory}
            onChange={(e) => setNewCategory(e.target.value)}
            placeholder="Enter category name"
            required
          />
          <button type="submit">Add Category</button>
        </form>
      </div>
    </div>
  );
};

export default ProductCategory;
