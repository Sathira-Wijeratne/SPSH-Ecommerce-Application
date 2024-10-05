//package com.example.ead_mobile_app;
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class EditCustomerProfileActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.edit_customer_profile);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

package com.example.ead_mobile_app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditCustomerProfileActivity extends AppCompatActivity {

    private TextView customerEmailTextView; // TextView to display the email
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_profile);

        // Initialize UI elements
        customerEmailTextView = findViewById(R.id.customerEmailTextView);

        // Get customer email from intent
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Check if the email was passed and display it
        if (customerEmail != null && !customerEmail.isEmpty()) {
            customerEmailTextView.setText("Email: " + customerEmail);
        } else {
            Toast.makeText(this, "No email provided", Toast.LENGTH_SHORT).show();
        }
    }
}
