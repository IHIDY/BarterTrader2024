package com.example.group8_bartertrader;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Padding settings for the UI elements
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //checks if the user is logged in before fetching
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //get the users unique id
            fetchData(userId); //fetch the users data from Firebase
        } else{
            Log.d("Firebase", "No user is logged in");
        }
    }

    //Method to fetch the data from Firebase using the userId
    //Retrieves firstName, lastName, email, and role
    //Check the logCat for data verification
    private void fetchData(String userId){
        //references the 'Users' in Firebase using their Id
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //get the users data and create a new user using the user class
                    RegistrationActivity.User user = snapshot.getValue(RegistrationActivity.User.class);
                    assert user != null; //make sure it is not null before continuing
                    Log.d("Firebase", "User retrieved: " + user.firstName + " " + user.lastName); //firstname + lastName
                    Log.d("Firebase", "Email: " + user.email); // email
                    Log.d("Firebase", "Role: " + user.role); // role
                    Toast.makeText(getApplicationContext(), "Welcome: " + user.firstName + " " + user.lastName, Toast.LENGTH_LONG).show(); //Welcome toast message
                } else{
                    Log.d("Firebase", "User not found"); //Log if the user is not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error with Database: " + error.getMessage()); // Log if the database fails
            }
        });
    }
}