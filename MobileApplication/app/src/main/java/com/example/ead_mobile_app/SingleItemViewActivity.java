package com.example.ead_mobile_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
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

    private LinearLayout itemContainer, reviewContainer;
    private RatingBar ratingBar;
    private EditText commentInput;
    private Button addReviewButton, submitReviewButton;
    private ExecutorService executorService;
    private String productId;
    private String customerEmail;  // User's email from login
    private String productName;
    private int productPrice;
    private String vendorEmail;
    private String imageBase64;  // For storing image data
    private boolean isRated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_view);

        // Initialize views
        itemContainer = findViewById(R.id.itemContainer);
        ratingBar = findViewById(R.id.reviewRatingBar);
        reviewContainer = findViewById(R.id.reviewContainer); // Review section container
        commentInput = findViewById(R.id.commentInput);
        addReviewButton = findViewById(R.id.addReviewButton);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        executorService = Executors.newSingleThreadExecutor();

        // Initially hide the review section
        reviewContainer.setVisibility(LinearLayout.GONE);

        // Get the product ID and customer email passed from the previous activity
        productId = getIntent().getStringExtra("productId");
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Fetch and display the product details
        fetchProductDetails(productId);

        // Set Add Review Button to show the review section
        addReviewButton.setOnClickListener(v -> reviewContainer.setVisibility(LinearLayout.VISIBLE));

        // Handle submit review functionality
        submitReviewButton.setOnClickListener(v -> submitReview());

        // Add to Cart button listener
        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> addToCart());

        // View Cart button listener
        Button viewCartButton = findViewById(R.id.viewCartButton);
        viewCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(SingleItemViewActivity.this, CartDetailsActivity.class);
            intent.putExtra("customerEmail", customerEmail); // Pass customer email to CartDetailsActivity
            startActivity(intent);
        });
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

            // Fetch review details.
            fetchReviewDetails(customerEmail, vendorEmail);

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
        priceTextView.setText("Price: Rs. " + price);

        // Set product stock
        TextView stockTextView = findViewById(R.id.productStock);
        stockTextView.setText("Available: " + stock);

        // Set product vendor email
        TextView vendorEmailTextView = findViewById(R.id.productVendorEmail);
        vendorEmailTextView.setText("Vendor: " + vendorEmail);
    }

    private void addToCart() {
        // Fetch the quantity input by the user
        EditText quantityInput = findViewById(R.id.quantityInput);
        int quantity = 1;  // Default value

        try {
            quantity = Integer.parseInt(quantityInput.getText().toString());
        } catch (NumberFormatException e) {
            showToast("Please enter a valid quantity.");
            return;
        }

        int finalQuantity = quantity;

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
                cartData.put("productQty", finalQuantity);  // Use user-entered quantity
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


    private void submitReview() {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Rates";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(!isRated){
                    connection.setRequestMethod("POST");
                }else {
                    connection.setRequestMethod("PUT");
                }
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Prepare review data in JSON format
                JSONObject reviewData = new JSONObject();
                reviewData.put("id", "");
                reviewData.put("vendorEmail", vendorEmail);
                reviewData.put("customerEmail", customerEmail);
                reviewData.put("stars", ratingBar.getRating());
                reviewData.put("comment", commentInput.getText().toString());

                // Send the review data to the server
                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(reviewData.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    showToast("Review submitted successfully");
                } else if(responseCode == 204){
                    showToast("Review updated successfully");
                }
                else {
                    showToast("Failed to submit review");
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

    private void fetchReviewDetails(String customerEmail, String vendorEmail) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Rates/" + customerEmail + "/" + vendorEmail;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    isRated = true;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    processReviewResponse(response.toString());
                } else if(responseCode == 204){
                    isRated = false;
                }
                else {
                    showToast("Failed to fetch product details");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void processReviewResponse(String response) {
        try {
            JSONObject review = new JSONObject(response);
            commentInput.setText(review.getString("comment"));
            ratingBar.setRating(review.getInt("stars"));

            // Update the UI with product details
//            new Handler(Looper.getMainLooper()).post(() -> displayProductDetails(productName, productPrice, stock, vendorEmail, bitmap));

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse product details");
        }
    }
}
