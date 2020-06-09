package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.adapters.FriendRequestAdapter;
import com.example.chat_app.models.Friend;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FriendRequestActivity extends AppCompatActivity {
    private CollectionReference friendRequestRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FriendRequestAdapter adapter;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = fAuth.getCurrentUser();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request_page);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_notify);
        Intent intent = getIntent();
        initRecyclerView();

        adapter.setButtonClickListener(new FriendRequestAdapter.ButtonClickListener() {
            @Override
            public void acceptButtonClick(DocumentSnapshot documentSnapshot, int position) {
                Friend requestInfo = documentSnapshot.toObject(Friend.class);

                // 여기 수정함
                Friend friend = new Friend(requestInfo.getProfile_pic_url(), requestInfo.getNickName(), requestInfo.getName(), requestInfo.getEmail(),
                        requestInfo.getPhoneNum(), requestInfo.getStatusMsg());

                DocumentReference addMyFriendDocRef = fStore.collection("friends/" + currentUser.getEmail() + "/follow").document(requestInfo.getEmail());
                addMyFriendDocRef.set(friend);
                adapter.deleteItem(position);
            }
            @Override
            public void declineButtonClick(DocumentSnapshot documentSnapshot, int position) {
                adapter.deleteItem(position);
            }
        });
    }

    private void initRecyclerView() {
        currentUser = fAuth.getCurrentUser();
        friendRequestRef = fStore.collection("friendRequest/" + currentUser.getEmail() + "/request");
        Query query = friendRequestRef;
        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>()
                .setQuery(query, Friend.class)
                .build();

        adapter = new FriendRequestAdapter(options);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }
}
