/*******************************************************
 * File:           User.cs
 * Author:         Wijeratne D.M.S.D
 * Created:        19.09.2024
 * Description:    This file defines the User model, 
 *                 representing user details such as role 
 *                 and activation status in the system.
 * ****************************************************/

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace SPSH_Ecommerce_Application.Models
{
    //General idea for Model Structure and attributes used are referenced from - https://code-maze.com/getting-started-aspnetcore-mongodb/
    public class User
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Name { get; set; }
        public string Role { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public bool Activated { get; set; }
    }
}
