package com.example.ead_mobile_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModifyUserAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.modify_user_account);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        EditText emailEditText = findViewById(R.id.modifyEmailEditText);
        EditText passwordEditText = findViewById(R.id.modifyPasswordEditText);
        Button saveChangesButton = findViewById(R.id.saveChangesButton);
        Button deactivateButton = findViewById(R.id.deactivateButton);

        saveChangesButton.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString().trim();
            String newPassword = passwordEditText.getText().toString().trim();

            if(validateInputs(newEmail,newPassword)){
                saveChanges(newEmail,newPassword);
            }else{
                Toast.makeText(ModifyUserAccountActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validateInputs(String email, String password){
        return !email.isEmpty() && !password.isEmpty();
    }

    private void saveChanges(String email, String password){
        Toast.makeText(ModifyUserAccountActivity.this,"Account Details Updated",Toast.LENGTH_SHORT).show();
    }

    private void deactivateAccount(){
        Toast.makeText(ModifyUserAccountActivity.this,"Account Deactivated",Toast.LENGTH_SHORT).show();
    }

    private void showDeactivateConfirmation(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deactivation")
                .setMessage("Are you sure you want to deactivate your account?")
                .setPositiveButton("Yes",(dialog,which) -> deactivateAccount())
                .setNegativeButton("No",(dialog,which) -> dialog.dismiss())
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