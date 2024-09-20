using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        public CartController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        [HttpGet("{email}")]
        public async Task<ActionResult<List<Order>>> Get(string email)
        {
            var CartCollection = _mongoDBService.GetCartsCollection();
            var orders = await CartCollection.Find(c => c.CustomerEmail == email).ToListAsync();

            return Ok(orders);
        }

    }
}
