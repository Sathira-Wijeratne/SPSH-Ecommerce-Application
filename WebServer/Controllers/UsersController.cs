/*
 * Description: This file contains the UsersController, responsible for handling
 * CRUD operations for users, including retrieving, creating, updating, and deleting users.
 */

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
            var users = await usersCollection.Find(u=> true).ToListAsync();
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

        // Creates a new user in the database
        [HttpPost]
        public async Task<ActionResult> Create([FromBody] User user)
        {
            if (user == null)
                return BadRequest(new { message = "User data is missing" });

            var usersCollection = _mongoDBService.GetUsersCollection();
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
            return NoContent();
        }

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
            return NoContent();
        }
    }
}
