//package com.example.ead_mobile_app;
//
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class EditCustomerProfileActivity extends AppCompatActivity {
//
//    private TextView customerEmailTextView; // TextView to display the email
//    private String customerEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_customer_profile);
//
//        // Initialize UI elements
//        customerEmailTextView = findViewById(R.id.customerEmailTextView);
//
//        // Get customer email from intent
//        customerEmail = getIntent().getStringExtra("customerEmail");
//
//        // Check if the email was passed and display it
//        if (customerEmail != null && !customerEmail.isEmpty()) {
//            customerEmailTextView.setText("Email: " + customerEmail);
//        } else {
//            Toast.makeText(this, "No email provided", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
package com.example.ead_mobile_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditCustomerProfileActivity extends AppCompatActivity {

    private TextView customerEmailTextView;
    private EditText nameEditText, roleEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton, submitButton, cancelButton;
    private String customerEmail;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_profile);

        customerEmailTextView = findViewById(R.id.customerEmailTextView);
        nameEditText = findViewById(R.id.nameEditText);
        roleEditText = findViewById(R.id.roleEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Get customer email from intent
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Show customer email and disable role/email fields
        if (customerEmail != null && !customerEmail.isEmpty()) {
            customerEmailTextView.setText("Email: " + customerEmail);
        }

        // Fetch customer details from GET API (use the executor service)
        fetchCustomerDetails();

        // When Change Password button is clicked
        changePasswordButton.setOnClickListener(v -> {
            newPasswordEditText.setVisibility(View.VISIBLE);
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        });

        // On Submit button click
        submitButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                // Call the PUT API to update the profile
                updateCustomerProfile(newPassword);
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

        // On Cancel button click
        cancelButton.setOnClickListener(v -> finish()); // Close activity, navigate back
    }

    private void fetchCustomerDetails() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                URL url = new URL("http://192.168.137.1:2030/api/Users/" + customerEmail);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {
                    // Read InputStream and convert to String
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the response string into a JSONObject
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    // Update UI with name and role on the main thread
                    runOnUiThread(() -> {
                        try {
                            nameEditText.setText(jsonResponse.getString("name"));
                            roleEditText.setText(jsonResponse.getString("role"));
                        } catch (JSONException e) {
                            e.printStackTrace();  // Handle the JSON exception
                            runOnUiThread(() -> Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show());
                        }
                    });
                }

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(EditCustomerProfileActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateCustomerProfile(String newPassword) {
        executorService.execute(() -> {
            try {
                URL url = new URL("http://192.168.137.1:2030/api/Users/" + customerEmail);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject requestData = new JSONObject();
                requestData.put("name", nameEditText.getText().toString());
                requestData.put("role", roleEditText.getText().toString());
                requestData.put("email", customerEmail);
                requestData.put("id","");
                requestData.put("activated",true);
                if (!newPassword.isEmpty()) {
                    requestData.put("password", newPassword);
                }

                OutputStream os = connection.getOutputStream();
                os.write(requestData.toString().getBytes());
                os.flush();
                os.close();

                if (connection.getResponseCode() == 200) {
                    // Success message after update
                    runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCustomerProfileActivity.this);
                        builder.setTitle("Success")
                                .setMessage("Profile edited successfully!")
                                .setPositiveButton("Back to AllActivity", (dialog, id) -> finish())
                                .show();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(EditCustomerProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
