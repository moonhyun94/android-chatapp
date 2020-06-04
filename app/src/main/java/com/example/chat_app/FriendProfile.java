package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app.fragment.ChatRoomFragment;
import com.example.chat_app.models.ChatRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FriendProfile extends AppCompatActivity implements View.OnClickListener {

    private Button chatButton, delete;
    private TextView nameView, emailView;
    private ImageView pic;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = fAuth.getCurrentUser();
    private String friendName, friendEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_profile_view);
        nameView = (TextView) findViewById(R.id.profile_name_friend);
        emailView = (TextView) findViewById(R.id.email_profile_friend);
        chatButton = (Button) findViewById(R.id.chatBtn);
        Intent intent = getIntent();
        friendName = intent.getExtras().getString("name");
        friendEmail = intent.getExtras().getString("email");
        nameView.setText(friendName);
        emailView.setText(friendEmail);
        chatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == chatButton) {
            String roomName = nameView.getText().toString().trim();

            // 내 계정에 채팅방 추가 하기 위한 ref
            final DocumentReference myChatRoomRef = fStore.collection("chatRooms/" + currentUser.getEmail() + "/rooms").document(friendEmail);

            // 내 이름을 친구 채팅방에 주기 위한 ref
            DocumentReference currentUserRef = fStore.collection("users").document(currentUser.getEmail());
            currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot currentUserDocument = task.getResult();
                        String currentUserName = currentUserDocument.getString("name");

                        // 친구 계정에 채팅방 추가를 위한 ref
                        DocumentReference friendChatRoomRef = fStore.collection("chatRooms/" + friendEmail + "/rooms")
                                .document(currentUser.getEmail());
                        ChatRoom friendChatRoom = new ChatRoom(currentUserName, "", currentUserName, currentUser.getEmail());
                        friendChatRoomRef.set(friendChatRoom);

                        // 내 계정에 채팅방 추가
                        ChatRoom myChatRoom = new ChatRoom(friendName, "", friendName, friendEmail);
                        myChatRoomRef.set(myChatRoom);
                    }
                }
            });
            Intent intent = new Intent(FriendProfile.this, ChatActivity.class);
            // 친구 프로필 창에서 채팅 시작 버튼 눌렀을 시 채팅방 이름 넘겨줌 (친구 이름)
            intent.putExtra("roomName", roomName);
            intent.putExtra("friendEmail", friendEmail);
            startActivity(intent);
        }
    }
}
