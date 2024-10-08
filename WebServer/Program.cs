/*******************************************************
 * File:           Program.cs
 * Author:         Wijeratne D.M.S.D & Senadheera P.V.P.P
 * Created:        19.09.2024
 * Description:    This file sets up an ASP.NET Core web 
 *                 application, configuring MongoDB services 
 *                 with custom decimal serialization, 
 *                 registering MongoDBService for database 
 *                 access, and enabling API controllers, 
 *                 HTTPS redirection, authorization, and 
 *                 Swagger for API documentation.
 * ****************************************************/

using MongoDB.Bson.Serialization.Serializers;
using MongoDB.Bson.Serialization;
using MongoDB.Bson;
using SPSH_Ecommerce_Application.Services;

namespace SPSH_Ecommerce_Application
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container.
            // MongoDB decimal serialization configuration - referenced from : https://stackoverflow.com/questions/43473147/how-to-use-decimal-type-in-mongodb
            BsonSerializer.RegisterSerializer(typeof(decimal), new DecimalSerializer(BsonType.Decimal128));
            BsonSerializer.RegisterSerializer(typeof(decimal?), new NullableSerializer<decimal>(new DecimalSerializer(BsonType.Decimal128)));
            
            //MongoDB configuration - referenced from : https://code-maze.com/getting-started-aspnetcore-mongodb/
            builder.Services.Configure<MongoDBSettings>(
                builder.Configuration.GetSection("MongoDB"));
            builder.Services.AddSingleton<MongoDBService>();

            builder.Services.AddControllers();
            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            builder.Services.AddCors(policy =>
            {
                policy.AddPolicy("CorsPolicy", opt => opt.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());
            });

            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }

            app.UseHttpsRedirection();

            app.UseCors("CorsPolicy");

            app.UseAuthorization();


            app.MapControllers();

            app.Run();
        }
    }
}
