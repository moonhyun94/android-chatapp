package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.adapters.MessageAdapter;
import com.example.chat_app.models.Message;
import com.example.chat_app.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView roomNameTv;
    private ImageButton sendBtn;
    private EditText inputMsg;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = fAuth.getCurrentUser();
    private DocumentReference currentUserRef = fStore.collection("users").document(currentUser.getEmail());
    private String roomName, friendEmail, message, currentUserNickName;

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
        setContentView(R.layout.chat);
        Calendar calendar = Calendar.getInstance();
        roomNameTv = findViewById(R.id.tv_chat_name);
        sendBtn = findViewById(R.id.button_send);
        inputMsg = findViewById(R.id.edt_input_message);
        recyclerView = findViewById(R.id.chat_recycler);

        // get Intent From ChatRoomFragment 여기서 계속 업데이트 해줘야될듯
        Intent intent = getIntent();
        roomName = intent.getExtras().getString("roomName");
        roomNameTv.setText(roomName);
        friendEmail = intent.getExtras().getString("friendEmail");
        initRecyclerView();
        sendBtn.setOnClickListener(this);

        // 내 이름 가져 오기 위한 리스너 입니다.
        currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                currentUserNickName = currentUser.getNickName();
            }
        });

    }

    private void initRecyclerView() {
        currentUser = fAuth.getCurrentUser();
        CollectionReference chatRef = fStore.collection("chatRooms/" + currentUser.getEmail() + "/rooms" + "/" + friendEmail + "/messages");

        Query query = chatRef;

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        adapter = new MessageAdapter(options);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        if (view == sendBtn) {
            message = inputMsg.getText().toString();
            if (!message.isEmpty()) {
                inputMsg.setText("");
                // 친구에게 보낼 채팅 item 생성 , LEFT  sender                     receiver
                Message friendMessage = new Message(currentUser.getEmail(), friendEmail, currentUserNickName, message, new Timestamp(new Date()));
                // 나에게 보낼 채팅 item 생성 , RIGHT
                Message myMessage = new Message(currentUser.getEmail(), friendEmail, "나", message, new Timestamp(new Date()));

                // 친구 채팅방 업데이트 용
                DocumentReference friendChatRoomRef = fStore.collection("chatRooms/" + friendEmail + "/rooms")
                        .document(currentUser.getEmail()).collection("messages").document(friendMessage.getTimestamp().toString());

                // 내 채팅방 업데이트 용
                DocumentReference myChatRoomRef = fStore.collection("chatRooms/" + currentUser.getEmail() + "/rooms")
                        .document(friendEmail).collection("messages").document(myMessage.getTimestamp().toString());

                // 마지막 메세지 보여주기 용
                DocumentReference myShowLastMsgRef = fStore.collection("chatRooms").document(currentUser.getEmail())
                        .collection("rooms").document(friendEmail);
                DocumentReference friendShowLastMsgRef = fStore.collection("chatRooms").document(friendEmail)
                        .collection("rooms").document(currentUser.getEmail());

                friendChatRoomRef.set(friendMessage);
                myChatRoomRef.set(myMessage);
                myShowLastMsgRef.update("lastMsg", message);
                friendShowLastMsgRef.update("lastMsg", message);
            }
        }

    }
}
