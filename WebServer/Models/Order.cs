/*******************************************************
 * File:           Order.cs
 * Author:         Wijeratne D.M.S.D
 * Created:        19.09.2024
 * Description:    This file defines the Order model, 
 *                 representing a customer's order.
 * ****************************************************/

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace SPSH_Ecommerce_Application.Models
{
    //General idea for Model Structure and attributes used are referenced from - https://code-maze.com/getting-started-aspnetcore-mongodb/
    public class Order
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set;}
        public string OrderId { get; set;}
        public string ProductId { get; set; }
        public string ProductName { get; set; }
        public string VendorEmail { get; set; }
        public int ProductQuantity { get; set; }
        public decimal ProductUnitPrice { get; set; }
        public string CustomerEmail { get; set; }
        public string Status { get; set; }
        public string Note { get; set; }
        public string ImageBase64 { get; set; }
    }
}
