/*
 * Description: This file defines the ProductCategory model, representing different categories of products in the e-commerce system.
 */

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace SPSH_Ecommerce_Application.Models
{
    public class ProductCategory
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string CategoryName { get; set; }

        public bool IsActive { get; set; }
    }
}
