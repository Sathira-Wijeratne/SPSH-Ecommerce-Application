////package com.example.ead_mobile_app;
////
////import android.os.Bundle;
////
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////
////public class CartDetailsActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.cart_details);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////    }
////}
//
//package com.example.ead_mobile_app;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Base64;
//import android.view.Gravity;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
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
//public class CartDetailsActivity extends AppCompatActivity {
//
//    private LinearLayout cartContainer;
//    private ExecutorService executorService;
//    private String customerEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cart_details);
//
//        // Initialize UI elements
//        cartContainer = findViewById(R.id.main);  // Assuming the main layout is a LinearLayout
//        executorService = Executors.newSingleThreadExecutor();
//
//        // Get customer email from intent
//        customerEmail = getIntent().getStringExtra("customerEmail");
//
//        // Fetch cart items
//        fetchCartItems(customerEmail);
//    }
//
//    // Method to fetch cart items from the API
//    private void fetchCartItems(String customerEmail) {
//        executorService.execute(() -> {
//            try {
//                String apiUrl = "http://192.168.137.1:2030/api/Carts/" + customerEmail;
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
//
//                    // Process response to display cart items
//                    processCartResponse(response.toString());
//                } else {
//                    showToast("Failed to fetch cart items");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }
//
//    // Method to process cart items JSON response
//    private void processCartResponse(String response) {
//        try {
//            JSONArray cartArray = new JSONArray(response);
//            for (int i = 0; i < cartArray.length(); i++) {
//                JSONObject cartItem = cartArray.getJSONObject(i);
//                String productName = cartItem.getString("productName");
//                int productPrice = cartItem.getInt("productPrice");
//                int productQty = cartItem.getInt("productQty");
//                String imageBase64 = cartItem.getString("imageBase64");
//
//                // Decode image from Base64
//                Bitmap productImage = decodeBase64Image(imageBase64);
//
//                // Add the cart item card to the layout
//                addCartItemCard(productName, productPrice, productQty, productImage);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast("Failed to parse cart items");
//        }
//    }
//
//    // Method to dynamically add a card for each cart item
//    private void addCartItemCard(String productName, int productPrice, int productQty, Bitmap productImage) {
//        new Handler(Looper.getMainLooper()).post(() -> {
//            // Create a card layout (LinearLayout) to hold the product details
//            LinearLayout cardLayout = new LinearLayout(this);
//            cardLayout.setOrientation(LinearLayout.VERTICAL);
//            cardLayout.setPadding(16, 16, 16, 16);
//            cardLayout.setGravity(Gravity.CENTER_VERTICAL);
//            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
//            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            ));
//
//            // Create and add an ImageView for the product image
//            ImageView productImageView = new ImageView(this);
//            productImageView.setImageBitmap(productImage);
//            productImageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
//            cardLayout.addView(productImageView);
//
//            // Create and add a TextView for the product name
//            TextView nameTextView = new TextView(this);
//            nameTextView.setText(productName);
//            nameTextView.setTextSize(18);
//            cardLayout.addView(nameTextView);
//
//            // Create and add a TextView for the product price
//            TextView priceTextView = new TextView(this);
//            priceTextView.setText("Price: $" + productPrice);
//            priceTextView.setTextSize(16);
//            cardLayout.addView(priceTextView);
//
//            // Create and add a TextView for the product quantity
//            TextView qtyTextView = new TextView(this);
//            qtyTextView.setText("Quantity: " + productQty);
//            qtyTextView.setTextSize(14);
//            cardLayout.addView(qtyTextView);
//
//            // Add the card to the main container (cartContainer)
//            cartContainer.addView(cardLayout);
//        });
//    }
//
//    // Utility method to decode Base64 image string to Bitmap
//    private Bitmap decodeBase64Image(String base64Str) {
//        try {
//            String imageDataBytes = base64Str.substring(base64Str.indexOf(",") + 1);
//            byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Show toast message
//    private void showToast(String message) {
//        new Handler(Looper.getMainLooper()).post(() ->
//                Toast.makeText(CartDetailsActivity.this, message, Toast.LENGTH_SHORT).show()
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
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartDetailsActivity extends AppCompatActivity {

    private LinearLayout cartContainer;
    private ExecutorService executorService;
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_details);

        // Initialize UI elements
        cartContainer = findViewById(R.id.main);  // Assuming the main layout is a LinearLayout
        executorService = Executors.newSingleThreadExecutor();

        // Get customer email from intent
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Fetch cart items
        fetchCartItems(customerEmail);
    }

    // Method to fetch cart items from the API
    private void fetchCartItems(String customerEmail) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Carts/" + customerEmail;
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

                    // Process response to display cart items
                    processCartResponse(response.toString());
                } else {
                    showToast("Failed to fetch cart items");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    // Method to process cart items JSON response
    private void processCartResponse(String response) {
        try {
            JSONArray cartArray = new JSONArray(response);
            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject cartItem = cartArray.getJSONObject(i);
                String productId = cartItem.getString("productId");
                String productName = cartItem.getString("productName");
                int productPrice = cartItem.getInt("productPrice");
                int productQty = cartItem.getInt("productQty");
                String imageBase64 = cartItem.getString("imageBase64");

                // Decode image from Base64
                Bitmap productImage = decodeBase64Image(imageBase64);

                // Add the cart item card to the layout
                addCartItemCard(productId, productName, productPrice, productQty, productImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse cart items");
        }
    }

    // Method to dynamically add a card for each cart item, with a delete button
    private void addCartItemCard(String productId, String productName, int productPrice, int productQty, Bitmap productImage) {
        new Handler(Looper.getMainLooper()).post(() -> {
            // Create a card layout (LinearLayout) to hold the product details
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16);
            cardLayout.setGravity(Gravity.CENTER_VERTICAL);
            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Create and add an ImageView for the product image
            ImageView productImageView = new ImageView(this);
            productImageView.setImageBitmap(productImage);
            productImageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            cardLayout.addView(productImageView);

            // Create and add a TextView for the product name
            TextView nameTextView = new TextView(this);
            nameTextView.setText(productName);
            nameTextView.setTextSize(18);
            cardLayout.addView(nameTextView);

            // Create and add a TextView for the product price
            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: $" + productPrice);
            priceTextView.setTextSize(16);
            cardLayout.addView(priceTextView);

            // Create and add a TextView for the product quantity
            TextView qtyTextView = new TextView(this);
            qtyTextView.setText("Quantity: " + productQty);
            qtyTextView.setTextSize(14);
            cardLayout.addView(qtyTextView);

            // Create a "Delete" button and add it to the card layout
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete Item");
            deleteButton.setOnClickListener(v -> {
                // Call method to delete the item
                deleteCartItem(customerEmail, productId);
                // Remove the card layout from the container after deletion
                cartContainer.removeView(cardLayout);
            });
            cardLayout.addView(deleteButton);

            // Add the card to the main container (cartContainer)
            cartContainer.addView(cardLayout);
        });
    }

    // Method to delete a cart item using customerEmail and productId
    private void deleteCartItem(String customerEmail, String productId) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Carts/" + customerEmail + "/" + productId;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showToast("Item deleted successfully");
                } else {
                    showToast("Failed to delete item");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    // Utility method to decode Base64 image string to Bitmap
    private Bitmap decodeBase64Image(String base64Str) {
        try {
            String imageDataBytes = base64Str.substring(base64Str.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Show toast message
    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(CartDetailsActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
