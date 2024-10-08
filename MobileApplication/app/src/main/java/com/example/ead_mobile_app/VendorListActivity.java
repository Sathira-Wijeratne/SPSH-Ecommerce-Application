package com.example.ead_mobile_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VendorListActivity extends AppCompatActivity {

    private LinearLayout vendorContainer;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vendor_list);

        vendorContainer = findViewById(R.id.vendorContainer);
        executorService = Executors.newSingleThreadExecutor();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchVendors();
    }

    private void fetchVendors() {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Users/get-by-category/Vendor";
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
                    runOnUiThread(() -> parseVendorResponse(response.toString()));
                } else {
                    showToast("Failed to fetch vendors.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void parseVendorResponse(String response) {
        try {
            JSONArray vendorArray = new JSONArray(response);
            for (int i = 0; i < vendorArray.length(); i++) {
                JSONObject vendor = vendorArray.getJSONObject(i);
                fetchRatings(vendor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVendorCard(JSONObject vendor, float avgRate) {
        try {
            View vendorCard = LayoutInflater.from(this).inflate(R.layout.vendor_card, vendorContainer, false);

            RatingBar vendorRatingBar = vendorCard.findViewById(R.id.vendorRatingBar);
            vendorRatingBar.setRating(avgRate);

            vendorCard.findViewById(R.id.vendorRatingBar);
            TextView vendorName = vendorCard.findViewById(R.id.vendorNameTextView);
            vendorName.setText(vendor.getString("name"));

            vendorContainer.addView(vendorCard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(VendorListActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void fetchRatings(JSONObject vendor) {
        executorService.execute(() -> {
            try {
                String apiUrl = "http://192.168.137.1:2030/api/Rates/vendor/" + vendor.getString("email");
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
                    runOnUiThread(() -> parseVendorRatingResponse(response.toString(), vendor));
                } else {
                    showToast("Failed to fetch vendor ratings.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void parseVendorRatingResponse(String response, JSONObject vendor) {
        try {
            JSONArray vendorRatingArray = new JSONArray(response);
            float avgRate = getAvgRate(vendorRatingArray);
            addVendorCard(vendor, avgRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getAvgRate(JSONArray ratingArray) {
        float avgRate = 0;
        try {
            for (int i = 0; i < ratingArray.length(); i++) {
                JSONObject rate = ratingArray.getJSONObject(i);
                avgRate = avgRate + rate.getInt("stars");
            }
            avgRate = avgRate / ratingArray.length();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return avgRate;
    }
}
