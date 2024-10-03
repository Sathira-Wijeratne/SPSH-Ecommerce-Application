/*******************************************************
 * File:           Product.cs
 * Author:         Wijeratne D.M.S.D
 * Created:        19.09.2024
 * Description:    This file defines the Product model, 
 *                 representing product details within 
 *                 the e-commerce application.
 * ****************************************************/

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace SPSH_Ecommerce_Application.Models
{
    public class Product
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string ProductId { get; set; }
        public string ProductCategory {  get; set; }
        public string VendorEmail { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public int Stock {  get; set; }
        public string ImageBase64 { get; set; }
    }
}
