package com.example.group8_bartertrader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.CRUD.UserCRUD;
import com.example.group8_bartertrader.utils.CredentialsValidator;
import com.google.android.material.snackbar.Snackbar;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn;
    private UserCRUD userCRUD;
    private CredentialsValidator cred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        loadRoleSpin();
        setRegBtn();
        setLogBtn();

        userCRUD = new UserCRUD();
        cred = new CredentialsValidator();
    }

    public void loadRoleSpin() {
        Spinner role = findViewById(R.id.roleSelect);
        String[] items = new String[]{"Select your Role", "Provider", "Receiver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);
    }

    public void setRegBtn() {
        Button regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(this);
    }

    public void setLogBtn() {
        Button logBtn = findViewById(R.id.loginBtn);
        logBtn.setOnClickListener(this);
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void showSnackbar(String msg) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

    protected String getEmail() {
        EditText emailBox = findViewById(R.id.email);
        return emailBox.getText().toString().trim();
    }

    protected String getPassword() {
        EditText passBox = findViewById(R.id.pass);
        return passBox.getText().toString().trim();
    }

    protected String getRole() {
        Spinner roleBox = findViewById(R.id.roleSelect);
        return roleBox.getSelectedItem().toString().trim();
    }

    protected String getFname() {
        EditText fnameBox = findViewById(R.id.fName);
        return fnameBox.getText().toString().trim();
    }

    protected String getLname() {
        EditText lnameBox = findViewById(R.id.lName);
        return lnameBox.getText().toString().trim();
    }

    protected void move2Login() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.post(() -> statusLabel.setText(message.trim()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginBtn) {
            move2Login();
            return;
        }

        String email = getEmail();
        String pass = getPassword();
        String role = getRole();
        String fName = getFname();
        String lName = getLname();

        if (email.isEmpty()) {
            setStatusMessage(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
            showToast(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
            showSnackbar(getResources().getString(R.string.EMPTY_EMAIL_ADDRESS));
        } else if (!cred.isValidEmail(email)) {
            setStatusMessage(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
            showToast(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
            showSnackbar(getResources().getString(R.string.INVALID_EMAIL_ADDRESS));
        } else if (pass.isEmpty()) {
            setStatusMessage(getResources().getString(R.string.EMPTY_PASSWORD));
            showToast(getResources().getString(R.string.EMPTY_PASSWORD));
            showSnackbar(getResources().getString(R.string.EMPTY_PASSWORD));
        } else if (!cred.isValidPass(pass)) {
            String passwordRequirements = "Password must be at least 8 characters long and include a mix of uppercase, lowercase, numbers, and special characters.";
            setStatusMessage(passwordRequirements);
            showToast(passwordRequirements);
            showSnackbar(passwordRequirements);

        } else if (!cred.isValidRole(role) || role.equals("Select your Role")) {
            setStatusMessage(getResources().getString(R.string.INVALID_ROLE));
            showToast(getResources().getString(R.string.INVALID_ROLE));
            showSnackbar(getResources().getString(R.string.INVALID_ROLE));
        } else if (cred.isFnameEmpty(fName)) {
            setStatusMessage(getResources().getString(R.string.EMPTY_FNAME));
            showToast(getResources().getString(R.string.EMPTY_FNAME));
            showSnackbar(getResources().getString(R.string.EMPTY_FNAME));
        } else if (cred.isLnameEmpty(lName)) {
            setStatusMessage(getResources().getString(R.string.EMPTY_LNAME));
            showToast(getResources().getString(R.string.EMPTY_LNAME));
            showSnackbar(getResources().getString(R.string.EMPTY_FNAME));
        } else if (!isNetworkAvailable()) {
            setStatusMessage(getResources().getString(R.string.NO_NETWORK));
            showToast("No internet connection. Please check your network.");
            showSnackbar("No internet connection. Please check your network.");
            return;
        } else {
            setStatusMessage(getResources().getString(R.string.REGISTRATION_SUCCESSFUL));
            userCRUD.registerUser(email, pass, role, fName, lName, new UserCRUD.RegisterCallback() {
                @Override
                public void onSuccess(String message) {
                    showToast(message);
                    move2Login();
                }

                @Override
                public void onFailure(String error) {
                    showToast(error);
                    setStatusMessage(getResources().getString(R.string.REGISTRATION_FAILED));
                }
            });
        }
    }
}
