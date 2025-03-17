package com.example.group8_bartertrader;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferDetailsActivity extends AppCompatActivity {
    private TextView prodName;
    private TextView targetProdName;
    private TextView status;
    private Button acceptButton;
    private Button declineButton;
    private DatabaseReference offerRef;
    private String offerId;

    @Override
    protected void onCreate(Bundle saveState){
        super.onCreate(saveState);
        setContentView(R.layout.activity_offer_details);

    }
    private void updateOfferStatus(String status){
        if (offerId != null){
            offerRef.child(offerId).child("status").setValue(status).addOnSuccessListener(aVoid->{
                Toast.makeText(this, "Offer" + status, Toast.LENGTH_SHORT).show();
                finish();
            })
                    .addOnFailureListener(e-> Log.e("OfferDetailsActivity", "Status change failed"));
        }
    }
}
