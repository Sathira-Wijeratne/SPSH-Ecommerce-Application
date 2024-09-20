using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace SPSH_Ecommerce_Application.Models
{
    public class Rate
    {
        [BsonId] // Maps this property to the MongoDB _id field
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string VendorEmail { get; set; }
        public string CustomerEmail { get; set; }
        public int Stars { get; set; }
        public string Comment { get; set; }
    }
}
