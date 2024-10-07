package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AllActivityPageActivity extends AppCompatActivity {

    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.all_activity_page);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the email passed from the login activity
        Intent intent = getIntent();
        customerEmail = intent.getStringExtra("customerEmail");

        // Find the View Profile layout
        LinearLayout viewProfileLayout = findViewById(R.id.editProfile);
        LinearLayout shoppingLayout = findViewById(R.id.settings);
        LinearLayout trackOrdersLayout = findViewById(R.id.myStats);
        LinearLayout notificationsLayout = findViewById(R.id.inviteFriend);
        LinearLayout vendorListLayout = findViewById(R.id.vendorList);

        // Set an OnClickListener for "View Profile" to navigate to ViewCustomerProfileActivity
        viewProfileLayout.setOnClickListener(v -> {
            // Create an Intent to navigate to ViewCustomerProfileActivity and pass the email
            Intent profileIntent = new Intent(AllActivityPageActivity.this, ViewCustomerProfileActivity.class);
            profileIntent.putExtra("customerEmail", customerEmail); // Pass the email to the next activity
            startActivity(profileIntent);
        });

        // Set an OnClickListener for "Shopping" to navigate to MainProductsPageActivity
        shoppingLayout.setOnClickListener(v -> {
            Intent shoppingIntent = new Intent(AllActivityPageActivity.this, MainProductsPageActivity.class);
            shoppingIntent.putExtra("customerEmail", customerEmail); // Pass the email to the next activity
            startActivity(shoppingIntent);
        });

        // Set an OnClickListener for "Track my Orders" to navigate to TrackOrdersActivity
        trackOrdersLayout.setOnClickListener(v -> {
            Intent trackOrdersIntent = new Intent(AllActivityPageActivity.this, TrackOrdersActivity.class);
            trackOrdersIntent.putExtra("customerEmail", customerEmail); // Pass the email to the next activity
            startActivity(trackOrdersIntent);
        });

        // Set an OnClickListener for "Order History" to navigate to OrderHistoryActivity
        notificationsLayout.setOnClickListener(v -> {
            Intent orderHistoryIntent = new Intent(AllActivityPageActivity.this, NotificationPageActivity.class);
            orderHistoryIntent.putExtra("customerEmail", customerEmail); // Pass the email to the next activity
            startActivity(orderHistoryIntent);
        });

        vendorListLayout.setOnClickListener(v -> {
            Intent orderHistoryIntent = new Intent(AllActivityPageActivity.this, VendorListActivity.class);
            orderHistoryIntent.putExtra("customerEmail", customerEmail); // Pass the email to the next activity
            startActivity(orderHistoryIntent);
        });
    }
}
