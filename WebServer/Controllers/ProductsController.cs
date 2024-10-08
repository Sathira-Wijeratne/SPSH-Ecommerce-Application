﻿/*******************************************************
 * File:           ProductsController.cs
 * Author:         Wijeratne D.M.S.D & Senadheera P.V.P.P
 * Created:        19.09.2024
 * Description:    This file contains the 
 *                 ProductsController, responsible for 
 *                 handling CRUD operations for products, 
 *                 including retrieving, creating, 
 *                 updating, and deleting products.
 * ****************************************************/

using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        // Constructor to initialize the MongoDB service dependency - Developer Wijeratne D.M.S.D
        public ProductsController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        //General idea for controller methods referenced from - https://code-maze.com/getting-started-aspnetcore-mongodb/

        // Retrieves all products from the database - Developer Wijeratne D.M.S.D
        [HttpGet]
        public async Task<ActionResult<List<Product>>> Get()
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var products = await productsCollection.Find(p => true).ToListAsync();
            return Ok(products);
        }

        // Retrieves a specific product by its ProductId from the database - Developer Wijeratne D.M.S.D
        [HttpGet("{ProductId}")]
        public async Task<ActionResult<Product>> Get(string ProductId)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var product = await productsCollection.Find(p => p.ProductId == ProductId).FirstOrDefaultAsync();
            if (product == null)
            {
                return NotFound(new { message = "Product not found" });
            }
            return Ok(product);
        }

        // Creates a new product in the database - Developer Wijeratne D.M.S.D
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Product product)
        {
            if (product == null)
                return BadRequest(new { message = "Product data is missing" });

            var productsCollection = _mongoDBService.GetProductsCollection();

            var result = await productsCollection.Find(o => o.ProductId == product.ProductId).FirstOrDefaultAsync();
            if (result != null)
            {
                return Conflict(new { message = "Product ID already exists" });
            }

            await productsCollection.InsertOneAsync(product);
            return CreatedAtAction(nameof(Get), new { ProductId = product.Id }, product);
        }

        // Updates an existing product by its ProductId - Developer Wijeratne D.M.S.D
        [HttpPut("{ProductId}")]
        public async Task<IActionResult> Update(string ProductId, [FromBody] Product updatedProduct)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var existingProduct = await productsCollection.Find(p => p.ProductId == ProductId).FirstOrDefaultAsync();

            // Retain the original Id and ProductId
            updatedProduct.Id = existingProduct.Id;
            updatedProduct.ProductId = ProductId;  

            var result = await productsCollection.ReplaceOneAsync(p => p.ProductId == ProductId, updatedProduct);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }

            return Ok(new { message = $"Product {ProductId} has been updated successfully" });
        }

        // Deletes a product from the database by its ProductId - Developer Wijeratne D.M.S.D
        [HttpDelete("{ProductId}")]
        public async Task<IActionResult> Delete(string ProductId)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var result = await productsCollection.DeleteOneAsync(p => p.ProductId == ProductId);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }
            return Ok(new { message = $"Product {ProductId} has been deleted successfully" });
        }

        // route to fetch only the product stocks - Developer Wijeratne D.M.S.D
        [HttpGet("stocks")]
        public async Task<ActionResult<List<object>>> GetStocks()
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var stocks = await productsCollection
                .Find(p=> true).Project(p=>new { p.ProductId, p.Stock }).ToListAsync();

            return Ok(stocks);
        }

        // Retrieves products match with the given name - Developer Senadheera P.V.P.P
        [HttpGet("search-name/{Name}")]
        public async Task<ActionResult<Product>> SearchByName(string Name)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();

            var filter = Builders<Product>.Filter.Regex(p => p.Name, new MongoDB.Bson.BsonRegularExpression(Name, "i"));
            var products = await productsCollection.Find(filter).ToListAsync();

            if (products == null)
            {
                return NotFound(new { message = "Products not found" });
            }
            return Ok(products);
        }

        // Retrieves products match with the given product category - Developer Senadheera P.V.P.P
        [HttpGet("search-category/{ProductCategory}")]
        public async Task<ActionResult<Product>> SearchByCategory(string ProductCategory)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();

            var filter = Builders<Product>.Filter.Regex(p => p.ProductCategory, new MongoDB.Bson.BsonRegularExpression(ProductCategory, "i"));
            var products = await productsCollection.Find(filter).ToListAsync();

            if (products == null)
            {
                return NotFound(new { message = "Products not found" });
            }
            return Ok(products);
        }

        // route to fetch only the product id, name and stocks for a given vendor - Developer Senadheera P.V.P.P
        [HttpGet("stocks-vendor/{VendorEmail}")]
        public async Task<ActionResult<List<object>>> GetStocksByVendor(string VendorEmail)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var stocks = await productsCollection
                .Find(p => p.VendorEmail == VendorEmail).Project(p => new { p.ProductId, p.Name, p.Stock }).SortBy(p => p.Stock).ToListAsync();

            return Ok(stocks);
        }

        // Retrieves a specific product by its ProductId and the vendor email from the database - Developer Senadheera P.V.P.P
        [HttpGet("vendor-prodid/{VendorEmail}/{ProductId}")]
        public async Task<ActionResult<Product>> GetVendorProdID(string VendorEmail, string ProductId)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var product = await productsCollection.Find(p => p.VendorEmail == VendorEmail && p.ProductId == ProductId).FirstOrDefaultAsync();
            if (product == null)
            {
                return NotFound(new { message = "Product not found" });
            }
            return Ok(product);
        }

        // Retrieves only the last product from the database - Developer Senadheera P.V.P.P
        [HttpGet("last-product")]
        public async Task<ActionResult<List<Product>>> GetLastProduct()
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var sortDefinition = Builders<Product>.Sort.Descending(p => p.ProductId);
            var products = await productsCollection.Find(p => true).Sort(sortDefinition).Limit(1).ToListAsync();
            return Ok(products);
        }
    }
}
