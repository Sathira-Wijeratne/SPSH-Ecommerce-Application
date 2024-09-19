using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    /*public class ProductsController : ControllerBase
    {
        // GET: api/<ProductsController>
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

        // GET api/<ProductsController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<ProductsController>
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

        // PUT api/<ProductsController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<ProductsController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }*/

    //above is auto generated routes
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

        [HttpGet("{id}")]
        public async Task<ActionResult<Product>> Get(string id)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var product = await productsCollection.Find(p => p.Id == id).FirstOrDefaultAsync();
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
            return CreatedAtAction(nameof(Get), new { id = product.Id }, product);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, Product updatedProduct)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var result = await productsCollection.ReplaceOneAsync(p => p.Id == id, updatedProduct);
            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var productsCollection = _mongoDBService.GetProductsCollection();
            var result = await productsCollection.DeleteOneAsync(p => p.Id == id);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Product not found" });
            }
            return NoContent();
        }
    }
}
