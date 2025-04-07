package com.example.group8_bartertrader.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.group8_bartertrader.model.Message;
import com. example. group8_bartertrader.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> messageList;
    private String currentUserId;

    /**
     * adapts the messages
     * @param context
     * @param messageList
     * @param currentUserId
     */
    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        LinearLayout messageContainer;
        TextView timeStamp;

        /**
         * creates the item view
         * @param itemView
         */
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
            timeStamp = itemView.findViewById(R.id.timestampTextView);
        }
    }

    /**
     *message view holder
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    /**
     *holds the content at the given time/position
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageTextView.setText(message.getContent());

        String time = android.text.format.DateFormat.format("hh:mm a", message.getTimestamp()).toString();
        holder.timeStamp.setText(time);

        if (message.getSenderId().equals(currentUserId)) {
            holder.messageContainer.setGravity(Gravity.END);
            holder.messageTextView.setBackgroundResource(R.drawable.bubble_me);
        } else {
//            System.out.println(message.getSenderId() + " *** " + currentUserId);
            holder.messageContainer.setGravity(Gravity.START);
            holder.messageTextView.setBackgroundResource(R.drawable.bubble_other);
        }
    }

    /**
     * returns the item count
     * @return
     */
    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
