/*******************************************************
 * File:           MyClass.cs
 * Author:         [Your Name]
 * Created:        [Date Created]
 * Description:    This file contains the implementation of the 
 *                 MyClass class, which handles [Brief Description 
 *                 of the Class Functionality].
 * 
 * Updates:
 * Date            Author          Changes
 * -------------------------------------------------------
 * [Date]          [Author]        [Description of Change]
 *
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
            // MongoDB decimal serialization configuration
            BsonSerializer.RegisterSerializer(typeof(decimal), new DecimalSerializer(BsonType.Decimal128));
            BsonSerializer.RegisterSerializer(typeof(decimal?), new NullableSerializer<decimal>(new DecimalSerializer(BsonType.Decimal128)));
            //MongoDB configuration
            builder.Services.Configure<MongoDBSettings>(
                builder.Configuration.GetSection("MongoDB"));

            builder.Services.AddSingleton<MongoDBService>();

            builder.Services.AddControllers();
            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();


            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }

            app.UseHttpsRedirection();

            app.UseAuthorization();


            app.MapControllers();

            app.Run();
        }
    }
}
