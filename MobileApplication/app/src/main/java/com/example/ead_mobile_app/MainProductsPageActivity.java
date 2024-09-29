//package com.example.ead_mobile_app;
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class MainProductsPageActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.main_products_page);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

package com.example.ead_mobile_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainProductsPageActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<String> categories;
    private final String API_URL = "http://192.168.137.1:2030/api/Products"; // Replace with your actual API URL

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_products_page);

        // Set up RecyclerView
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns

        categories = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();

        // Fetch categories from API
        fetchCategoriesFromApi();

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

    // Parse the JSON response and extract unique categories
    private void parseCategoriesFromResponse(String response) {
        try {
            // Convert response to JSONArray
            JSONArray jsonArray = new JSONArray(response);

            HashSet<String> categorySet = new HashSet<>(); // To hold unique categories

            // Loop through each product in the response
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = jsonArray.getJSONObject(i);
                String category = product.getString("productCategory"); // Extract category
                categorySet.add(category); // Ensure uniqueness with HashSet
            }

            categories.clear();
            categories.addAll(categorySet); // Add unique categories to the list

            // Update UI on the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                categoryAdapter = new CategoryAdapter(categories);
                categoryRecyclerView.setAdapter(categoryAdapter);
            });

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse data");
        }
    }

    // Show toast message
    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(MainProductsPageActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
