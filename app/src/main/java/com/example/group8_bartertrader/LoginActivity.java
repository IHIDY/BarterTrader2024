package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.CRUD.UserCRUD;
import com.example.group8_bartertrader.utils.CredentialsValidator;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activity for user login with validation and navigation based on user role.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText emailTextView;
    private EditText passwordTextView;
    private UserCRUD userCRUD;
    private CredentialsValidator cred;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /**
     * Initializes the activity, sets up view components, and login button listener.
     * @param savedInstanceState The saved instance state if the activity is being recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.pass);
        userCRUD = new UserCRUD();
        cred = new CredentialsValidator();

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    /**
     * Displays a snackbar with the provided message.
     * @param msg The message to be displayed in the snackbar.
     */
    private void showSnackbar(String msg) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }
    /**
     * Navigates to the dashboard based on user role.
     * @param role The user role which determines the dashboard activity.
     */
    private void navigateToDashboard(String role) {
        Class<?> targetActivity;

        switch (role) {
            case "Provider":
                targetActivity = ProviderDash.class;
                break;
            case "Receiver":
                targetActivity = ReceiverDash.class;
                break;
            default:
                Toast.makeText(this, "Invalid user role.", Toast.LENGTH_SHORT).show();
                return;
        }

        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
    /**
     * Handles user login by validating input and calling the login method.
     */
    private void loginUser() {
        String email = emailTextView.getText().toString();
        String pass = passwordTextView.getText().toString();

        if (email.isEmpty()) {
            showSnackbar(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
        } else if (pass.isEmpty()) {
            showSnackbar("Empty password");
        } else if (!cred.isValidEmail(email)) {
            showSnackbar(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
        } else if (!cred.isValidPass(pass)) {
            String passwordRequirements = "Password must be at least 8 characters long and include a mix of uppercase, lowercase, numbers, and special characters.";
            showSnackbar(passwordRequirements);
        }

        if (!validateInput(email, "Email") || !validateInput(pass, "Password")) {
            return;
        }

        userCRUD.loginUser(email, pass, new UserCRUD.LoginCallback() {
            @Override
            public void onSuccess(String role) {
                navigateToDashboard(role);
            }

            @Override
            public void onFailure(String message) {
//                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates that the input field is not empty.
     * @param input The input to be validated.
     * @param fieldName The name of the field for error messages.
     * @return True if input is valid, false otherwise.
     */
    private boolean validateInput(String input, String fieldName) {
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "Please enter your " + fieldName + "!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
