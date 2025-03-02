package com.example.group8_bartertrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProviderDash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_dash);

        Button pBtn = findViewById(R.id.pBtn);
        Button postUsedProductsBtn = findViewById(R.id.postButton);

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


    }

}