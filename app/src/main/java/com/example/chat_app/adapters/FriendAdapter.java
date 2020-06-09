package com.example.chat_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.models.Friend;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class FriendAdapter extends FirestoreRecyclerAdapter<Friend, FriendAdapter.FriendHolder> {

    private OnItemClickListener listener;

    public FriendAdapter(@NonNull FirestoreRecyclerOptions<Friend> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull Friend model) {

        if(!model.getProfile_pic_url().isEmpty() && model.getProfile_pic_url().length() > 0) {
            Picasso.get()
                    .load(model.getProfile_pic_url())
                    .placeholder(R.drawable.ic_default)
                    .into(holder.imageViewProfile);
        } else {
            holder.imageViewProfile.setImageResource(R.drawable.ic_default);
        }

        holder.textViewName.setText(model.getNickName());
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }
}
