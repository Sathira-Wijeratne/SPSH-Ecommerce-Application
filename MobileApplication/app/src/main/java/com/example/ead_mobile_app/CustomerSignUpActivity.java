package com.example.ead_mobile_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomerSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_sign_up);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button signUpButton = findViewById(R.id.signupButton);
        Button loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(CustomerSignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(confirmPassword)){
                Toast.makeText(CustomerSignUpActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(CustomerSignUpActivity.this,"Sign-Up Successful",Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(v->{
            Toast.makeText(CustomerSignUpActivity.this, "Redirecting to Login",Toast.LENGTH_SHORT).show();
        });
    }

}