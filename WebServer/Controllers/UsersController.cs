/*******************************************************
 * File:           UsersController.cs
 * Author:         Wijeratne D.M.S.D
 * Created:        19.09.2024
 * Description:    This file contains the UsersController,
 *                 responsible for handling CRUD operations 
 *                 for users, including retrieving, creating, 
 *                 updating, and deleting users.
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
    public class UsersController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        // Constructor to initialize the MongoDB service dependency
        public UsersController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        // Retrieves all users from the database
        [HttpGet]
        public async Task<ActionResult<List<User>>> Get()
        {
            var usersCollection = _mongoDBService.GetUsersCollection();

            var sortOrder = new List<string> { "Admin", "CSR", "Vendor", "Customer" };

            var users = await usersCollection.Find(u=> true).ToListAsync();
            users = users.OrderBy(u => sortOrder.IndexOf(u.Role)).ToList();
            return Ok(users);
        }

        // Retrieves a specific user by email from the database
        [HttpGet("{email}")]
        public async Task<ActionResult<User>> Get(string email)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var user = await usersCollection.Find(u => u.Email == email).FirstOrDefaultAsync();
            if (user == null)
            {
                return NotFound(new { message = "User not found" });
            }
            return Ok(user);
        }

        // Retrieves specific user category from the database
        [HttpGet("get-by-category/{role}")]
        public async Task<ActionResult<List<User>>> GetByCategory(string role)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var users = await usersCollection.Find(u => u.Role == role).ToListAsync();
            return Ok(users);
        }

        // Creates a new user in the database
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] User user)
        {
            if (user == null)
                return BadRequest(new { message = "User data is missing" });

            var usersCollection = _mongoDBService.GetUsersCollection();

            var result = await usersCollection.Find(o => o.Email == user.Email).FirstOrDefaultAsync();
            if (result != null)
            {
                return Conflict(new { message = "User already exists" });
            }

            await usersCollection.InsertOneAsync(user);
            return CreatedAtAction(nameof(Get), new { email = user.Email }, user);
        }

        // Updates an existing user by email
        [HttpPut("{email}")]
        public async Task<IActionResult> Update(string email, User updatedUser)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();

            var existingUser= await usersCollection.Find(u => u.Email == email).FirstOrDefaultAsync();

            updatedUser.Id = existingUser.Id;
            updatedUser.Email = email;

            var result = await usersCollection.ReplaceOneAsync(u => u.Email == email, updatedUser);
            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "User not found" });
            }
            return Ok(new { message = $"User has been updated successfully" });
        }

        // Updates the password and role of an existing user by email
        /*[HttpPatch("update/{email}")]
        public async Task<IActionResult> UpdateUserPasswordAndRole(string email, [FromBody] User updatedUser)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();

            // Find the existing user by email
            var existingUser = await usersCollection.Find(u => u.Email == email).FirstOrDefaultAsync();

            if (existingUser == null)
            {
                return NotFound(new { message = "User not found" });
            }

            // Update only the Password and Role fields
            var update = Builders<User>.Update
                .Set(u => u.Password, updatedUser.Password)
                .Set(u => u.Role, updatedUser.Role);

            var result = await usersCollection.UpdateOneAsync(u => u.Email == email, update);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Failed to update user" });
            }

            return Ok(new { message = $"User with email {email} has been updated successfully" });
        }*/


        // Deletes a user from the database by email
        [HttpDelete("{email}")]
        public async Task<IActionResult> Delete(string email)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var result = await usersCollection.DeleteOneAsync(u => u.Email == email);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "User not found" });
            }
            return Ok(new { message = $"User has been deleted successfully" });
        }

        // Activates or deactivates a user account
        [HttpPatch("set-activation/{email}")]
        public async Task<IActionResult> SetActivation(string email, [FromQuery] bool activate)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var existingUser = await usersCollection.Find(u => u.Email == email).FirstOrDefaultAsync();

            if (existingUser == null)
            {
                return NotFound(new { message = "User not found" });
            }

            if (existingUser.Activated == activate)
            {
                var currentStatus = activate ? "already activated" : "already deactivated";
                return BadRequest(new { message = $"User account is {currentStatus}" });
            }

            var update = Builders<User>.Update.Set(u => u.Activated, activate);
            var result = await usersCollection.UpdateOneAsync(u => u.Email == email, update);

            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "Failed to activate user" });
            }

            var status = activate ? "activated" : "deactivated";
            return Ok(new { message = $"User account with email : {email} has been {status} successfully" });
        }
    }
}
