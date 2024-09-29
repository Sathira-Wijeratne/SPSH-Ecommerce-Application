/*******************************************************
 * File:           CustomerNotificationsController.cs
 * Author:         Wijeratne D.M.S.D
 * Created:        24.09.2024
 * Description:    This file contains the implementation 
 *                 of the customer notofication
 *                 controller, which handles the backend
 *                 operations of customer notification 
 *                 management.
 * ****************************************************/

using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CustomerNotificationsController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        // Constructor to initialize the MongoDB service dependency
        public CustomerNotificationsController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        // Retrieves all notifications
        [HttpGet]
        public async Task<ActionResult<List<CustomerNotfication>>> Get()
        {
            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();
            var notifications = await notificationsCollection.Find(n => true).ToListAsync();
            return Ok(notifications);
        }

        // Retrieves notifications by OrderId
        [HttpGet("{OrderId}")]
        public async Task<ActionResult<List<CustomerNotfication>>> GetByOrderId(string OrderId)
        {
            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();
            var notification = await notificationsCollection.Find(n => n.OrderId == OrderId).ToListAsync();
            if (notification == null || notification.Count == 0)
            {
                return NotFound(new { message = "No notifications found for this order" });
            }
            return Ok(notification);
        }

        // Retrieves notifications by Customer Email
        [HttpGet("get-by-email/{CustomerEmail}")]
        public async Task<ActionResult<List<CustomerNotfication>>> GetByCustomerEmail(string CustomerEmail)
        {
            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();
            var notification = await notificationsCollection.Find(n => n.CustomerEmail == CustomerEmail).ToListAsync();
            if (notification == null || notification.Count == 0)
            {
                return NotFound(new { message = "No notifications found for this customer email" });
            }
            return Ok(notification);
        }

        // Creates a new customer notification
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] CustomerNotfication notification)
        {
            if (notification == null)
                return BadRequest(new { message = "Notification data is missing" });

            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();
            await notificationsCollection.InsertOneAsync(notification);
            return CreatedAtAction(nameof(GetByOrderId), new { orderId = notification.OrderId }, notification);
        }

        // Updates the MarkAsRead of Notification by OrderId
        [HttpPatch("mark-as-read/{OrderId}")]
        public async Task<IActionResult> UpdateMarkAsRead(string OrderId, [FromQuery] bool markAsRead)
        {
            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();

            var existingNotification = await notificationsCollection.Find(n => n.OrderId == OrderId).FirstOrDefaultAsync();

            if (existingNotification == null)
            {
                return NotFound(new { message = "Notification not found for this order" });
            }

            if (existingNotification.MarkAsRead == markAsRead)
            {
                var currentStatus = markAsRead ? "already marked as read" : "already marked as unread";
                return BadRequest(new { message = $"Customer notification is {currentStatus}" });
            }

            var update = Builders<CustomerNotfication>.Update.Set(n => n.MarkAsRead, markAsRead);
            var result = await notificationsCollection.UpdateOneAsync(n => n.OrderId == OrderId, update);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Failed to update notification" });
            }

            var status = markAsRead ? "read" : "unread";

            return Ok(new { message = $"Notification for Order {OrderId} has been marked as {status} successfully" });
        }


        // Deletes a notification by OrderId
        [HttpDelete("{OrderId}")]
        public async Task<IActionResult> Delete(string OrderId)
        {
            var notificationsCollection = _mongoDBService.GetCustomerNotificationsCollection();
            var result = await notificationsCollection.DeleteOneAsync(n => n.OrderId == OrderId);

            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "Notification not found for this order" });
            }

            return Ok(new { message = $"Notification for orderId: {OrderId}  has been deleted successfully" });
        }
    }
}
