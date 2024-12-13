package com.example.NeoGestion.Control;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Model.Message;
import com.example.NeoGestion.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messages;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.isSentByUser() ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
