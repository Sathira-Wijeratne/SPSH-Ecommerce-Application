
package com.example.ead_mobile_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewCustomerProfileActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, roleTextView;  // Updated from EditText to TextView
    private Button modifyAccountButton, deactivateAccountButton;
    private ExecutorService executorService;
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_customer_profile);

        // Initialize UI elements
        nameTextView = findViewById(R.id.nameTextView);  // Updated ID
        emailTextView = findViewById(R.id.emailTextView);  // Updated ID
        roleTextView = findViewById(R.id.roleTextView);  // Updated ID
        modifyAccountButton = findViewById(R.id.modifyAccountButton);
        deactivateAccountButton = findViewById(R.id.deactivateAccountButton);

        // Get customer email from intent
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Fetch and display customer details
        fetchCustomerDetails(customerEmail);

        // Add button listeners
        modifyAccountButton.setOnClickListener(v -> {
            // Code for modifying account
            Toast.makeText(ViewCustomerProfileActivity.this, "Modify Account Clicked", Toast.LENGTH_SHORT).show();
        });

        deactivateAccountButton.setOnClickListener(v -> showDeactivateConfirmation());
    }

    private void fetchCustomerDetails(String email) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Users/" + email;
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

                    // Parse response and update UI
                    parseAndDisplayResponse(response.toString());
                } else {
                    showToast("Failed to fetch customer details");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void deactivateAccount(String email) {
        executorService.execute(() -> {
            try {
                // Set up the DELETE request to the server
                String urlString = "http://192.168.137.1:2030/api/Users/" + email;
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");

                // Get response from the server
                int responseCode = urlConnection.getResponseCode();

                // Update the UI based on the response
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(ViewCustomerProfileActivity.this, "Account Deactivated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewCustomerProfileActivity.this, "Failed to Deactivate Account", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> Toast.makeText(ViewCustomerProfileActivity.this, "Error in Account Deactivation", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showDeactivateConfirmation() {
        String email = emailTextView.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ViewCustomerProfileActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deactivation")
                .setMessage("Are you sure you want to deactivate your account?")
                .setPositiveButton("Yes", (dialog, which) -> deactivateAccount(email))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void parseAndDisplayResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String name = jsonResponse.getString("name");
            String email = jsonResponse.getString("email");
            String role = jsonResponse.getString("role");

            runOnUiThread(() -> {
                nameTextView.setText("Name: " + name);  // Updated from EditText to TextView
                emailTextView.setText("Email: " + email);  // Updated from EditText to TextView
                roleTextView.setText("Role: " + role);  // Updated from EditText to TextView
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Failed to parse customer details");
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ViewCustomerProfileActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
