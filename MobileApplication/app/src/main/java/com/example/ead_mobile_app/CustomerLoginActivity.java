
package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ExecutorService executorService;
    private TextView createAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Initialize ExecutorService for background tasks
        executorService = Executors.newSingleThreadExecutor();
        createAccountText = findViewById(R.id.createAccountText);

        // Set an OnClickListener on the "Create Account" text
        createAccountText.setOnClickListener(v -> {
            // Redirect to the Sign-Up page (CustomerSignUpActivity)
            Intent intent = new Intent(CustomerLoginActivity.this, CustomerSignUpActivity.class);
            startActivity(intent);
        });
        // Set OnClickListener for Login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate inputs before proceeding
            if (validateInputs(email, password)) {
                // Call method to log in user
                loginUser(email, password);
            } else {
                Toast.makeText(CustomerLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to validate the form inputs
    private boolean validateInputs(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    // Method to handle the login process using the provided API
    private void loginUser(String email, String password) {
        // Run the login process in a background thread
        executorService.execute(() -> {
            try {
                // API endpoint with email parameter
                String urlString = "http://192.168.137.1:2030/api/Users/" + email;
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // Check if the response code is 200 (OK)
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the API
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse the response JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String retrievedPassword = jsonResponse.getString("password"); // Get the password from the response
                    String role = jsonResponse.getString("role"); // Get the role from the response
                    boolean isActivated = jsonResponse.getBoolean("activated"); // Get the 'activated' status from the response

                    // Check if the password matches, the user is activated, and the role is "Customer"
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    if (retrievedPassword.equals(password) && isActivated && role.equals("Customer")) {
                        // Login successful
                        mainHandler.post(() -> {
                            Toast.makeText(CustomerLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            // Redirect to MainProductsPageActivity
                            Intent intent = new Intent(CustomerLoginActivity.this, AllActivityPageActivity.class);
                            intent.putExtra("customerEmail", email);  // Pass the logged-in email
                            startActivity(intent);
                            finish();
                        });
                    } else if (!isActivated) {
                        // User is not activated
                        mainHandler.post(() -> Toast.makeText(CustomerLoginActivity.this, "Account not activated", Toast.LENGTH_SHORT).show());
                    } else if (!role.equals("Customer")) {
                        // User is not a customer
                        mainHandler.post(() -> Toast.makeText(CustomerLoginActivity.this, "Access denied: Not a customer", Toast.LENGTH_SHORT).show());
                    } else {
                        // Password mismatch
                        mainHandler.post(() -> Toast.makeText(CustomerLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // User not found or other error
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> Toast.makeText(CustomerLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle any errors during the API call
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> Toast.makeText(CustomerLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void setupUiForImmersiveEdgeToEdge(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
