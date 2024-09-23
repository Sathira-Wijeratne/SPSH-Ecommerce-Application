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
        [HttpGet("{id}")]
        public async Task<ActionResult<Order>> Get(string id)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var order = await ordersCollection.Find(o => o.Id == id).FirstOrDefaultAsync();
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
            return CreatedAtAction(nameof(Get), new { id = order.Id }, order);
        }

        // Updates the status of an existing order.
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, [FromBody] Order updatedOrder)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();

            var existingOrder = await ordersCollection.Find(o => o.Id == id).FirstOrDefaultAsync();

            if (existingOrder == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            existingOrder.Status = updatedOrder.Status;

            var result = await ordersCollection.ReplaceOneAsync(o => o.Id == id, existingOrder);

            return NoContent();
        }

        // Deletes an order from the database by its ID.
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var result = await ordersCollection.DeleteOneAsync(o => o.Id == id);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Order not found" });
            }
            return NoContent();
        }

        [HttpPut("update-note/{orderId}")]
        public async Task<IActionResult> UpdateNoteForOrder(string orderId, [FromBody] string newNote)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();

            // Define the filter to match all orders with the specified OrderId
            var filter = Builders<Order>.Filter.Eq(o => o.OrderId, orderId);

            // Define the update to set the Note field with the new value
            var update = Builders<Order>.Update.Set(o => o.Note, newNote);

            // Update all matching documents
            var result = await ordersCollection.UpdateManyAsync(filter, update);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "No orders found for the specified OrderId" });
            }

            return Ok(new { message = $"{result.ModifiedCount} order(s) updated with new note" });
        }

        // Retrieves orders by status
        [HttpGet("status/{status}")]
        public async Task<ActionResult<Order>> GetByStatus(string status)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var orders = await ordersCollection.Find(o => o.Status == status).ToListAsync();
            if (orders == null)
            {
                return NotFound(new { message = "Orders not found" });
            }

            return Ok(orders);
        }

        // Retrieves orders by status and customer email
        [HttpGet("status-customer/{status}/{customerEmail}")]
        public async Task<ActionResult<Order>> GetByStatusCustomer(string status, string customerEmail)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var orders = await ordersCollection.Find(o => o.Status == status && o.CustomerEmail == customerEmail).ToListAsync();
            if (orders == null)
            {
                return NotFound(new { message = "Orders not found" });
            }

            return Ok(orders);
        }

    }
}
