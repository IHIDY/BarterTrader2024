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
/**
 * Activity to display received offers for a provider.
 * Fetches offers from Firebase based on the logged-in provider's email and displays them in a RecyclerView.
 */
public class ReceivedOfferActivity extends AppCompatActivity {

    private RecyclerView receivedOffersRecyclerView;
    private ReceivedOfferAdapter receivedOfferAdapter;
    private List<Offer> receivedOfferList;
    private DatabaseReference offersRef;
    private FirebaseAuth mAuth;

    /**
     * Initializes the activity, sets up RecyclerView, and fetches the received offers.
     */
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
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }else{
            Log.e("ReceivedOfferActivity", "the main layout is not found");
        }

        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Fetches the offers from Firebase based on the provider's email.
     */
    private void fetchReceivedOffers(String providerEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("DEBUG_FIREBASE", "No user logged-in");
            return;
        }
        offersRef.orderByChild("providerEmail").equalTo(providerEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receivedOfferList.clear();
                Log.d("DEBUG_FIREBASE", "Data: " + dataSnapshot.toString());

                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = offerSnapshot.getValue(Offer.class);
                    if (offer != null) {
                        String offerId = offerSnapshot.getKey();
                        offer.setId(offerId);
                        receivedOfferList.add(offer);
                        Log.d("DEBUG_FIREBASE", "Offer added: " + offer.getOfferedItemName() + " Status: " + offer.getStatus());
                    }
                }
                receivedOfferAdapter.notifyDataSetChanged();
                if (receivedOfferList.isEmpty()){
                    Log.w("DEBUG_FIREBASE", "No offers found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReceivedOfferActivity", "loadOffers:onCancelled", databaseError.toException());
                Log.e("DEBUG_FIREBASE", "Firebase error: " + databaseError.getMessage());
                Toast.makeText(ReceivedOfferActivity.this, "Failed to load offers.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
