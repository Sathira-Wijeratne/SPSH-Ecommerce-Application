using Microsoft.AspNetCore.Mvc.ModelBinding;

namespace SPSH_Ecommerce_Application.Models
{
    public class Product
    {
        public string Id { get; set; }
        public string ProductId { get; set; }
        public string VendorEmail { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public int Stock {  get; set; }
    }
}
