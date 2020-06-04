package com.example.chat_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.models.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Message model) {
        holder.item_Msg.setText(model.getMessage());
        holder.userName.setText(model.getName());
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
        }

        return new ChatViewHolder(view);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView item_Msg, userName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            item_Msg = (TextView) itemView.findViewById(R.id.item_chatMsg);
            userName = (TextView) itemView.findViewById(R.id.item_chat_userName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(getItem(position).getSender().equals(currentUser.getEmail())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
