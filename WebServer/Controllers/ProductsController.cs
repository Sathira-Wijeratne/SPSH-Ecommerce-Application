using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        public ProductsController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        [HttpGet]
        public async Task<ActionResult<List<Product>>> Get()
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var products = await productsCollection.Find(p => true).ToListAsync();
            return Ok(products);
        }

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

        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Product product)
        {
            if (product == null)
                return BadRequest(new { message = "Product data is missing" });

            var productsCollection = _mongoDBService.GetProductsCollection();
            await productsCollection.InsertOneAsync(product);
            return CreatedAtAction(nameof(Get), new { ProductId = product.Id }, product);
        }

        [HttpPut("{ProductId}")]
        public async Task<IActionResult> Update(string ProductId, [FromBody] Product updatedProduct)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();

            // Ensure the updatedProduct does not have its Id modified (ignore the Id from the body)
            var existingProduct = await productsCollection.Find(p => p.ProductId == ProductId).FirstOrDefaultAsync();

            updatedProduct.Id = existingProduct.Id;
            updatedProduct.ProductId = ProductId;  

            var result = await productsCollection.ReplaceOneAsync(p => p.ProductId == ProductId, updatedProduct);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }

            return NoContent();
        }


        [HttpDelete("{ProductId}")]
        public async Task<IActionResult> Delete(string ProductId)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var result = await productsCollection.DeleteOneAsync(p => p.ProductId == ProductId);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }
            return NoContent();
        }
    }
}
