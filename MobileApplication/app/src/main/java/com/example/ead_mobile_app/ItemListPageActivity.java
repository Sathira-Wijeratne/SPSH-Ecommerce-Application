//package com.example.ead_mobile_app;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Base64;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
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
//public class ItemListPageActivity extends AppCompatActivity {
//
//    private LinearLayout itemContainer;
//    private ExecutorService executorService;
//    private String selectedCategory;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.item_list_page);
//
//        // Get the selected category from the Intent
//        selectedCategory = getIntent().getStringExtra("category");
//
//        // Set up the container for items (cards)
//        itemContainer = findViewById(R.id.itemContainer);
//        executorService = Executors.newSingleThreadExecutor();
//
//        // Fetch items based on the selected category
//        fetchItemsByCategory(selectedCategory);
//    }
//
//    // Method to fetch items from the API
//    private void fetchItemsByCategory(String category) {
//        executorService.execute(() -> {
//            try {
//                String apiUrl = "http://192.168.137.1:2030/api/Products/search-category/" + category;
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
//                    showToast("Failed to fetch items");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }
//
//    // Process the JSON response and dynamically add item views
//    private void processResponse(String response) {
//        try {
//            JSONArray productsArray = new JSONArray(response);
//            for (int i = 0; i < productsArray.length(); i++) {
//                JSONObject product = productsArray.getJSONObject(i);
//                String name = product.getString("name");
//                int price = product.getInt("price");
//                int stock = product.getInt("stock");
//
//                // Add item view to the container
//                addItemCard(name, price, stock);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast("Failed to parse items");
//        }
//    }
//
//    // Method to dynamically add a card view for each item
//    private void addItemCard(String name, int price, int stock) {
//        new Handler(Looper.getMainLooper()).post(() -> {
//            // Create a container for the card (LinearLayout)
//            LinearLayout cardLayout = new LinearLayout(this);
//            cardLayout.setOrientation(LinearLayout.VERTICAL);
//            cardLayout.setPadding(16, 16, 16, 16);
//            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
//            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            ));
//            cardLayout.setGravity(Gravity.CENTER);
//
//            // Create and add the TextView for the product name
//            TextView nameTextView = new TextView(this);
//            nameTextView.setText(name);
//            nameTextView.setTextSize(18);
//            nameTextView.setGravity(Gravity.CENTER);
//            cardLayout.addView(nameTextView);
//
//            // Create and add the TextView for the product price
//            TextView priceTextView = new TextView(this);
//            priceTextView.setText("Price: $" + price);
//            priceTextView.setTextSize(16);
//            priceTextView.setGravity(Gravity.CENTER);
//            cardLayout.addView(priceTextView);
//
//            // Create and add the TextView for the product stock/quantity available
//            TextView stockTextView = new TextView(this);
//            stockTextView.setText("Available: " + stock);
//            stockTextView.setTextSize(14);
//            stockTextView.setGravity(Gravity.CENTER);
//            cardLayout.addView(stockTextView);
//
//            // Create and add a Button for "View"
//            Button viewButton = new Button(this);
//            viewButton.setText("View");
//            viewButton.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            ));
//            viewButton.setGravity(Gravity.CENTER);
//            cardLayout.addView(viewButton);
//
//            // Optional: Add click listener to the button
//            viewButton.setOnClickListener(v -> {
//                Toast.makeText(ItemListPageActivity.this, "Viewing " + name, Toast.LENGTH_SHORT).show();
//                // You can add logic to open a detailed view of the product if needed
//            });
//
//            // Add the card to the container layout
//            itemContainer.addView(cardLayout);
//        });
//    }
//
//    // Show toast message
//    private void showToast(String message) {
//        new Handler(Looper.getMainLooper()).post(() ->
//                Toast.makeText(ItemListPageActivity.this, message, Toast.LENGTH_SHORT).show()
//        );
//    }
//}
package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemListPageActivity extends AppCompatActivity {

    private LinearLayout itemContainer;
    private ExecutorService executorService;
    private String selectedCategory;
    private String customerEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_page);
        customerEmail = getIntent().getStringExtra("customerEmail");
        // Get the selected category from the Intent
        selectedCategory = getIntent().getStringExtra("category");

        // Set up the container for items (cards)
        itemContainer = findViewById(R.id.itemContainer);
        executorService = Executors.newSingleThreadExecutor();

        // Fetch items based on the selected category
        fetchItemsByCategory(selectedCategory);
    }

    // Method to fetch items from the API
    private void fetchItemsByCategory(String category) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Products/search-category/" + category;
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
                    showToast("Failed to fetch items");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    // Process the JSON response and dynamically add item views
    private void processResponse(String response) {
        try {
            JSONArray productsArray = new JSONArray(response);
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                String productId = product.getString("productId");  // Get product ID
                String name = product.getString("name");
                int price = product.getInt("price");
                int stock = product.getInt("stock");

                // Add item view to the container
                addItemCard(productId, name, price, stock);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse items");
        }
    }

    // Method to dynamically add a card view for each item
    private void addItemCard(String productId, String name, int price, int stock) {
        new Handler(Looper.getMainLooper()).post(() -> {
            // Create a container for the card (LinearLayout)
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16);
            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Create and add the TextView for the product name
            TextView nameTextView = new TextView(this);
            nameTextView.setText(name);
            nameTextView.setTextSize(18);
            cardLayout.addView(nameTextView);

            // Create and add the TextView for the product price
            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: $" + price);
            priceTextView.setTextSize(16);
            cardLayout.addView(priceTextView);

            // Create and add the TextView for the product stock/quantity available
            TextView stockTextView = new TextView(this);
            stockTextView.setText("Available: " + stock);
            stockTextView.setTextSize(14);
            cardLayout.addView(stockTextView);

            // Create and add a Button for "View"
            Button viewButton = new Button(this);
            viewButton.setText("View");
            viewButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            cardLayout.addView(viewButton);

            // Add the card to the container layout
            itemContainer.addView(cardLayout);

            // Set click listener on "View" button
            viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(ItemListPageActivity.this, SingleItemViewActivity.class);
                intent.putExtra("productId", productId);  // Pass the productId to SingleItemViewActivity
                intent.putExtra("customerEmail", customerEmail);
                startActivity(intent);
            });
        });
    }

    // Show toast message
    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(ItemListPageActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
