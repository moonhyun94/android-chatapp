package com.example.chat_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.R;
import com.example.chat_app.models.ChatRoom;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChatRoomAdapter extends FirestoreRecyclerAdapter<ChatRoom, ChatRoomAdapter.ChatRoomHolder> {

    private ChatRoomClickListener listener;

    public ChatRoomAdapter(@NonNull FirestoreRecyclerOptions<ChatRoom> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomAdapter.ChatRoomHolder holder, int position, @NonNull ChatRoom model) {
        holder.lastMsg.setText(model.getLastMsg());
        holder.chatRoomName.setText(model.getRoomName());
    }

    @NonNull
    @Override
    public ChatRoomAdapter.ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomHolder(view);
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder {
        TextView lastMsg, chatRoomName, msgCount;

        public ChatRoomHolder(@NonNull View itemView) {
            super(itemView);
            lastMsg = itemView.findViewById(R.id.last_message_view);
            chatRoomName = itemView.findViewById(R.id.chat_room_name);
            msgCount = itemView.findViewById(R.id.msg_count_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onChatRoomClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface ChatRoomClickListener {
        void onChatRoomClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setChatRoomClickListener(ChatRoomClickListener listener) {
        this.listener = listener;
    }
}

