package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
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
            // Create the card layout
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.HORIZONTAL);
            cardLayout.setPadding(14, 14, 14, 14);
            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

            // Set layout params with increased margins and height to add space between the cards
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200  // Increase the height of the card here (400dp can be adjusted)
            );
            layoutParams.setMargins(14, 14, 14, 14); // Set margins (left, top, right, bottom) for spacing
            cardLayout.setLayoutParams(layoutParams);

            // Left part of the card (Product details)
            LinearLayout productInfoLayout = new LinearLayout(this);
            productInfoLayout.setOrientation(LinearLayout.VERTICAL);
            productInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,  // Make this fill the available height
                    1  // Give this layout more weight so it takes most space
            ));

            // Product Name
            TextView nameTextView = new TextView(this);
            nameTextView.setText(name);
            nameTextView.setTextSize(18);
            nameTextView.setTextColor(getResources().getColor(android.R.color.black));
            productInfoLayout.addView(nameTextView);

            // Product Price
            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: Rs." + price);
            priceTextView.setTextSize(16);
            priceTextView.setTextColor(getResources().getColor(android.R.color.black));
            productInfoLayout.addView(priceTextView);

            // Product Stock
            TextView stockTextView = new TextView(this);
            stockTextView.setText("Available: " + stock);
            stockTextView.setTextSize(14);
            stockTextView.setTextColor(getResources().getColor(android.R.color.black));
            productInfoLayout.addView(stockTextView);

            // Add product info layout to the card
            cardLayout.addView(productInfoLayout);

            // Add "View" Button to the right side of the card
            Button viewButton = new Button(this);
            viewButton.setText("View");
            viewButton.setTextColor(getResources().getColor(android.R.color.white));
            viewButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Light blue background

            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            buttonLayoutParams.setMargins(16, 24, 16, 24); // Add left and right margin to center the button better
            viewButton.setLayoutParams(buttonLayoutParams);

            // Set an OnClickListener for the button
            viewButton.setOnClickListener(v -> {
                // Navigate to SingleItemViewActivity, passing the productId and customerEmail
                Intent intent = new Intent(ItemListPageActivity.this, SingleItemViewActivity.class);
                intent.putExtra("productId", productId);  // Pass the productId to SingleItemViewActivity
                intent.putExtra("customerEmail", customerEmail);  // Pass the customerEmail
                startActivity(intent);
            });

            // Add the View button to the card layout
            cardLayout.addView(viewButton);

            // Add the card to the main item container
            itemContainer.addView(cardLayout);
        });
    }



    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ItemListPageActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
