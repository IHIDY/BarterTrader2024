package com.example.group8_bartertrader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.OfferAdapter;
import com.example.group8_bartertrader.model.Offer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOffersActivity extends AppCompatActivity {

    private RecyclerView offerRecyclerView;
    private OfferAdapter offerAdapter;
    private List<Offer> offerList;
    private DatabaseReference offersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_offers);

        Button settingsBtn = findViewById(R.id.recsettingbutton);

        // Initialize the Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userEmail = currentUser.getEmail().replace(".", "_");
        offersRef = FirebaseDatabase.getInstance().getReference("Offers").child(userEmail);

        // Initailize the RecyclerView
        offerRecyclerView = findViewById(R.id.productRecyclerView);
        offerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        offerList = new ArrayList<>();
        offerAdapter = new OfferAdapter(offerList);
        offerRecyclerView.setAdapter(offerAdapter);

        // Fetch the Offers
        fetchOffersFromFirebase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the Go to Settings Button
        settingsBtn.setOnClickListener(v -> {
            Log.d("BTN CLICKED", "SETTINGS BUTTON CLICKED");
            Intent intent = new Intent(MyOffersActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void fetchOffersFromFirebase() {
        offersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offerList.clear();
                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = offerSnapshot.getValue(Offer.class);
                    if (offer != null) {
                        Log.d("Offer fetched", "ID: " + offer.getId() + ", Status: " + offer.getStatus());
                        offerList.add(offer);
                    }
                }
                offerAdapter.notifyDataSetChanged(); // 更新 UI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MyOffersActivity", "loadOffers:onCancelled", databaseError.toException());
                Toast.makeText(MyOffersActivity.this, "Failed to load offers.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
