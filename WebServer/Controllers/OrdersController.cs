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

        existingOrder.Status = updatedOrder.Status;

        var result = await ordersCollection.ReplaceOneAsync(o => o.OrderId == OrderId, existingOrder);

        return NoContent();
    }

    // Deletes an order from the database by its ID.
    [HttpDelete("{OrderId}")]
    public async Task<IActionResult> Delete(string OrderId)
    {
        var ordersCollection = _mongoDBService.GetOrdersCollection();
        var result = await ordersCollection.DeleteOneAsync(o => o.OrderId == OrderId);
        if (result.DeletedCount == 0)
        {
            return NotFound(new { message = "Order not found" });
        }
        return NoContent();
    }

    // Gets order status from orderId
    [HttpGet("status/{OrderId}")]
    public async Task<ActionResult<List<object>>> GetStatus(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var result = await ordersCollection.Find(o => o.OrderId == OrderId).Project(o => new {o.OrderId, o.Status}).ToListAsync();
            return Ok(result);
        }
}
}
