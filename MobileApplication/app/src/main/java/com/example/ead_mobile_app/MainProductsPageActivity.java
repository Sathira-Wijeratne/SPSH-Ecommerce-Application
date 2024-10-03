//////package com.example.ead_mobile_app;
//////
//////import android.os.Bundle;
//////import android.os.Handler;
//////import android.os.Looper;
//////import android.widget.Toast;
//////
//////import androidx.appcompat.app.AppCompatActivity;
//////import androidx.core.view.ViewCompat;
//////import androidx.core.view.WindowInsetsCompat;
//////import androidx.recyclerview.widget.GridLayoutManager;
//////import androidx.recyclerview.widget.RecyclerView;
//////
//////import org.json.JSONArray;
//////import org.json.JSONException;
//////import org.json.JSONObject;
//////
//////import java.io.BufferedReader;
//////import java.io.InputStreamReader;
//////import java.net.HttpURLConnection;
//////import java.net.URL;
//////import java.util.ArrayList;
//////import java.util.HashSet;
//////import java.util.List;
//////import java.util.concurrent.ExecutorService;
//////import java.util.concurrent.Executors;
//////
//////public class MainProductsPageActivity extends AppCompatActivity {
//////
//////    private RecyclerView categoryRecyclerView;
//////    private CategoryAdapter categoryAdapter;
//////    private List<String> categories;
//////    private final String API_URL = "http://192.168.137.1:2030/api/Products"; // Replace with your actual API URL
//////
//////    private ExecutorService executorService;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.main_products_page);
//////
//////        // Set up RecyclerView
//////        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
//////        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns
//////
//////        categories = new ArrayList<>();
//////        executorService = Executors.newSingleThreadExecutor();
//////
//////        // Fetch categories from API
//////        fetchCategoriesFromApi();
//////
//////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//////            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
//////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
//////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
//////            return WindowInsetsCompat.CONSUMED;
//////        });
//////    }
//////
//////    // Method to fetch categories from API
//////    private void fetchCategoriesFromApi() {
//////        executorService.execute(() -> {
//////            try {
//////                URL url = new URL(API_URL);
//////                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//////                connection.setRequestMethod("GET");
//////                connection.connect();
//////
//////                // Check if connection is successful
//////                int responseCode = connection.getResponseCode();
//////                if (responseCode == HttpURLConnection.HTTP_OK) {
//////                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//////                    StringBuilder response = new StringBuilder();
//////                    String line;
//////                    while ((line = reader.readLine()) != null) {
//////                        response.append(line);
//////                    }
//////                    reader.close();
//////
//////                    // Parse JSON response and extract categories
//////                    parseCategoriesFromResponse(response.toString());
//////                } else {
//////                    showToast("Failed to fetch data from API");
//////                }
//////            } catch (Exception e) {
//////                e.printStackTrace();
//////                showToast("Error: " + e.getMessage());
//////            }
//////        });
//////    }
//////
//////    // Parse the JSON response and extract unique categories
//////    private void parseCategoriesFromResponse(String response) {
//////        try {
//////            // Convert response to JSONArray
//////            JSONArray jsonArray = new JSONArray(response);
//////
//////            HashSet<String> categorySet = new HashSet<>(); // To hold unique categories
//////
//////            // Loop through each product in the response
//////            for (int i = 0; i < jsonArray.length(); i++) {
//////                JSONObject product = jsonArray.getJSONObject(i);
//////                String category = product.getString("productCategory"); // Extract category
//////                categorySet.add(category); // Ensure uniqueness with HashSet
//////            }
//////
//////            categories.clear();
//////            categories.addAll(categorySet); // Add unique categories to the list
//////
//////            // Update UI on the main thread
//////            new Handler(Looper.getMainLooper()).post(() -> {
//////                categoryAdapter = new CategoryAdapter(categories, MainProductsPageActivity.this); // Pass the context here
//////                categoryRecyclerView.setAdapter(categoryAdapter);
//////            });
//////
//////        } catch (JSONException e) {
//////            e.printStackTrace();
//////            showToast("Failed to parse data");
//////        }
//////    }
//////
//////    // Show toast message
//////    private void showToast(String message) {
//////        new Handler(Looper.getMainLooper()).post(() ->
//////                Toast.makeText(MainProductsPageActivity.this, message, Toast.LENGTH_SHORT).show()
//////        );
//////    }
//////}
////package com.example.ead_mobile_app;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.os.Handler;
////import android.os.Looper;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////import androidx.recyclerview.widget.GridLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import org.json.JSONArray;
////import org.json.JSONException;
////import org.json.JSONObject;
////
////import java.io.BufferedReader;
////import java.io.InputStreamReader;
////import java.net.HttpURLConnection;
////import java.net.URL;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.concurrent.ExecutorService;
////import java.util.concurrent.Executors;
////
////public class MainProductsPageActivity extends AppCompatActivity {
////
////    private RecyclerView categoryRecyclerView;
////    private CategoryAdapter categoryAdapter;
////    private List<String> categories;
////    private final String API_URL = "http://192.168.137.1:2030/api/ProductCategories/active"; // Updated API URL
////
////    private ExecutorService executorService;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.main_products_page);
////
////        // Set up RecyclerView
////        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
////        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns
////
////        categories = new ArrayList<>();
////        executorService = Executors.newSingleThreadExecutor();
////
////        // Fetch categories from API
////        fetchCategoriesFromApi();
////
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
////                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
////            return WindowInsetsCompat.CONSUMED;
////        });
////    }
////
////    // Method to fetch categories from API
////    private void fetchCategoriesFromApi() {
////        executorService.execute(() -> {
////            try {
////                URL url = new URL(API_URL);
////                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////                connection.setRequestMethod("GET");
////                connection.connect();
////
////                // Check if connection is successful
////                int responseCode = connection.getResponseCode();
////                if (responseCode == HttpURLConnection.HTTP_OK) {
////                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                    StringBuilder response = new StringBuilder();
////                    String line;
////                    while ((line = reader.readLine()) != null) {
////                        response.append(line);
////                    }
////                    reader.close();
////
////                    // Parse JSON response and extract categories
////                    parseCategoriesFromResponse(response.toString());
////                } else {
////                    showToast("Failed to fetch data from API");
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////                showToast("Error: " + e.getMessage());
////            }
////        });
////    }
////
////    // Parse the JSON response and extract categories
////    private void parseCategoriesFromResponse(String response) {
////        try {
////            // Convert response to JSONArray
////            JSONArray jsonArray = new JSONArray(response);
////            categories.clear();
////
////            // Loop through each category in the response
////            for (int i = 0; i < jsonArray.length(); i++) {
////                JSONObject categoryObject = jsonArray.getJSONObject(i);
////                String categoryName = categoryObject.getString("categoryName"); // Extract category name
////                categories.add(categoryName);
////            }
////
////            // Update UI on the main thread
////            new Handler(Looper.getMainLooper()).post(() -> {
////                categoryAdapter = new CategoryAdapter(categories);
////                categoryRecyclerView.setAdapter(categoryAdapter);
////            });
////
////        } catch (JSONException e) {
////            e.printStackTrace();
////            showToast("Failed to parse data");
////        }
////    }
////
////    // Show toast message
////    private void showToast(String message) {
////        new Handler(Looper.getMainLooper()).post(() ->
////                Toast.makeText(MainProductsPageActivity.this, message, Toast.LENGTH_SHORT).show()
////        );
////    }
////
////
////}
//
//package com.example.ead_mobile_app;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.GridLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
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
//public class MainProductsPageActivity extends AppCompatActivity {
//
//    private GridLayout categoryGridLayout;
//    private final String API_URL = "http://192.168.137.1:2030/api/ProductCategories/active"; // Updated API URL
//    private ExecutorService executorService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_products_page);
//
//        // Set up GridLayout
//        categoryGridLayout = findViewById(R.id.categoryGridLayout);
//        executorService = Executors.newSingleThreadExecutor();
//
//        // Fetch categories from API
//        fetchCategoriesFromApi();
//
//        // Handle window insets for proper padding
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
//            return WindowInsetsCompat.CONSUMED;
//        });
//    }
//
//    // Method to fetch categories from API
//    private void fetchCategoriesFromApi() {
//        executorService.execute(() -> {
//            try {
//                URL url = new URL(API_URL);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//
//                // Check if connection is successful
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
//                    // Parse JSON response and extract categories
//                    parseCategoriesFromResponse(response.toString());
//                } else {
//                    showToast("Failed to fetch data from API");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }
//
//    // Parse the JSON response and extract categories
//    private void parseCategoriesFromResponse(String response) {
//        try {
//            // Convert response to JSONArray
//            JSONArray jsonArray = new JSONArray(response);
//
//            // Loop through each category in the response
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject categoryObject = jsonArray.getJSONObject(i);
//                String categoryName = categoryObject.getString("categoryName"); // Extract category name
//
//                // Update UI to add categories
//                addCategoryCard(categoryName);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast("Failed to parse data");
//        }
//    }
//
//    // Method to dynamically add category cards
//    private void addCategoryCard(String categoryName) {
//        new Handler(Looper.getMainLooper()).post(() -> {
//            TextView categoryCard = new TextView(this);
//            categoryCard.setText(categoryName);
//            categoryCard.setBackgroundResource(R.drawable.category_card_background);
//            categoryCard.setGravity(Gravity.CENTER);
//            categoryCard.setPadding(32, 32, 32, 32);
//            categoryCard.setLayoutParams(new GridLayout.LayoutParams());
//            categoryCard.setTextSize(18);
//
//            // Add category card to GridLayout
//            categoryGridLayout.addView(categoryCard);
//        });
//    }
//
//    // Show toast message
//    private void showToast(String message) {
//        new Handler(Looper.getMainLooper()).post(() ->
//                Toast.makeText(MainProductsPageActivity.this, message, Toast.LENGTH_SHORT).show()
//        );
//    }
//}

