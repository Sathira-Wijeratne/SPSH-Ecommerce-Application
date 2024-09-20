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

        public UsersController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        [HttpGet]
        public async Task<ActionResult<List<User>>> Get()
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var users = await usersCollection.Find(u=> true).ToListAsync();
            return Ok(users);
        }

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

        [HttpPost]
        public async Task<ActionResult> Create([FromBody] User user)
        {
            if (user == null)
                return BadRequest(new { message = "User data is missing" });

            var usersCollection = _mongoDBService.GetUsersCollection();
            await usersCollection.InsertOneAsync(user);
            return CreatedAtAction(nameof(Get), new { email = user.Email }, user);
        }

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
