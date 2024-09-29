//package com.example.ead_mobile_app;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class CustomerSignUpActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
//        setContentView(R.layout.customer_sign_up);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
//        EditText emailEditText = findViewById(R.id.emailEditText);
//        EditText passwordEditText = findViewById(R.id.passwordEditText);
//        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
//        Button signUpButton = findViewById(R.id.signupButton);
//        Button loginButton = findViewById(R.id.loginButton);
//
//        signUpButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString().trim();
//            String password = passwordEditText.getText().toString().trim();
//            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
//
//            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
//                Toast.makeText(CustomerSignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if(!password.equals(confirmPassword)){
//                Toast.makeText(CustomerSignUpActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Toast.makeText(CustomerSignUpActivity.this,"Sign-Up Successful",Toast.LENGTH_SHORT).show();
//        });
//
//        loginButton.setOnClickListener(v->{
//            Toast.makeText(CustomerSignUpActivity.this, "Redirecting to Login",Toast.LENGTH_SHORT).show();
//        });
//    }
//
//}


package com.example.ead_mobile_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomerSignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_sign_up);

        // Initialize EditTexts and Buttons
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        // Set OnClickListener for Sign-Up button
        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Validate fields and passwords
            if (validateSignUpForm(email, password, confirmPassword)) {
                // Call the method to handle sign-up
                signUp(email, password);
            }
        });

        // Set OnClickListener for Login button
        loginButton.setOnClickListener(v ->
                Toast.makeText(CustomerSignUpActivity.this, "Redirecting to Login", Toast.LENGTH_SHORT).show()
        );
    }

    // Method to validate the form inputs
    private boolean validateSignUpForm(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(CustomerSignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(CustomerSignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Separate method to handle the sign-up logic
    private void signUp(String email, String password) {
        new SignUpTask().execute(email, password);
    }

    // AsyncTask to handle the sign-up request
    private class SignUpTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            try {
                // Set up the URL connection
                URL url = new URL("http://192.168.137.1:2030/api/Users"); // API URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Build the JSON payload
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("Id", ""); // Leave Id empty
                jsonParam.put("Name", "Customer"); // Default name or pass user's name if available
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
                return responseCode == HttpURLConnection.HTTP_OK; // Check if the response is 200 OK

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(CustomerSignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                // Optionally, you can navigate to another activity upon success
            } else {
                Toast.makeText(CustomerSignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}