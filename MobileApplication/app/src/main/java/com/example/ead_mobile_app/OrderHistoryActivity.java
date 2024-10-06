package com.example.ead_mobile_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private LinearLayout ordersContainer;
    private ExecutorService executorService;
    private String status, customerEmail;
    private TextView statusHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_history);

        ordersContainer = findViewById(R.id.orders_container);
        executorService = Executors.newSingleThreadExecutor();

        status = getIntent().getStringExtra("status");
        customerEmail = getIntent().getStringExtra("customerEmail");
//        orderStatus = getIntent().getStringExtra("status");

        // Find the statusHeading TextView and set the text
        statusHeading = findViewById(R.id.statusHeading);
        if (status != null && !status.isEmpty()) {
            statusHeading.setText("Order Status: " + status);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchOrdersByStatus();
    }

    private void fetchOrdersByStatus() {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Orders/status-customer/" + status + "/" + customerEmail;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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

                    // Parse and display orders
                    parseAndDisplayOrders(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void parseAndDisplayOrders(String response) {
        try {
            JSONArray ordersArray = new JSONArray(response);
            runOnUiThread(() -> {
                for (int i = 0; i < ordersArray.length(); i++) {
                    try {
                        JSONObject order = ordersArray.getJSONObject(i);
                        addOrderCard(order);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrderCard(JSONObject order) {
        try {
            View orderCardView = LayoutInflater.from(this).inflate(R.layout.order_card, ordersContainer, false);

            // Set order details to the card
            TextView orderIdTextView = orderCardView.findViewById(R.id.orderIdTextView);
            TextView productNameTextView = orderCardView.findViewById(R.id.productNameTextView);
            TextView productQuantityTextView = orderCardView.findViewById(R.id.productQuantityTextView);
            LinearLayout buttonsLayout = orderCardView.findViewById(R.id.buttonsLayout);
            Button editButton = orderCardView.findViewById(R.id.editButton);
            Button cancelButton = orderCardView.findViewById(R.id.cancelButton);

            // Set the order data
            orderIdTextView.setText("Order ID: " + order.getString("orderId"));
            productNameTextView.setText("Product: " + order.getString("productName"));
            productQuantityTextView.setText("Quantity: " + order.getString("productQuantity"));

            // If the status is "Processing", show the Edit and Cancel buttons
            if (status.equals("Processing")) {
                buttonsLayout.setVisibility(View.VISIBLE);  // Show the buttons

                // Add actions for Edit and Cancel buttons
                editButton.setOnClickListener(v -> {
                    // Logic to handle edit functionality
                    handleEditOrder(order);
                });

                cancelButton.setOnClickListener(v -> {
                    // Logic to handle cancel functionality
                    handleCancelOrder(order);
                });
            }

            // Add the card to the container
            ordersContainer.addView(orderCardView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditOrder(JSONObject order) {
        // Logic to edit the order
        String orderId = order.optString("orderId");
        // You can implement edit functionality here
        showToast("Edit button clicked for Order ID: " + orderId);
    }

    private void handleCancelOrder(JSONObject order) {
        String orderId = order.optString("orderId");
        // Show the dialog to get the reason from the user
        showReasonDialog(orderId, true);
    }

    private void showReasonDialog(String orderId, boolean isCancel) {
        // Create an EditText for input
        EditText reasonInput = new EditText(this);
        reasonInput.setHint("Enter your reason");

        // Create a dialog
        new AlertDialog.Builder(this)
                .setTitle(isCancel ? "Cancel Order" : "Edit Order")
                .setMessage("Please provide a reason:")
                .setView(reasonInput)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String reason = reasonInput.getText().toString();
                    if (!reason.isEmpty()) {
                        // Call PUT API first to update the note
                        submitReason(orderId, reason, isCancel);
                    } else {
                        // Show an error message if reason is empty
                        showToast("Reason cannot be empty.");
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Call PUT API to update the note
    private void submitReason(String orderId, String reason, boolean isCancel) {
        executorService.execute(() -> {
            try {
                // API URL for the PUT request
                String apiUrl = "http://192.168.137.1:2030/api/Orders/update-note/" + orderId;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // JSON body for the request (reason)
                String jsonBody = "\"" + reason + "\"";  // Example: "This is note v2"

                // Write the reason to the request body
                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(jsonBody.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Successfully updated note, now update the status
                    updateOrderStatus(orderId, isCancel);
                } else {
                    showToast("Failed to update the note.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }


    // Call PATCH API to update the order status
    private void updateOrderStatus(String orderId, boolean isCancel) {
        executorService.execute(() -> {
            try {
                // Determine the status based on whether the action is Cancel or Edit
                String status = isCancel ? "Requested%20to%20cancel" : "Processing";

                String apiUrl = "http://192.168.137.1:2030/api/Orders/manage/" + orderId + "?Status=" + status;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showToast(isCancel ? "Order cancellation requested." : "Order updated.");
                    // Optionally refresh the orders or update the UI
                } else {
                    showToast("Failed to update order status.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }


    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(OrderHistoryActivity.this, message, Toast.LENGTH_SHORT).show());
    }

//    private void showReasonDialog(String orderId, boolean isCancel) {
//        // Create an EditText for input
//        EditText reasonInput = new EditText(this);
//        reasonInput.setHint("Enter your reason");
//
//        // Create a dialog
//        new AlertDialog.Builder(this)
//                .setTitle(isCancel ? "Cancel Order" : "Edit Order")
//                .setMessage("Please provide a reason:")
//                .setView(reasonInput)
//                .setPositiveButton("Submit", (dialog, which) -> {
//                    String reason = reasonInput.getText().toString();
//                    if (!reason.isEmpty()) {
//                        // Call PUT API first
//                        submitReason(orderId, reason, isCancel);
//                    } else {
//                        // Show an error message if reason is empty
//                        showToast("Reason cannot be empty.");
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

    // Call PUT API to update the note
//    private void submitReason(String orderId, String reason, boolean isCancel) {
//        executorService.execute(() -> {
//            try {
//                String apiUrl = "http://192.168.137.1:2030/api/Orders/update-note/" + orderId;
//                URL url = new URL(apiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("PUT");
//                connection.setRequestProperty("Content-Type", "application/json");
//                connection.setDoOutput(true);
//
//                // Write the reason to the request body
//                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
//                outputStream.write(("{\"reason\":\"" + reason + "\"}").getBytes());
//                outputStream.flush();
//                outputStream.close();
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    // Successfully updated note, now update the status
//                    updateOrderStatus(orderId, isCancel);
//                } else {
//                    showToast("Failed to update the note.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }

    // Call PATCH API to update the order status
//    private void updateOrderStatus(String orderId, boolean isCancel) {
//        executorService.execute(() -> {
//            try {
//                // Determine the status based on whether the action is Cancel or Edit
//                String status = isCancel ? "Requested to cancel" : "Processing";
//
//                String apiUrl = "http://192.168.137.1:2030/api/Orders/manage/" + orderId + "?Status=" + status;
//                URL url = new URL(apiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("PATCH");
//                connection.setRequestProperty("Accept", "application/json");
//                connection.connect();
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    showToast(isCancel ? "Order cancellation requested." : "Order updated.");
//                    // Optionally refresh the orders or update the UI
//                } else {
//                    showToast("Failed to update order status.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToast("Error: " + e.getMessage());
//            }
//        });
//    }



}
