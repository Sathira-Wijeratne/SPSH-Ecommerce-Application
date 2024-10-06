package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
    private EditText searchInput;
    private TextView noResultsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_page);

        customerEmail = getIntent().getStringExtra("customerEmail");
        selectedCategory = getIntent().getStringExtra("category");

        itemContainer = findViewById(R.id.itemContainer);
        searchInput = findViewById(R.id.searchInput);
        noResultsFound = new TextView(this);
        noResultsFound.setText("No results found");
        noResultsFound.setTextSize(18);
        noResultsFound.setTextColor(getResources().getColor(android.R.color.black));

        executorService = Executors.newSingleThreadExecutor();

        // Fetch initial items based on category
        fetchItemsByCategory(selectedCategory);

        // Listen for text input changes and trigger search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTerm = charSequence.toString().trim();
                if (!searchTerm.isEmpty()) {
                    searchProducts(searchTerm);  // Perform search
                } else {
                    // If no search term, reload the category items
                    fetchItemsByCategory(selectedCategory);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

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

                    // Call UI update method on the main thread
                    runOnUiThread(() -> processResponse(response.toString()));
                } else {
                    showToast("Failed to fetch items");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void searchProducts(String searchTerm) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Products/search-name/" + searchTerm;
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

                    // Call UI update method on the main thread
                    runOnUiThread(() -> processSearchResponse(response.toString()));
                } else {
                    showToast("Failed to search products");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void processResponse(String response) {
        try {
            JSONArray productsArray = new JSONArray(response);
            itemContainer.removeAllViews();  // Clear previous views
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                String productId = product.getString("productId");
                String name = product.getString("name");
                int price = product.getInt("price");
                int stock = product.getInt("stock");
                addItemCard(productId, name, price, stock);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse items");
        }
    }

    private void processSearchResponse(String response) {
        try {
            JSONArray productsArray = new JSONArray(response);
            itemContainer.removeAllViews();  // Clear previous views
            if (productsArray.length() == 0) {
                // Display "No results found" if search results are empty
                itemContainer.addView(noResultsFound);
            } else {
                // Display search results
                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject product = productsArray.getJSONObject(i);
                    String productId = product.getString("productId");
                    String name = product.getString("name");
                    int price = product.getInt("price");
                    int stock = product.getInt("stock");
                    addItemCard(productId, name, price, stock);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse search results");
        }
    }

    private void addItemCard(String productId, String name, int price, int stock) {
        runOnUiThread(() -> {
            // Create the product card on the main thread
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16);
            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView nameTextView = new TextView(this);
            nameTextView.setText(name);
            nameTextView.setTextSize(18);
            cardLayout.addView(nameTextView);

            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: $" + price);
            priceTextView.setTextSize(16);
            cardLayout.addView(priceTextView);

            TextView stockTextView = new TextView(this);
            stockTextView.setText("Available: " + stock);
            stockTextView.setTextSize(14);
            cardLayout.addView(stockTextView);

            itemContainer.addView(cardLayout);
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ItemListPageActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
