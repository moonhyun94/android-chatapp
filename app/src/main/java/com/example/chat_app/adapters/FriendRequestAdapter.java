package com.example.chat_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.chat_app.R;
import com.example.chat_app.models.Friend;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends FirestoreRecyclerAdapter<Friend, FriendRequestAdapter.FriendRequestViewHolder> {

    private ButtonClickListener listener;

    public FriendRequestAdapter(@NonNull FirestoreRecyclerOptions<Friend> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendRequestAdapter.FriendRequestViewHolder holder, int position, @NonNull Friend model) {

        if(!model.getProfile_pic_url().isEmpty() && model.getProfile_pic_url().length() > 0) {
            Picasso.get()
                    .load(model.getProfile_pic_url())
                    .placeholder(R.drawable.ic_default)
                    .into(holder.profile_pic_view);
        } else {
            holder.profile_pic_view.setImageResource(R.drawable.ic_default);
        }
        holder.name_view.setText(model.getName());
    }

    @NonNull
    @Override
    public FriendRequestAdapter.FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_friend, parent, false);

        return new FriendRequestViewHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class FriendRequestViewHolder extends ViewHolder {
        CircleImageView profile_pic_view;
        TextView name_view;
        Button accept_button, decline_button;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic_view = (CircleImageView) itemView.findViewById(R.id.item_profile_pic_request_friend);
            name_view = (TextView) itemView.findViewById(R.id.item_name_request_friend);
            accept_button = (Button) itemView.findViewById(R.id.item_accept_button_request_friend);
            decline_button = (Button) itemView.findViewById(R.id.item_decline_button_request_friend);

            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.acceptButtonClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            decline_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.declineButtonClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface ButtonClickListener {
        void acceptButtonClick(DocumentSnapshot documentSnapshot, int position);
        void declineButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setButtonClickListener(ButtonClickListener acceptListener) {
        this.listener = acceptListener;
    }
}
