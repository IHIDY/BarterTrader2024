package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group8_bartertrader.provider.ProviderProductList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderDash extends AppCompatActivity {

    private DatabaseReference productsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_dash);

        Button pBtn = findViewById(R.id.pBtn);
        Button postUsedProductsBtn = findViewById(R.id.postButton);
        Button receivedOffersBtn = findViewById(R.id.button_received_offers);
        Button viewPostProducts = findViewById(R.id.viewPostProducts);

        // Initialize Firebase Authentication
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize Firebase Database Reference
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pBtn.setOnClickListener(v -> {
            Log.d("BTN CLICKED", "P BTN CLICKED");
            Intent intent = new Intent(ProviderDash.this, SettingsActivity.class);
            startActivity(intent);
        });

        postUsedProductsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ProductForm.class);
            startActivity(intent);
        });

        receivedOffersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ReceivedOfferActivity.class);
            startActivity(intent);
        });

        viewPostProducts.setOnClickListener(v -> {
            Intent intent = new Intent(ProviderDash.this, ProviderProductList.class);
            startActivity(intent);
        });
    }
}
