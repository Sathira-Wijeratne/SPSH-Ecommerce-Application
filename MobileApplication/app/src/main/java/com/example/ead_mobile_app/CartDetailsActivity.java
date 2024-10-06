
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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartDetailsActivity extends AppCompatActivity {

    private LinearLayout cartContainer;
    private ExecutorService executorService;
    private String customerEmail;
    private List<JSONObject> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_details);

        // Initialize UI elements
        cartContainer = findViewById(R.id.main);
        executorService = Executors.newSingleThreadExecutor();

        // Get customer email from intent
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Fetch cart items
        cartItems = new ArrayList<>();
        fetchCartItems(customerEmail);
    }

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

    private void processCartResponse(String response) {
        try {
            JSONArray cartArray = new JSONArray(response);
            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject cartItem = cartArray.getJSONObject(i);
                cartItems.add(cartItem);  // Add the item to the list for future reference
                String productId = cartItem.getString("productId");
                String productName = cartItem.getString("productName");
                int productPrice = cartItem.getInt("productPrice");
                int productQty = cartItem.getInt("productQty");
                String imageBase64 = cartItem.getString("imageBase64");

                Bitmap productImage = decodeBase64Image(imageBase64);
                addCartItemCard(productId, productName, productPrice, productQty, productImage);
            }
            addPayNowButton();

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Failed to parse cart items");
        }
    }

    private void addCartItemCard(String productId, String productName, int productPrice, int productQty, Bitmap productImage) {
        new Handler(Looper.getMainLooper()).post(() -> {
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16);
            cardLayout.setGravity(Gravity.CENTER_VERTICAL);
            cardLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            ImageView productImageView = new ImageView(this);
            productImageView.setImageBitmap(productImage);
            productImageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            cardLayout.addView(productImageView);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(productName);
            nameTextView.setTextSize(18);
            cardLayout.addView(nameTextView);

            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: $" + productPrice);
            priceTextView.setTextSize(16);
            cardLayout.addView(priceTextView);

            TextView qtyTextView = new TextView(this);
            qtyTextView.setText("Quantity: " + productQty);
            qtyTextView.setTextSize(14);
            cardLayout.addView(qtyTextView);

            cartContainer.addView(cardLayout);
        });
    }

    private void addPayNowButton() {
        new Handler(Looper.getMainLooper()).post(() -> {
            Button payNowButton = new Button(this);
            payNowButton.setText("Pay Now");
            payNowButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            payNowButton.setOnClickListener(v -> processPayment());
            cartContainer.addView(payNowButton);
        });
    }

    private void processPayment() {
        executorService.execute(() -> {
            try {
                // Step 1: Fetch the next order ID using GET API
                String apiUrl = "http://192.168.137.1:2030/api/Orders/get-next-oid";
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

                    // Parse the response to get the newOid value
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String orderId = jsonResponse.getString("newOid");

                    // Step 2: Make the POST request to /api/Orders
                    submitOrder(orderId);
                } else {
                    showToast("Failed to generate order ID");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }


    private void submitOrder(String orderId) {
        executorService.execute(() -> {
            try {
                // POST API to submit the order
                String apiUrl = "http://192.168.137.1:2030/api/Orders";

                for (JSONObject cartItem : cartItems) {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Create a single order item JSON object
                    JSONObject orderItem = new JSONObject();
                    orderItem.put("id","");
                    orderItem.put("orderId", orderId);
                    orderItem.put("productId", cartItem.getString("productId"));
                    orderItem.put("productName", cartItem.getString("productName"));
                    orderItem.put("vendorEmail", cartItem.getString("vendorEmail"));
                    orderItem.put("productQuantity", cartItem.getInt("productQty"));
                    orderItem.put("productUnitPrice", cartItem.getInt("productPrice"));
                    orderItem.put("customerEmail", customerEmail);
                    orderItem.put("status", "Processing");  // Assuming "Processing" as the status
                    orderItem.put("note", "");  // Additional notes can be added here
                    orderItem.put("imageBase64", cartItem.getString("imageBase64"));

                    // Send the data to the server
                    BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                    outputStream.write(orderItem.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        showToast("Item for product " + cartItem.getString("productName") + " ordered successfully.");
                    } else {
                        showToast("Failed to submit order for product " + cartItem.getString("productName"));
                    }
                }

                // After submitting all products, show a success message with the order ID
                showToast("Order CREATED Successfully. Order ID: " + orderId);

            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }



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


    private void showOrderCreatedToast(String orderId) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(CartDetailsActivity.this, "Order CREATED Successfully. Order ID: " + orderId, Toast.LENGTH_LONG).show()
        );
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(CartDetailsActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }
}
