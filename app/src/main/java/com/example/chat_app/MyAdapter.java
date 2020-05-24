package com.example.chat_app;

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Friend> friends = new ArrayList<>();

    public MyAdapter(List<Friend> friends) {
        this.friends = friends;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        protected ImageView profileImg;
        protected TextView name;
        protected TextView statusMsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImg = (ImageView) itemView.findViewById(R.id.item_img);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.statusMsg = (TextView) itemView.findViewById(R.id.item_statusMsg);
        }

        void onBind(Friend friend) {
            profileImg.setImageResource(friend.getId());
            name.setText(friend.getName());
            statusMsg.setText(friend.getStatusMsg());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
       myViewHolder.profileImg.setImageResource(friends.get(position).getId());
        myViewHolder.name.setText(friends.get(position).getName());
        myViewHolder.statusMsg.setText(friends.get(position).getStatusMsg());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    void addFriend(Friend friend) {
        friends.add(friend);
    }
}
