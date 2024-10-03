import React, { useEffect, useState } from "react";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./ProductCategories.css"; // Import the updated CSS
import MenuBar from "../adminDashboard/menuBar/MenuBar";
import axios from "axios";

const ProductCategory = () => {
  useEffect(() => {
    axios.get("http://192.168.137.1:2030/api/ProductCategories").then((res) => {
      console.log(res.data);
      setCategories(res.data);
    });
  }, []);

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
  );
};

export default ProductCategory;
