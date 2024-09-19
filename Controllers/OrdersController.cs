using Microsoft.AspNetCore.Http;
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

    public OrdersController(MongoDBService mongoDBService)
    {
        _mongoDBService = mongoDBService;
    }

    [HttpGet]
    public async Task<ActionResult<List<Order>>> Get()
    {
        var ordersCollection = _mongoDBService.GetOrdersCollection();
        var orders = await ordersCollection.Find(o => true).ToListAsync();

            TimeZoneInfo indiaTimeZone = TimeZoneInfo.FindSystemTimeZoneById("India Standard Time");

        foreach(var order in orders)
            {
                order.OrderDate = TimeZoneInfo.ConvertTimeFromUtc(order.OrderDate, indiaTimeZone);
            }

        return Ok(orders);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<Order>> Get(string id)
    {
        var ordersCollection = _mongoDBService.GetOrdersCollection();
        var order = await ordersCollection.Find(o => o.Id == id).FirstOrDefaultAsync();
        if (order == null)
        {
            return NotFound(new { message = "Order not found" });
        }

        // Convert UTC OrderDate to IST when displaying
        TimeZoneInfo indiaTimeZone = TimeZoneInfo.FindSystemTimeZoneById("India Standard Time");
        order.OrderDate = TimeZoneInfo.ConvertTimeFromUtc(order.OrderDate, indiaTimeZone);

        return Ok(order);
    }

    [HttpPost]
    public async Task<ActionResult> Create([FromBody] Order order)
    {
        if (order == null)
            return BadRequest(new { message = "Order data is missing" });

        // Store the order date as UTC
        order.OrderDate = DateTime.UtcNow;

        Console.WriteLine("OrderDate: " + order.OrderDate);

        var ordersCollection = _mongoDBService.GetOrdersCollection();
        await ordersCollection.InsertOneAsync(order);
        return CreatedAtAction(nameof(Get), new { id = order.Id }, order);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, Order updatedProduct)
    {
        var ordersCollection = _mongoDBService.GetOrdersCollection();
        var result = await ordersCollection.ReplaceOneAsync(o => o.Id == id, updatedProduct);
        if (result.MatchedCount == 0)
        {
            return NotFound(new { message = "Order not found" });
        }
        return NoContent();
    }

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
}
}
