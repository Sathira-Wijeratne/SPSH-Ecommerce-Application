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
//public class NotificationPageActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.notification_page);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

package com.example.ead_mobile_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationPageActivity extends AppCompatActivity {

    private LinearLayout notificationContainer;
    private ExecutorService executorService;
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notification_page);

        notificationContainer = findViewById(R.id.notificationContainer);
        executorService = Executors.newSingleThreadExecutor();

        // Get the customer email from the intent passed from the login page
        customerEmail = getIntent().getStringExtra("customerEmail");

        // Ensure the email is available before proceeding
        if (customerEmail != null && !customerEmail.isEmpty()) {
            fetchNotifications(customerEmail);
        } else {
            showToast("Failed to retrieve customer email.");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchNotifications(String customerEmail) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/CustomerNotifications/get-by-email/" + customerEmail;
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
                    // Process response
                    runOnUiThread(() -> parseNotifications(response.toString()));
                } else {
                    showToast("Failed to fetch notifications.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void parseNotifications(String response) {
        try {
            JSONArray notificationsArray = new JSONArray(response);
            for (int i = 0; i < notificationsArray.length(); i++) {
                JSONObject notification = notificationsArray.getJSONObject(i);
                addNotificationCard(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNotificationCard(JSONObject notification) {
        try {
            View notificationCard = LayoutInflater.from(this).inflate(R.layout.notification_card, notificationContainer, false);

            TextView notificationMessage = notificationCard.findViewById(R.id.notificationMessage);
            TextView notificationStatus = notificationCard.findViewById(R.id.notificationStatus);
            TextView notificationTime = notificationCard.findViewById(R.id.notificationTime);

            boolean markAsRead = notification.getBoolean("markAsRead");
            String message = notification.getString("notificationMessage");
            String notificationId = notification.getString("orderId");

            notificationMessage.setText(message);

            if (!markAsRead) {
                notificationMessage.setTypeface(null, android.graphics.Typeface.BOLD);
                notificationStatus.setText("Unread");
            } else {
                notificationStatus.setText("");
            }

            String timestamp = notification.optString("time", "Just Now");
            notificationTime.setText(timestamp);

            // Add click listener to handle marking notification as read
            notificationCard.setOnClickListener(v -> {
                if (!markAsRead) {
                    markNotificationAsRead(notificationId, notificationStatus, notificationMessage);
                }
            });

            notificationContainer.addView(notificationCard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markNotificationAsRead(String notificationId, TextView notificationStatus, TextView notificationMessage) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/CustomerNotifications/mark-as-read/" + notificationId + "?MarkAsRead=true";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        notificationStatus.setText("");  // Remove 'Unread'
                        notificationMessage.setTypeface(null, android.graphics.Typeface.NORMAL);  // Change to normal text
                        showToast("Notification marked as read.");
                    });
                } else {
                    showToast("Failed to mark notification as read.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(NotificationPageActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
