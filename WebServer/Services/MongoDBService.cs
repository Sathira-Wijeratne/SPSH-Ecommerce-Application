﻿/*******************************************************
 * File:           MyClass.cs
 * Author:         [Your Name]
 * Created:        [Date Created]
 * Description:    This file contains the implementation of the 
 *                 MyClass class, which handles [Brief Description 
 *                 of the Class Functionality].
 * 
 * Updates:
 * Date            Author          Changes
 * -------------------------------------------------------
 * [Date]          [Author]        [Description of Change]
 *
 * ****************************************************/

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

        public IMongoCollection<Rate> GetRatesCollection()
        {
            return _database.GetCollection<Rate>("Rates");
        }

        public IMongoCollection<ProductCategory> GetProductCategoriesCollection()
        {
            return _database.GetCollection<ProductCategory>("ProductCategories");
        }

        public IMongoCollection<CustomerNotfication> GetCustomerNotificationsCollection()
        {
            return _database.GetCollection<CustomerNotfication>("CustomerNotifications");
        }
    }

    public class MongoDBSettings
    {
        public string ConnectionString { get; set; }
        public string DatabaseName { get; set; }
    }
}
