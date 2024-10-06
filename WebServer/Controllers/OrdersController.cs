/*******************************************************
 * File:           OrdersController.cs
 * Author:         Wijeratne D.M.S.D & Senadheera P.V.P.P
 * Created:        19.09.2024
 * Description:    This file is responsible for handling 
 *                 API requests related to Order
 *                 management,including CRUD operations 
 *                 (Create, Read, Update, Delete) for 
 *                 orders in the e-commerce system.
 * ****************************************************/

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

        // Constructor for initializing the MongoDB service dependency - Developer Wijeratne D.M.S.D
        public OrdersController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        // Retrieves all orders from the database - Developer Wijeratne D.M.S.D
        [HttpGet]
        public async Task<ActionResult<List<Order>>> Get()
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var sortOrder = new List<string> { "Requested to cancel", "Processing", "Delivered", "Completed", "Cancelled" };
            var orders = await ordersCollection.Find(o => true).ToListAsync();
            orders = orders.OrderBy(o => sortOrder.IndexOf(o.Status)).ToList();
            return Ok(orders);
        }

        // Retrieves all order records for specific orderID from the database - Developer Wijeratne D.M.S.D
        [HttpGet("{OrderId}")]
        public async Task<ActionResult<Order>> Get(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var order = await ordersCollection.Find(o => o.OrderId == OrderId).ToListAsync();
            if (order == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            return Ok(order);
        }

        // Creates a new order in the database - Developer Wijeratne D.M.S.D
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Order order)
        {
            if (order == null)
                return BadRequest(new { message = "Order data is missing" });

            var ordersCollection = _mongoDBService.GetOrdersCollection();
            await ordersCollection.InsertOneAsync(order);
            return CreatedAtAction(nameof(Get), new { orderId = order.OrderId }, order);
        }

        /*// Updates the status of an existing order.
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
            //updatedOrder.Id = existingOrder.Id;
            //updatedOrder.OrderId = existingOrder.OrderId;

            var result = await ordersCollection.ReplaceOneAsync(o => o.OrderId == OrderId, updatedOrder);

            return Ok(new { message = $"Order {OrderId} has been updated successfully" });
        }*/

        // Deletes an order from the database by its ID - Developer Wijeratne D.M.S.D
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

        // Gets order status from orderId - Developer Wijeratne D.M.S.D
        [HttpGet("status/{OrderId}")]
        public async Task<ActionResult<List<object>>> GetStatus(string OrderId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var result = await ordersCollection.Find(o => o.OrderId == OrderId).Project(o => new { o.OrderId, o.Status }).FirstOrDefaultAsync();
            return Ok(result);
        }

        // Manage order status from orderId - Developer Wijeratne D.M.S.D
        [HttpPatch("manage/{OrderId}")]
        public async Task<IActionResult> ManageOrder(string OrderId, [FromQuery] string Status)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var existingOrder = await ordersCollection.Find(o => o.OrderId == OrderId).FirstOrDefaultAsync();

            if (existingOrder == null)
            {
                return NotFound(new { message = "Order not found" });
            }

            var validStatuses = new List<string> { "Cancelled", "Requested to cancel", "Delivered", "Completed", "Processing" };

            if (!validStatuses.Contains(Status))
            {
                return BadRequest(new { message = $"Invalid status. Valid statuses are: {string.Join(", ", validStatuses)}" });
            }

            var update = Builders<Order>.Update.Set(o => o.Status, Status);
            string insertedNotificationId = null;

            if (Status == "Cancelled")
            {
                var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();

                try
                {
                    var notification = new CustomerNotfication
                    {
                        OrderId = existingOrder.OrderId,
                        CustomerEmail = existingOrder.CustomerEmail,
                        MarkAsRead = false,
                        NotificationMessage = $"Your order with OrderId {OrderId} has been cancelled."
                    };

                    await notificationsCollection.InsertOneAsync(notification);

                    // Store the inserted notification ID in case we need to roll it back later
                    insertedNotificationId = notification.Id;
                }
                catch (Exception ex)
                {
                    return StatusCode(500, new { message = $"Failed to create notification: {ex.Message}" });
                }
            }

            var result = await ordersCollection.UpdateManyAsync(o => o.OrderId == OrderId, update);

            if (result.MatchedCount == 0)
            {
                if (!string.IsNullOrEmpty(insertedNotificationId))
                {
                    var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();

                    // Rollback previously inserted notification to maintain consistency
                    await notificationsCollection.DeleteOneAsync(n => n.Id == insertedNotificationId);
                }

                return NotFound(new { message = "Failed to update order status" });
            }

            return Ok(new { message = $"Order {OrderId} has been updated to status: {Status}" });
        }

        //Add cancelation note when cancelling the order - Developer Senadheera P.V.P.P
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

        // Retrieves orders by status - Developer Senadheera P.V.P.P
        [HttpGet("get-by-status/{status}")]
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

        // Retrieves orders by status and customer email - Developer Senadheera P.V.P.P
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

        // Retrieves 5 recent orders by of a particular vendor - Developer Senadheera P.V.P.P
        [HttpGet("vendor-recent-orders/{vendorEmail}")]
        public async Task<ActionResult<Order>> VendorRecentOrders(string vendorEmail)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();

            // Define sorting: OrderId descending, ProductId ascending
            var sortDefinition = Builders<Order>.Sort.Descending(o => o.OrderId).Ascending(o => o.ProductId);
            var orders = await ordersCollection.Find(o => o.VendorEmail == vendorEmail).Sort(sortDefinition).Limit(5).ToListAsync();

            if (orders == null)
            {
                return NotFound(new { message = "Orders not found" });
            }

            return Ok(orders);
        }

        // Retrieves all orders for a specific vendor - Developer Senadheera P.V.P.P
        [HttpGet("vendor/{vendorEmail}")]
        public async Task<ActionResult<List<Order>>> GetVendorOrders(string vendorEmail)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var sortOrder = new List<string> { "Requested to cancel", "Processing", "Delivered", "Completed", "Cancelled" };
            var orders = await ordersCollection.Find(o => o.VendorEmail == vendorEmail).ToListAsync();
            orders = orders.OrderBy(o => sortOrder.IndexOf(o.Status)).ToList();
            return Ok(orders);
        }

        // Retrieves orders by status and product id
        [HttpGet("get-by-status-prodid/{status}/{ProductId}")]
        public async Task<ActionResult<Order>> GetByStatusProdID(string status, string ProductId)
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var orders = await ordersCollection.Find(o => o.Status == status && o.ProductId == ProductId).ToListAsync();
            if (orders == null)
            {
                return NotFound(new { message = "Orders not found" });
            }

            return Ok(orders);
        }

        // Retrieves the next order ID - Developer Senadheera P.V.P.P
        [HttpGet("get-next-oid")]
        public async Task<ActionResult<List<Order>>> GetNextOrderID()
        {
            var ordersCollection = _mongoDBService.GetOrdersCollection();
            var sortDefinition = Builders<Order>.Sort.Descending(o => o.OrderId);
            var orders = await ordersCollection.Find(o => true).Sort(sortDefinition).Limit(1).Project(o => new { o.OrderId }).FirstOrDefaultAsync();

            int nextOid = Int32.Parse(orders.OrderId.Substring(1)) + 1;
            String newOrderId = "O" + nextOid.ToString("D3");

            return Ok(new { newOid = newOrderId });
        }

    }
}
