package com.example.chat_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.FriendProfileActivity;
import com.example.chat_app.FriendRequestActivity;
import com.example.chat_app.R;
import com.example.chat_app.adapters.FriendAdapter;
import com.example.chat_app.models.Friend;
import com.example.chat_app.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FriendPageFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference userRef;
    private FriendAdapter adapter;
    private ImageButton addFriendBtn;
    private ImageButton notifyBtn;
    private DocumentReference sendFriendRequestRef;
    private FirebaseUser currentUser = fAuth.getCurrentUser();
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        currentUser = fAuth.getCurrentUser();

        if (currentUser != null) {
            userRef = fStore.collection("friends/" + currentUser.getUid() + "/follow");
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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.friend_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initRecyclerView();
        FirebaseUser user = fAuth.getCurrentUser();

        notifyBtn = (ImageButton) view.findViewById(R.id.notifyFriendBtn);
        addFriendBtn = (ImageButton) view.findViewById(R.id.addFriendBtn);
        addFriendBtn.setOnClickListener(this);
        notifyBtn.setOnClickListener(this);

        // 친구 눌렀을 때 프로필 창 띄우는 클릭 리스너
        adapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Friend friend = documentSnapshot.toObject(Friend.class);
                String nickName = friend.getNickName();
                String name = friend.getName();
                String email = friend.getEmail();
                Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                intent.putExtra("nickName", nickName);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initRecyclerView() {
        currentUser = fAuth.getCurrentUser();
        userRef = fStore.collection("friends/" + currentUser.getEmail() + "/follow");
        Query query = userRef;

        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>()
                .setQuery(query, Friend.class)
                .build();

        adapter = new FriendAdapter(options);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        if (view == addFriendBtn) {
            // 친구 추가 대화창 클릭 리스너
            FriendAddDialog dialog = FriendAddDialog.getInstance(new FriendAddDialog.EmailInputListener() {
                @Override
                public void onEmailInputComplete(String findByUserEmail) {
                    if (!findByUserEmail.isEmpty()) {

                        DocumentReference findUserRef = fStore.collection("users").document(findByUserEmail);
                        sendFriendRequestRef = fStore.collection("friendRequest").document(findByUserEmail);

                        findUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    User user = documentSnapshot.toObject(User.class);
                                    String setFriendProfilePic = user.getProfile_pic_url();
                                    String setFriendNickName = user.getNickName();
                                    String setFriendName = user.getName();
                                    String setFriendEmail = user.getEmail();
                                    String setFriendPhoneNum = user.getPhoneNumber();
                                    String setFriendStatusMsg = user.getStatusMsg();

                                    Friend friend = new Friend(setFriendProfilePic, setFriendNickName, setFriendName, setFriendEmail, setFriendPhoneNum, setFriendStatusMsg);
                                    //Friend friend = new Friend("default", setFriendNickName, setFriendName, setFriendEmail, setFriendPhoneNum, setFriendStatusMsg);
                                    DocumentReference addMyFriendDocRef = fStore.collection("friends/" + currentUser.getEmail() + "/follow").document(setFriendEmail);

                                    addMyFriendDocRef.set(friend);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        DocumentReference currentUserRef = fStore.collection("users").document(currentUser.getEmail());
                        currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = documentSnapshot.toObject(User.class);
                                String requestFriendProfilePic = user.getProfile_pic_url();
                                String requestFriendNickName = user.getNickName();
                                String requestFriendName = user.getName();
                                String requestFriendEmail = user.getEmail();
                                String requestFriendPhoneNum = user.getPhoneNumber();
                                String requestFriendStatusMsg = user.getStatusMsg();

                                Friend friend = new Friend(requestFriendProfilePic, requestFriendNickName, requestFriendName, requestFriendEmail, requestFriendPhoneNum, requestFriendStatusMsg);
                                //Friend friend = new Friend("default", requestFriendNickName, requestFriendName, requestFriendEmail, requestFriendPhoneNum, requestFriendStatusMsg);
                                sendFriendRequestRef.collection("request").document(currentUser.getEmail()).set(friend);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });

            dialog.show(getFragmentManager(), "Dialog");
            return;
        }

        if (view == notifyBtn) {
            startActivity(new Intent(getActivity(), FriendRequestActivity.class));
            return;
        }
    }
}