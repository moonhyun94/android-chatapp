package com.example.chat_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FriendAdapter extends FirestoreRecyclerAdapter<Friend, FriendAdapter.FriendHolder> {

    public FriendAdapter(@NonNull FirestoreRecyclerOptions<Friend> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull Friend model) {
        holder.imageViewProfile.setImageResource(model.getId());
        holder.textViewName.setText(model.getName());
        holder.textViewStatusMsg.setText(model.getStatusMsg());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new FriendHolder(v);
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName;
        TextView textViewStatusMsg;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.item_img);
            textViewName = itemView.findViewById(R.id.item_name);
            textViewStatusMsg = itemView.findViewById(R.id.item_statusMsg);
        }
    }
}
