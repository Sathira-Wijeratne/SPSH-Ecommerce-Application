package com.example.ead_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrackOrdersActivity extends AppCompatActivity {

    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.track_orders);

        customerEmail = getIntent().getStringExtra("customerEmail");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listeners on each status circle
        TextView completedCircle = findViewById(R.id.completedCircle);
        completedCircle.setOnClickListener(v -> navigateToOrderHistory("Completed"));

        TextView deliveredCircle = findViewById(R.id.deliveredCircle);
        deliveredCircle.setOnClickListener(v -> navigateToOrderHistory("Delivered"));

        TextView cancelledCircle = findViewById(R.id.cancelledCircle);
        cancelledCircle.setOnClickListener(v -> navigateToOrderHistory("Cancelled"));

        TextView processingCircle = findViewById(R.id.processingCircle);
        processingCircle.setOnClickListener(v -> navigateToOrderHistory("Processing"));

        TextView requestedToCancelCircle = findViewById(R.id.requestedToCancelCircle);
        requestedToCancelCircle.setOnClickListener(v -> navigateToOrderHistory("Requested to cancel"));
    }

    private void navigateToOrderHistory(String status) {
        Intent intent = new Intent(TrackOrdersActivity.this, OrderHistoryActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("customerEmail", customerEmail);
        startActivity(intent);
    }
}
