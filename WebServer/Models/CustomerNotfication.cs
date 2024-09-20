using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace SPSH_Ecommerce_Application.Models
{
    public class CustomerNotfication
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string OrderId { get; set; }

        public bool MarkAsRead { get; set; }
        public string NotificationMessage { get; set; }
    }
}
