package com.example.chat_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.Friend;
import com.example.chat_app.FriendAdapter;
import com.example.chat_app.LoginActivity;
import com.example.chat_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class FriendFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Friend> friends;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    //private FirebaseUser user;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference userRef; //= fStore.collection("users");
    private DocumentReference documentReference;
    private FriendAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        FirebaseUser user = fAuth.getCurrentUser();

        if (user != null) {
            userRef = fStore.collection("friends/" + user.getUid() + "/follow");
        } else {
            return;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initRecyclerView();
        FirebaseUser user = fAuth.getCurrentUser();

//        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                firebaseAuth.signOut();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    private void initRecyclerView() {

        FirebaseUser user = fAuth.getCurrentUser();
        userRef = fStore.collection("friends/" + user.getUid() + "/follow");
        Query query = userRef;
        query.get();
                //userRef;

                //.orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>()
                .setQuery(query, Friend.class)
                .build();

        adapter = new FriendAdapter(options);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
