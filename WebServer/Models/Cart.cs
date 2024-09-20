namespace SPSH_Ecommerce_Application.Models
{
    public class Cart
    {
        public string Id { get; set; }
        public string CustomerEmail { get; set; }
        public string ProductId { get; set; }
        public string ProductName { get; set; }
        public string vendorEmail { get; set; }
        public int ProductQty { get; set; }
        public decimal ProductPrice { get; set; }
    }
}
