using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RatesController : ControllerBase
    {
        private readonly MongoDBService _mongoDBService;

        public RatesController(MongoDBService mongoDBService)
        {
            _mongoDBService = mongoDBService;
        }

        [HttpGet]
        public async Task<ActionResult<List<Rate>>> Get()
        {
            var RateCollection = _mongoDBService.GetRatesCollection();
            var rates = await RateCollection.Find(r => true).ToListAsync();

            return Ok(rates);
        }

        [HttpGet("cust/{customerEmail}")]
        public async Task<ActionResult<List<Rate>>> GetSingleCustAllRatings(string customerEmail)
        {
            var RateCollection = _mongoDBService.GetRatesCollection();
            var rates = await RateCollection.Find(r => r.CustomerEmail == customerEmail).ToListAsync();

            return Ok(rates);
        }

        [HttpGet("vendor/{venderEmail}")]
        public async Task<ActionResult<List<Rate>>> GetSingleVendorAllRatings(string venderEmail)
        {
            var RateCollection = _mongoDBService.GetRatesCollection();
            var rates = await RateCollection.Find(r => r.VendorEmail == venderEmail).ToListAsync();

            return Ok(rates);
        }

        [HttpGet("{customerEmail}/{venderEmail}")]
        public async Task<ActionResult<List<Rate>>> GetSingleCustSingleVendorRatings(string customerEmail, string venderEmail)
        {
            var RateCollection = _mongoDBService.GetRatesCollection();
            var rates = await RateCollection.Find(r => r.CustomerEmail == customerEmail && r.VendorEmail == venderEmail).FirstOrDefaultAsync();

            return Ok(rates);
        }

        [HttpPost]
        public async Task<ActionResult> Create([FromBody] Rate rate)
        {
            if (rate == null)
                return BadRequest(new { message = "Rate data is missing" });

            var rateCollection = _mongoDBService.GetRatesCollection();
            await rateCollection.InsertOneAsync(rate);
            return CreatedAtAction(nameof(Get), new { customerEmail = rate.CustomerEmail, venderEmail = rate.VendorEmail }, rate);

        }
    }
}
