package com.example.group8_bartertrader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ReceivedOfferAdapter;
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

public class ReceivedOfferActivity extends AppCompatActivity {

    private RecyclerView receivedOffersRecyclerView;
    private ReceivedOfferAdapter receivedOfferAdapter;
    private List<Offer> receivedOfferList;
    private DatabaseReference offersRef;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_received_offers);

        Button backButton = findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String providerEmail = currentUser.getEmail();
        offersRef = FirebaseDatabase.getInstance().getReference("Offers");

        receivedOffersRecyclerView = findViewById(R.id.receivedOffersRecyclerView);
        receivedOffersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        receivedOfferList = new ArrayList<>();
        receivedOfferAdapter = new ReceivedOfferAdapter(receivedOfferList, this);
        receivedOffersRecyclerView.setAdapter(receivedOfferAdapter);

        fetchReceivedOffers(providerEmail);

        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }else{
            Log.e("ReceivedOfferActivity", "the main layout is not found");
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void fetchReceivedOffers(String providerEmail) {
        offersRef.orderByChild("providerEmail").equalTo(providerEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receivedOfferList.clear();

                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = offerSnapshot.getValue(Offer.class);
                    if (offer != null) {
                        receivedOfferList.add(offer);
                    }
                }
                receivedOfferAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReceivedOfferActivity", "loadOffers:onCancelled", databaseError.toException());
                Toast.makeText(ReceivedOfferActivity.this, "Failed to load offers.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
