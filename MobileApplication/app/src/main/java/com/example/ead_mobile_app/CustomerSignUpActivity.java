
package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerSignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText,nameEditText;
    private Button signUpButton, loginButton;
    private ExecutorService executorService;
    private CheckBox termsCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_sign_up);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        nameEditText = findViewById(R.id.nameEditText);
        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Set OnClickListener for Sign-Up button
        signUpButton.setOnClickListener(v -> {
            if (validateSignUpForm()) {
                // If validation succeeds, call the method to collect inputs and sign up
                collectAndSignUp();
            }
        });

        // Set OnClickListener for Login button
        loginButton.setOnClickListener(v -> {
            // Redirect to the login page when the Login button is clicked
            Intent intent = new Intent(CustomerSignUpActivity.this, CustomerLoginActivity.class);
            startActivity(intent);
        });
    }

    // Method to validate the form inputs
    private boolean validateSignUpForm() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(CustomerSignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(CustomerSignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(CustomerSignUpActivity.this, "Please agree to the terms and conditions.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // New method to collect user inputs, create the payload, and perform the API call
    private void collectAndSignUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Pass the user inputs to the method that sends the API request
        sendSignUpRequest(email, password);
    }

    // Method to handle the sign-up request using ExecutorService
    private void sendSignUpRequest(String email, String password) {
        executorService.execute(() -> {
            try {
                // Set up the URL connection
                URL url = new URL("http://192.168.137.1:2030/api/Users"); // API URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Build the JSON payload
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("Id", "");
                jsonParam.put("Name",nameEditText ); // Default name or pass user's name if available
                jsonParam.put("Role", "Customer"); // Default role as 'Customer'
                jsonParam.put("Email", email);
                jsonParam.put("Password", password);
                jsonParam.put("Activated", false); // Set as false initially

                // Write the JSON payload to the output stream
                OutputStream os = urlConnection.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                // Get response from the server
                int responseCode = urlConnection.getResponseCode();

                // Update UI on the main thread based on response
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    if (responseCode == 201) {
                        // Sign-up successful, show toast
                        Toast.makeText(CustomerSignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        // Redirect to the login page after successful sign-up
                        Intent intent = new Intent(CustomerSignUpActivity.this, CustomerLoginActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close the sign-up activity so the user cannot go back to it
                    } else {
                        Toast.makeText(CustomerSignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // Update UI on the main thread for failure
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    Toast.makeText(CustomerSignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
