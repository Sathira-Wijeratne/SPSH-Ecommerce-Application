﻿/*******************************************************
 * File:           Cart.cs
 * Author:         Senadheera P.V.P.P
 * Created:        20.09.2024
 * Description:    This file contains the definition of 
 *                 Cart schema in MongoDB.
 * ****************************************************/

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace SPSH_Ecommerce_Application.Models
{
    //General idea for Model Structure and attributes used are referenced from - https://code-maze.com/getting-started-aspnetcore-mongodb/

    public class Cart
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string CustomerEmail { get; set; }
        public string ProductId { get; set; }
        public string ProductName { get; set; }
        public string VendorEmail { get; set; }
        public int ProductQty { get; set; }
        public decimal ProductPrice { get; set; }
        public string ImageBase64 { get; set; }
    }
}
