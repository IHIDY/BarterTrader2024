/**
 * ChatActivity - Handles the chat functionality between users.
 * It displays messages and allows users to send new ones when the offer is accepted.
 */
package com.example.group8_bartertrader;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.MessageAdapter;
import com.example.group8_bartertrader.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private List<Message> messageList;
    private MessageAdapter adapter;

    private String offerId;
    private String currentUserEmail;

    private DatabaseReference messagesRef;
    private DatabaseReference offerAcceptedRef;
    private FirebaseAuth mAuth;

    /**
     * Initializes the activity, sets up UI elements and Firebase references.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 从 intent 获取参数
        offerId = getIntent().getStringExtra("offerId");
        mAuth = FirebaseAuth.getInstance();
        currentUserEmail = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "No email";

        // UI 初始化
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList, currentUserEmail);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(adapter);

        messagesRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(offerId)
                .child("messages");

        offerAcceptedRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(offerId)
                .child("offerAccepted");

        checkChatPermission();
    }

    /**
     * Checks if the chat offer has been accepted before allowing message sending.
     */
    private void checkChatPermission() {
        offerAcceptedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean accepted = snapshot.getValue(Boolean.class);
                if (accepted != null && accepted) {
                    listenForMessages();
                    setupSendButton();
                } else {
                    sendButton.setEnabled(false);
                    messageEditText.setEnabled(false);
                    Toast.makeText(ChatActivity.this, "Chat not available until offer is accepted.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error checking chat permission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Listens for new messages in the chat.
     */

    private void listenForMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    messageRecyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    /**
     * Sets up the send button to send messages to Firebase.
     */
    private void setupSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) return;

                Message message = new Message(currentUserEmail, text, System.currentTimeMillis());
                messagesRef.push().setValue(message);
                messageEditText.setText("");
            }
        });
    }
}
