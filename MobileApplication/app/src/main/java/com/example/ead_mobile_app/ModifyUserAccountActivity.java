//package com.example.ead_mobile_app;
//
//import android.app.AlertDialog;
//import android.os.Bundle;
//import android.view.View;
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
//public class ModifyUserAccountActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.modify_user_account);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
//        EditText emailEditText = findViewById(R.id.modifyEmailEditText);
//        EditText passwordEditText = findViewById(R.id.modifyPasswordEditText);
//        Button saveChangesButton = findViewById(R.id.saveChangesButton);
//        Button deactivateButton = findViewById(R.id.deactivateButton);
//
//        saveChangesButton.setOnClickListener(v -> {
//            String newEmail = emailEditText.getText().toString().trim();
//            String newPassword = passwordEditText.getText().toString().trim();
//
//            if(validateInputs(newEmail,newPassword)){
//                saveChanges(newEmail,newPassword);
//            }else{
//                Toast.makeText(ModifyUserAccountActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public boolean validateInputs(String email, String password){
//        return !email.isEmpty() && !password.isEmpty();
//    }
//
//    private void saveChanges(String email, String password){
//        Toast.makeText(ModifyUserAccountActivity.this,"Account Details Updated",Toast.LENGTH_SHORT).show();
//    }
//
//    private void deactivateAccount(){
//        Toast.makeText(ModifyUserAccountActivity.this,"Account Deactivated",Toast.LENGTH_SHORT).show();
//    }
//
//    private void showDeactivateConfirmation(){
//        new AlertDialog.Builder(this)
//                .setTitle("Confirm Deactivation")
//                .setMessage("Are you sure you want to deactivate your account?")
//                .setPositiveButton("Yes",(dialog,which) -> deactivateAccount())
//                .setNegativeButton("No",(dialog,which) -> dialog.dismiss())
//                .create()
//                .show();
//    }
//
//    private void setupUiForImmersiveEdgeToEdge(View view) {
//        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
//            v.setPadding(
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
//                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
//            );
//            return WindowInsetsCompat.CONSUMED;
//        });
//    }
//}

package com.example.ead_mobile_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModifyUserAccountActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button saveChangesButton;
    private Button deactivateButton;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user_account);

        // Initialize UI elements
        emailEditText = findViewById(R.id.modifyEmailEditText);
        passwordEditText = findViewById(R.id.modifyPasswordEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);
        deactivateButton = findViewById(R.id.deactivateButton);

        // Initialize ExecutorService for background tasks
        executorService = Executors.newSingleThreadExecutor();

        saveChangesButton.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString().trim();
            String newPassword = passwordEditText.getText().toString().trim();

            if (validateInputs(newEmail, newPassword)) {
                saveChanges(newEmail, newPassword);
            } else {
                Toast.makeText(ModifyUserAccountActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the listener for the deactivate account button
        deactivateButton.setOnClickListener(v -> showDeactivateConfirmation());
    }

    // Validate email and password inputs
    public boolean validateInputs(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    // Handle account changes (e.g., update email or password)
    private void saveChanges(String email, String password) {
        Toast.makeText(ModifyUserAccountActivity.this, "Account Details Updated", Toast.LENGTH_SHORT).show();
    }

    // Method to make an API request to delete the user's account
    private void deactivateAccount(String email) {
        executorService.execute(() -> {
            try {
                // Set up the DELETE request to the server
                String urlString = "http://192.168.137.1:2030/api/Users/" + email; // API URL with email
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");

                // Get response from the server
                int responseCode = urlConnection.getResponseCode();

                // Update the UI based on the response
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(ModifyUserAccountActivity.this, "Account Deactivated Successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another activity or close the app
                    } else {
                        Toast.makeText(ModifyUserAccountActivity.this, "Failed to Deactivate Account", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> Toast.makeText(ModifyUserAccountActivity.this, "Error in Account Deactivation", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Show confirmation dialog before deactivating the account
    private void showDeactivateConfirmation() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ModifyUserAccountActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deactivation")
                .setMessage("Are you sure you want to deactivate your account?")
                .setPositiveButton("Yes", (dialog, which) -> deactivateAccount(email)) // Call the API if the user confirms
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Dismiss the dialog if the user cancels
                .create()
                .show();
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
