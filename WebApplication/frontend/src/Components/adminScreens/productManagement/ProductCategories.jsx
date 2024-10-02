import React, { useState } from "react";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./ProductCategories.css"; // Import the updated CSS
import MenuBar from "../adminDashboard/menuBar/MenuBar";

const ProductCategory = () => {
  const [categories, setCategories] = useState([
    { id: "1", name: "Laptops", status: "Active" },
    { id: "2", name: "Phones", status: "Inactive" },
    { id: "3", name: "Tablets", status: "Active" },
  ]);

  const [newCategory, setNewCategory] = useState("");

  const handleAddCategory = (e) => {
    e.preventDefault();
    const newCat = {
      id: (categories.length + 1).toString(),
      name: newCategory,
      status: "Active",
    };
    setCategories([...categories, newCat]);
    setNewCategory("");
  };

  const toggleCategoryStatus = (id, currentStatus) => {
    const updatedStatus = currentStatus === "Active" ? "Inactive" : "Active";
    setCategories(
      categories.map((category) =>
        category.id === id ? { ...category, status: updatedStatus } : category
      )
    );
  };

  return (
    <div className="category-container">
      <MenuBar />

      <div className="add-category-form">
        <h1>Add New Category</h1>
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
                    onClick={() =>
                      toggleCategoryStatus(category.id, category.status)
                    }
                  />
                ) : (
                  <FaToggleOff
                    className="toggle-icon"
                    onClick={() =>
                      toggleCategoryStatus(category.id, category.status)
                    }
                  />
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductCategory;
