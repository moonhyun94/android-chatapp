package com.example.chat_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.models.Message;
import com.example.chat_app.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference currentUserRef;
    private String getMsgTypeLeftEmail;
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Message model) {

        switch (holder.getItemViewType()) {
            case MSG_TYPE_RIGHT :
                holder.item_Msg.setText(model.getMessage());
                holder.userName.setText(model.getNickName());
                break;

            case MSG_TYPE_LEFT:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                currentUserRef = fStore.collection("users").document(model.getSender());
                currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String profile_pic_url = documentSnapshot.getString("profile_pic_url");
                        if(!profile_pic_url.isEmpty() && profile_pic_url.length() > 0) {

                            Picasso.get()
                                    .load(profile_pic_url)
                                    .placeholder(R.drawable.ic_default)
                                    .error(R.drawable.ic_default)
                                    .into(holder.profile_image);
                        }
                    }
                });
                holder.item_Msg.setText(model.getMessage());
                holder.userName.setText(model.getNickName());
                break;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            return new ChatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            return new ImageViewHolder(view);
        }

    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profile_image;
        private TextView item_Msg, userName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            item_Msg = (TextView) itemView.findViewById(R.id.item_chatMsg);
            userName = (TextView) itemView.findViewById(R.id.item_chat_userName);
            profile_image = itemView.findViewById(R.id.chat_left_image_view);
        }
    }

    public class ImageViewHolder extends ChatViewHolder {

        private CircleImageView profile_image;
        private TextView item_Msg, userName;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.chat_left_image_view);
            item_Msg = (TextView) itemView.findViewById(R.id.item_chatMsg);
            userName = (TextView) itemView.findViewById(R.id.item_chat_userName);
        }
    }

    public void onBind(final ImageViewHolder holder, int position, Message model) {
        if(holder.profile_image != null)
            currentUserRef = fStore.collection("users").document(model.getSender());
        currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String profile_pic_url = documentSnapshot.getString("profile_pic_url");
                if(!profile_pic_url.isEmpty() && profile_pic_url.length() > 0) {

                    Picasso.get()
                            .load(profile_pic_url)
                            .placeholder(R.drawable.ic_default)
                            .error(R.drawable.ic_default)
                            .into(holder.profile_image);
                }
            }
        });

        holder.item_Msg.setText(model.getMessage());
        holder.userName.setText(model.getNickName());
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
