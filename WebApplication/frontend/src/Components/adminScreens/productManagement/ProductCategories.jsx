import React, { useState, useEffect } from "react";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./ProductCategories.css"; // CSS file following the same design
import MenuBar from "../adminDashboard/menuBar/MenuBar";

const ProductCategory = () => {
  const [categories, setCategories] = useState([
    // Sample categories for demonstration purposes
    { id: "1", name: "Laptops", status: "Active" },
    { id: "2", name: "Phones", status: "Inactive" },
    { id: "3", name: "Tablets", status: "Active" },
  ]);
  const [newCategory, setNewCategory] = useState("");

  // Function to handle adding a new category
  const handleAddCategory = (e) => {
    e.preventDefault();
    const newCat = {
      id: (categories.length + 1).toString(), // Generate an ID for the new category
      name: newCategory,
      status: "Active",
    };
    setCategories([...categories, newCat]);
    setNewCategory(""); // Reset input field
  };

  // Function to toggle active/inactive status
  const toggleCategoryStatus = (id, currentStatus) => {
    const updatedStatus = currentStatus === "Active" ? "Inactive" : "Active";
    setCategories(categories.map((category) =>
      category.id === id ? { ...category, status: updatedStatus } : category
    ));
  };

  return (
    <div className="category-container">
      <MenuBar/>
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
