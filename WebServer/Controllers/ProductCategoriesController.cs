/*
 * Description: This file contains the ProductCategoriesController, which is responsible for handling
 * CRUD operations for product categories, including retrieving, creating, updating, deleting,
 * and managing the active/inactive status of product categories.
 */

using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductCategoriesController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        // Constructor to initialize the MongoDB service dependency
        public ProductCategoriesController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        // Retrieves all product categories from the database
        [HttpGet]
        public async Task<ActionResult<List<ProductCategory>>> Get()
        {
            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            var productCategories = await productCategoriesCollection.Find(p => true).ToListAsync();
            return Ok(productCategories);
        }

        // Retrieves a specific product category by its name from the database
        [HttpGet("{CategoryName}")]
        public async Task<ActionResult<ProductCategory>> Get(string CategoryName)
        {
            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            var productCategory = await productCategoriesCollection.Find(p => p.CategoryName == CategoryName).ToListAsync();
            if (productCategory == null)
            {
                return NotFound(new { message = "Product Category not found" });
            }
            return Ok(productCategory);
        }

        // Creates a new product category in the database
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] ProductCategory productCategory)
        {
            if (productCategory == null)
                return BadRequest(new { message = "ProductCategory data is missing" });

            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            await productCategoriesCollection.InsertOneAsync(productCategory);
            return CreatedAtAction(nameof(Get), new { CategoryName = productCategory.Id }, productCategory);
        }

        // Updates the details of an existing product category
        [HttpPut("{CategoryName}")]
        public async Task<IActionResult> Update(string CategoryName, [FromBody] ProductCategory updatedProductCategory)
        {
            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            var existingProductCategory = await productCategoriesCollection.Find(p => p.CategoryName == CategoryName).FirstOrDefaultAsync();

            // Retain the original Id and CategoryName
            updatedProductCategory.Id = existingProductCategory.Id;
            updatedProductCategory.CategoryName = CategoryName;

            var result = await productCategoriesCollection.ReplaceOneAsync(p => p.CategoryName == CategoryName, updatedProductCategory);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Product Category not found" });
            }

            return NoContent();
        }

        // Deletes a product category from the database by its name
        [HttpDelete("{CategoryName}")]
        public async Task<IActionResult> Delete(string CategoryName)
        {
            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            var result = await productCategoriesCollection.DeleteOneAsync(p => p.CategoryName == CategoryName);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Product Category not found" });
            }
            return NoContent();
        }

        // Retrieves only the active product categories from the database
        [HttpGet("active")]
        public async Task<ActionResult<List<ProductCategory>>> GetActiveCategories()
        {
            var productCategoriesCollection = _mongoDBService.GetProductCategoriesCollection();
            var activeCategories = await productCategoriesCollection.Find(c => c.IsActive).ToListAsync();
            return Ok(activeCategories);
    }
    }
}