package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainProductsPageActivity extends AppCompatActivity {

    private GridLayout categoryGridLayout;
    private final String API_URL = "http://192.168.137.1:2030/api/ProductCategories/active"; // Updated API URL
    private ExecutorService executorService;
    private String customerEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_products_page);
        customerEmail = getIntent().getStringExtra("customerEmail");
        // Set up GridLayout
        categoryGridLayout = findViewById(R.id.categoryGridLayout);
        executorService = Executors.newSingleThreadExecutor();

        // Fetch categories from API
        fetchCategoriesFromApi();

        // Handle window insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    // Method to fetch categories from API
    private void fetchCategoriesFromApi() {
        executorService.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Check if connection is successful
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON response and extract categories
                    parseCategoriesFromResponse(response.toString());
                } else {
                    showToast("Failed to fetch data from API");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    // Parse the JSON response and extract categories
    private void parseCategoriesFromResponse(String response) {
        try {
            // Convert response to JSONArray
            JSONArray jsonArray = new JSONArray(response);

            // Loop through each category in the response
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject categoryObject = jsonArray.getJSONObject(i);
                String categoryName = categoryObject.getString("categoryName"); // Extract category name

                // Update UI to add categories
                addCategoryCard(categoryName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse data");
        }
    }

    // Method to dynamically add category cards
    private void addCategoryCard(String categoryName) {
        new Handler(Looper.getMainLooper()).post(() -> {
            TextView categoryCard = new TextView(this);
            categoryCard.setText(categoryName);
            categoryCard.setBackgroundResource(R.drawable.category_card_background);
            categoryCard.setGravity(Gravity.CENTER);
            categoryCard.setPadding(32, 32, 32, 32);
            categoryCard.setLayoutParams(new GridLayout.LayoutParams());
            categoryCard.setTextSize(18);

            // Set click listener to pass the selected category
            categoryCard.setOnClickListener(v -> {
                // Intent to navigate to ItemListPageActivity
                Intent intent = new Intent(MainProductsPageActivity.this, ItemListPageActivity.class);
                intent.putExtra("category", categoryName); // Pass the selected category
                intent.putExtra("customerEmail", customerEmail);
                startActivity(intent); // Start ItemListPageActivity
            });

            // Add category card to GridLayout
            categoryGridLayout.addView(categoryCard);
        });
    }

    // Show toast message
    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(MainProductsPageActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
