using Microsoft.Extensions.Options;
using MongoDB.Driver;
using SPSH_Ecommerce_Application.Models;

namespace SPSH_Ecommerce_Application.Services
{
    public class MongoDBService
    {
        private readonly IMongoDatabase _database;

        public MongoDBService(IOptions<MongoDBSettings> mongoDBSettings)
        {
            var client = new MongoClient(mongoDBSettings.Value.ConnectionString);
            _database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
        }

        public IMongoCollection<Product> GetProductsCollection()
        {
            return _database.GetCollection<Product>("Products");
        }

        public IMongoCollection<Order> GetOrdersCollection()
        {
            return _database.GetCollection<Order>("Orders");
        }

        public IMongoCollection<User> GetUsersCollection()
        {
            return _database.GetCollection<User>("Users");
        }

        public IMongoCollection<Cart> GetCartsCollection()
        {
            return _database.GetCollection<Cart>("Carts");
        }
    }

    public class MongoDBSettings
    {
        public string ConnectionString { get; set; }
        public string DatabaseName { get; set; }
    }
}
