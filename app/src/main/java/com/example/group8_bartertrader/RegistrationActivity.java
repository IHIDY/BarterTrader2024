package com.example.group8_bartertrader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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

    /**
     * Loads the role selection spinner with available options for the user.
     */
    public void loadRoleSpin() {
        Spinner role = findViewById(R.id.roleSelect);
        String[] items = new String[]{"Select your Role", "Provider", "Receiver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);
    }

    /**
     * Sets the registration button's click listener.
     */
    public void setRegBtn() {
        Button regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(this);
    }

    /**
     * Sets the login button's click listener.
     */
    public void setLogBtn() {
        Button logBtn = findViewById(R.id.loginBtn);
        logBtn.setOnClickListener(this);
    }

    /**
     * Displays a toast message with the provided string.
     * @param msg the message to be displayed in the toast
     */
    private void showToast(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        Log.d("showToast", msg);
    }
    /**
     * Displays a snackbar with the provided message.
     * @param msg the message to be displayed in the snackbar
     */
    private void showSnackbar(String msg) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Retrieves the email entered by the user in the corresponding EditText.
     * @return the email as a string
     */
    protected String getEmail() {
        EditText emailBox = findViewById(R.id.email);
        return emailBox.getText().toString().trim();
    }

    /**
     * Retrieves the password entered by the user in the corresponding EditText.
     * @return the password as a string
     */
    protected String getPassword() {
        EditText passBox = findViewById(R.id.pass);
        return passBox.getText().toString().trim();
    }

    /**
     * Retrieves the role selected by the user from the role spinner.
     * @return the selected role as a string
     */
    protected String getRole() {
        Spinner roleBox = findViewById(R.id.roleSelect);
        return roleBox.getSelectedItem().toString().trim();
    }

    /**
     * Retrieves the first name entered by the user in the corresponding EditText.
     * @return the first name as a string
     */
    protected String getFname() {
        EditText fnameBox = findViewById(R.id.fName);
        return fnameBox.getText().toString().trim();
    }
    /**
     * Retrieves the last name entered by the user in the corresponding EditText.
     * @return the last name as a string
     */
    protected String getLname() {
        EditText lnameBox = findViewById(R.id.lName);
        return lnameBox.getText().toString().trim();
    }

    protected void move2Login() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    /**
     * Sets the status message on the UI in the status label text view.
     * @param message the message to be displayed
     */
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.post(() -> statusLabel.setText(message.trim()));
    }
    /**
     * Checks if the device is connected to the internet.
     * @return true if the network is available, false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
    /**
     * Called when a button is clicked. Handles the registration or login button clicks.
     * @param view the clicked view
     */
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
