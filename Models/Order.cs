namespace SPSH_Ecommerce_Application.Models
{
    public class Order
    {
        public string Id { get; set; }
        public string ProductId { get; set; }
        public string CustomerEmail { get; set; }
        public string Status { get; set; }
        public DateTime OrderDate { get; set; }
    }
}
