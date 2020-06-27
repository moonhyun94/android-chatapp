package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app.models.ChatRoom;
import com.example.chat_app.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class FriendProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button chatButton, delete, closeBtn;
    private TextView nameView, emailView, nickNameView;
    private ImageView profileView;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = fAuth.getCurrentUser();
    private CollectionReference friendRef;
    private String friendName, friendEmail, friendNickName;
    private DocumentReference getFriendRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_profile_view);
        nameView = (TextView) findViewById(R.id.profile_name_friend);
        emailView = (TextView) findViewById(R.id.email_profile_friend);
        nickNameView = (TextView) findViewById(R.id.profile_nickName_friend);
        profileView = (ImageView) findViewById(R.id.friend_profile_image);
        chatButton = (Button) findViewById(R.id.chatBtn);
        closeBtn = (Button) findViewById(R.id.friend_profile_closeBtn);
        delete = (Button) findViewById(R.id.delete_friend_button);
        // get Intent from FriendPageFragment
        Intent intent = getIntent();
        friendNickName = intent.getExtras().getString("nickName");
        friendName = intent.getExtras().getString("name");
        friendEmail = intent.getExtras().getString("email");

        getFriendRef = fStore.collection("users").document(friendEmail);
        getFriendRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        String friendNickName = user.getNickName();
                        String friendName = user.getName();
                        String friendEmail = user.getEmail();
                        String friendProfilePicUrl = user.getProfile_pic_url();
                        nickNameView.setText(friendNickName);
                        nameView.setText(friendName);
                        emailView.setText(friendEmail);
                        if (!friendProfilePicUrl.isEmpty() && friendProfilePicUrl.length() > 0) {
                            Picasso.get()
                                    .load(friendProfilePicUrl)
                                    .placeholder(R.drawable.ic_default)
                                    .into(profileView);
                        } else {
                            profileView.setImageResource(R.drawable.ic_default);
                        }
                    }
                });
        chatButton.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == closeBtn) {
            finish();
        }
        if(view == delete) {
            friendRef = fStore.collection("friends").document(currentUser.getEmail())
                    .collection("follow");
            CollectionReference chatRef = fStore.collection("chatRooms").document(currentUser.getEmail())
                    .collection("rooms");

            friendRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String email = snapshot.getString("email");

                        DocumentReference reference = fStore.collection("friends").document(email)
                                .collection("follow").document(currentUser.getEmail());
                        reference.delete();

                        DocumentReference reference2 = friendRef.document(email);
                        reference2.delete();
                    }
                }
            });

            chatRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        final String email = snapshot.getString("participantEmail");

                        final CollectionReference msgRef = fStore.collection("chatRooms").document(email)
                                .collection("rooms").document(currentUser.getEmail()).collection("messages");
                        msgRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    DocumentReference tmpRef = fStore.collection("chatRooms").document(email)
                                            .collection("rooms").document(currentUser.getEmail())
                                            .collection("messages").document(snapshot.getId());
                                    tmpRef.delete();
                                }
                            }
                        });

                        final CollectionReference msgRef2 = fStore.collection("chatRooms").document(currentUser.getEmail())
                                .collection("rooms").document(email).collection("messages");
                        msgRef2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    DocumentReference tmpRef = fStore.collection("chatRooms").document(currentUser.getEmail())
                                            .collection("rooms").document(email)
                                            .collection("messages").document(snapshot.getId());
                                    tmpRef.delete();
                                }
                            }
                        });

                        DocumentReference reference = fStore.collection("chatRooms").document(email).collection("rooms")
                                .document(currentUser.getEmail());
                        reference.delete();

                        DocumentReference reference2 = fStore.collection("chatRooms").document(currentUser.getEmail())
                                .collection("rooms").document(email);
                        reference2.delete();
                    }
                }
            });
            finish();
        }
        if (view == chatButton) {
            String roomName = nickNameView.getText().toString().trim();

            // 내 계정에 채팅방 추가 하기 위한 ref
            final DocumentReference myChatRoomRef = fStore.collection("chatRooms/" + currentUser.getEmail() + "/rooms").document(friendEmail);

            // 내 이름을 친구 채팅방에 주기 위한 ref
            DocumentReference currentUserRef = fStore.collection("users").document(currentUser.getEmail());
            currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot currentUserDocument = task.getResult();
                        String currentUserName = currentUserDocument.getString("name");
                        String currentUserNickName = currentUserDocument.getString("nickName");

                        // 친구 계정에 채팅방 추가를 위한 ref
                        DocumentReference friendChatRoomRef = fStore.collection("chatRooms/" + friendEmail + "/rooms")
                                .document(currentUser.getEmail());
                        ChatRoom friendChatRoom = new ChatRoom(currentUserNickName, "", currentUserName, currentUser.getEmail());
                        friendChatRoomRef.set(friendChatRoom);

                        // 내 계정에 채팅방 추가
                        ChatRoom myChatRoom = new ChatRoom(friendNickName, "", friendName, friendEmail);
                        myChatRoomRef.set(myChatRoom);
                    }
                }
            });
            Intent intent = new Intent(FriendProfileActivity.this, ChatActivity.class);
            // 친구 프로필 창에서 채팅 시작 버튼 눌렀을 시 채팅방 이름 넘겨줌 (친구 이름)
            intent.putExtra("roomName", roomName);
            intent.putExtra("friendEmail", friendEmail);
            startActivity(intent);
        }
    }
}
