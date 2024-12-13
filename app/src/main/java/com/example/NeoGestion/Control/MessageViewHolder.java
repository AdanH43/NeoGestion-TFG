package com.example.NeoGestion.Control;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Model.Message;
import com.example.NeoGestion.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView textViewMessage;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewMessage = itemView.findViewById(R.id.textViewMessage);
    }

    public void bind(Message message) {
        textViewMessage.setText(message.getContent());
    }
}
