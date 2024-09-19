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

        [HttpGet("{id}")]
        public async Task<ActionResult<User>> Get(string id)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var user = await usersCollection.Find(u => u.Id == id).FirstOrDefaultAsync();
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
            return CreatedAtAction(nameof(Get), new { id = user.Id }, user);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, User updatedUser)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var result = await usersCollection.ReplaceOneAsync(u => u.Id == id, updatedUser);
            if (result.MatchedCount == 0)
            {
                return NotFound(new { message = "User not found" });
            }
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var usersCollection = _mongoDBService.GetUsersCollection();
            var result = await usersCollection.DeleteOneAsync(u => u.Id == id);
            if (result.DeletedCount == 0)
            {
                return NotFound(new { message = "User not found" });
            }
            return NoContent();
        }
    }
}
