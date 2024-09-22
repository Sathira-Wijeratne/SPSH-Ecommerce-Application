/*
 * Description: This file is responsible for handling API requests related to Order management,
 * including CRUD operations (Create, Read, Update, Delete) for orders in the e-commerce system
 */

using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Services;
using SPSH_Ecommerce_Application.Models;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrdersController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        // Constructor for initializing the MongoDB service dependency
        public OrdersController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        // Retrieves all orders from the database
        [HttpGet]
        public async Task<ActionResult<List<Order>>> Get()
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var orders = await ordersCollection.Find(o => true).ToListAsync();

            return Ok(orders);
        }

        // Retrieves a specific order by its ID from the database
        [HttpGet("{OrderId}")]
        public async Task<ActionResult<Order>> Get(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var order = await ordersCollection.Find(o => o.OrderId == OrderId).FirstOrDefaultAsync();
            if (order == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            return Ok(order);
        }

        // Creates a new order in the database
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Order order)
        {
            if (order == null)
                return BadRequest(new { message = "Order data is missing" });

            var ordersCollection = _mongoDBService.GetOrdersCollection();
            await ordersCollection.InsertOneAsync(order);
            return CreatedAtAction(nameof(Get), new { orderId = order.OrderId }, order);
        }

        // Updates the status of an existing order.
        [HttpPut("{OrderId}")]
        public async Task<IActionResult> Update(string OrderId, [FromBody] Order updatedOrder)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();

            var existingOrder = await ordersCollection.Find(o => o.OrderId == OrderId).FirstOrDefaultAsync();

            if (existingOrder == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            //existingOrder.Status = updatedOrder.Status;
            updatedOrder.Id = existingOrder.Id;
            updatedOrder.OrderId = existingOrder.OrderId;

            var result = await ordersCollection.ReplaceOneAsync(o => o.OrderId == OrderId, updatedOrder);

                return Ok(new { message = $"Order {OrderId} has been updated successfully" });
            }

        // Deletes an order from the database by its ID.
        [HttpDelete("{OrderId}")]
        public async Task<IActionResult> Delete(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var result = await ordersCollection.DeleteManyAsync(o => o.OrderId == OrderId);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Order not found" });
            }
            return Ok(new { message = $"Order {OrderId} has been deleted successfully" });
        }

        // Gets order status from orderId
        [HttpGet("status/{OrderId}")]
        public async Task<ActionResult<List<object>>> GetStatus(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var result = await ordersCollection.Find(o => o.OrderId == OrderId).Project(o => new { o.OrderId, o.Status }).FirstOrDefaultAsync();
            return Ok(result);
        }

        // Manage order status from orderId
        [HttpPatch("manage/{OrderId}")]
        public async Task<IActionResult> ManageOrder(string OrderId, [FromQuery] string Status)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var existingOrder = await ordersCollection.Find(o => o.OrderId == OrderId).FirstOrDefaultAsync();

            if (existingOrder == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            var validStatuses = new List<string> { "Cancelled", "Delivered", "Completed", "Processing" };

            if (!validStatuses.Contains(Status))
            {
                return BadRequest(new { message = $"Invalid status. Valid statuses are: {string.Join(", ", validStatuses)}" });
            }

            var update = Builders<Order>.Update.Set(o => o.Status, Status);
            var result = await ordersCollection.UpdateManyAsync(o => o.OrderId == OrderId, update);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Failed to update order status" });
            }

            return Ok(new { message = $"Order {OrderId} has been updated to status: {Status}" });
        }
    }
}
