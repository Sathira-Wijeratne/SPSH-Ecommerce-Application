using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartsController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        public CartsController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        [HttpGet("{customerEmail}")]
        public async Task<ActionResult<List<Cart>>> Get(string customerEmail)
        {
            var CartCollection = _mongoDBService.GetCartsCollection();
            var orders = await CartCollection.Find(c => c.CustomerEmail == customerEmail).ToListAsync();

            return Ok(orders);
        }

        [HttpGet("{customerEmail}/{productId}")]
        public async Task<ActionResult<List<Cart>>> Get(string customerEmail, string productId)
        {
            var CartCollection = _mongoDBService.GetCartsCollection();
            var orders = await CartCollection.Find(c => c.CustomerEmail == customerEmail && c.ProductId == productId).FirstOrDefaultAsync();

            return Ok(orders);
        }

        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Cart cart)
        {
            if (cart == null)
                return BadRequest(new { message = "Cart data is missing" });

            var cartCollection = _mongoDBService.GetCartsCollection();
            await cartCollection.InsertOneAsync(cart);
            return CreatedAtAction(nameof(Get), new { customerEmail = cart.CustomerEmail, productId = cart.ProductId }, cart);

        }

    }
}
