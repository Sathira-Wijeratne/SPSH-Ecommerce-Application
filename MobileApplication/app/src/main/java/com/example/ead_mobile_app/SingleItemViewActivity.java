//package com.example.ead_mobile_app;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Base64;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RatingBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class SingleItemViewActivity extends AppCompatActivity {
//
//    private LinearLayout itemContainer;
//    private RatingBar ratingBar;
//    private ExecutorService executorService;
//    private String productId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.single_item_view);
//
//        // Initialize views
//        itemContainer = findViewById(R.id.itemContainer);
//        ratingBar = findViewById(R.id.ratingBar);  // Reference the RatingBar from XML
//        executorService = Executors.newSingleThreadExecutor();
//
//        // Get the product ID passed from the previous activity
//        productId = getIntent().getStringExtra("productId");
//
//        // Fetch and display the product details
//        fetchProductDetails(productId);
//    }
//
//    private void fetchProductDetails(String productId) {
//        executorService.execute(() -> {
//            try {
//                String apiUrl = "http://192.168.137.1:2030/api/Products/" + productId;
//                URL url = new URL(apiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("Accept", "application/json");
//                connection.connect();
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    reader.close();
//                    processResponse(response.toString());
//                } else {
//                    showToast("Failed to fetch product details");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }
//
//    private void processResponse(String response) {
//        try {
//            JSONObject product = new JSONObject(response);
//            String name = product.getString("name");
//            int price = product.getInt("price");
//            int stock = product.getInt("stock");
//            String vendorEmail = product.getString("vendorEmail");
//            String description = product.getString("description");
//            String imageBase64 = product.getString("imageBase64");
//
//            // Decode Base64 image
//            byte[] decodedString = Base64.decode(imageBase64.split(",")[1], Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//            // Update the UI with product details
//            new Handler(Looper.getMainLooper()).post(() -> displayProductDetails(name, price, stock, vendorEmail, bitmap));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast("Failed to parse product details");
//        }
//    }
//
//    private void displayProductDetails(String name, int price, int stock, String vendorEmail, Bitmap productImage) {
//        // Set product image
//        ImageView productImageView = findViewById(R.id.productImage);
//        productImageView.setImageBitmap(productImage);
//
//        // Set product name
//        TextView nameTextView = findViewById(R.id.productName);
//        nameTextView.setText(name);
//
//        // Set product price
//        TextView priceTextView = findViewById(R.id.productPrice);
//        priceTextView.setText("Price: $" + price);
//
//        // Set product stock
//        TextView stockTextView = findViewById(R.id.productStock);
//        stockTextView.setText("Available: " + stock);
//
//        // Set product vendor email
//        TextView vendorEmailTextView = findViewById(R.id.productVendorEmail);
//        vendorEmailTextView.setText("Vendor: " + vendorEmail);
//
//        // Add click listener to "Add to Cart" button
//        Button addToCartButton = findViewById(R.id.addToCartButton);
//        addToCartButton.setOnClickListener(v -> {
//            Toast.makeText(SingleItemViewActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private void showToast(String message) {
//        new Handler(Looper.getMainLooper()).post(() ->
//                Toast.makeText(SingleItemViewActivity.this, message, Toast.LENGTH_SHORT).show()
//        );
//    }
//}

package com.example.ead_mobile_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleItemViewActivity extends AppCompatActivity {

    private LinearLayout itemContainer;
    private RatingBar ratingBar;
    private ExecutorService executorService;
    private String productId;
    private String customerEmail;  // User's email from login
    private String productName;
    private int productPrice;
    private String vendorEmail;
    private String imageBase64;  // For storing image data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_view);

        // Initialize views
        itemContainer = findViewById(R.id.itemContainer);
        ratingBar = findViewById(R.id.ratingBar);  // Reference the RatingBar from XML
        executorService = Executors.newSingleThreadExecutor();

        // Get the product ID and customer email passed from the previous activity
        productId = getIntent().getStringExtra("productId");
        customerEmail = getIntent().getStringExtra("customerEmail");  // Get customer email from login


        // Fetch and display the product details
        fetchProductDetails(productId);
    }

    private void fetchProductDetails(String productId) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Products/" + productId;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    processResponse(response.toString());
                } else {
                    showToast("Failed to fetch product details");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void processResponse(String response) {
        try {
            JSONObject product = new JSONObject(response);
            productName = product.getString("name");
            productPrice = product.getInt("price");
            int stock = product.getInt("stock");
            vendorEmail = product.getString("vendorEmail");
            String description = product.getString("description");
            imageBase64 = product.getString("imageBase64");

            // Decode Base64 image
            byte[] decodedString = Base64.decode(imageBase64.split(",")[1], Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Update the UI with product details
            new Handler(Looper.getMainLooper()).post(() -> displayProductDetails(productName, productPrice, stock, vendorEmail, bitmap));

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse product details");
        }
    }

    private void displayProductDetails(String name, int price, int stock, String vendorEmail, Bitmap productImage) {
        // Set product image
        ImageView productImageView = findViewById(R.id.productImage);
        productImageView.setImageBitmap(productImage);

        // Set product name
        TextView nameTextView = findViewById(R.id.productName);
        nameTextView.setText(name);

        // Set product price
        TextView priceTextView = findViewById(R.id.productPrice);
        priceTextView.setText("Price: $" + price);

        // Set product stock
        TextView stockTextView = findViewById(R.id.productStock);
        stockTextView.setText("Available: " + stock);

        // Set product vendor email
        TextView vendorEmailTextView = findViewById(R.id.productVendorEmail);
        vendorEmailTextView.setText("Vendor: " + vendorEmail);

        // Add click listener to "Add to Cart" button
        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            // Call method to add the product to the cart
            addToCart();
        });
    }

    private void addToCart() {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Carts";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Prepare the cart data in JSON format
                JSONObject cartData = new JSONObject();
                cartData.put("id","");
                cartData.put("customerEmail", customerEmail);
                cartData.put("productId", productId);
                cartData.put("productName", productName);
                cartData.put("vendorEmail", vendorEmail);
                cartData.put("productQty", 1);  // Default quantity is 1
                cartData.put("productPrice", productPrice);
                cartData.put("imageBase64", imageBase64);

                // Send the cart data to the server
                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(cartData.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    showToast("Item added to cart successfully");
                } else {
                    showToast("Failed to add item to cart");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(SingleItemViewActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
