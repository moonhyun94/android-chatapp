package com.example.chat_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.ChatActivity;
import com.example.chat_app.R;
import com.example.chat_app.adapters.ChatRoomAdapter;
import com.example.chat_app.models.ChatRoom;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatRoomFragment extends Fragment {

    private ChatRoomAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private String roomName, friendEmail;
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_room_recycler);
        initRecyclerView();

        // 친구 프로필 창에서 채팅 버튼 누르면 채팅창 띄워주는 클릭 이벤트
        adapter.setChatRoomClickListener(new ChatRoomAdapter.ChatRoomClickListener() {
            @Override
            public void onChatRoomClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                ChatRoom chatRoom = documentSnapshot.toObject(ChatRoom.class);

                roomName = chatRoom.getRoomName();
                friendEmail = chatRoom.getParticipantEmail();

                // 채팅 방에서 채팅방 클릭시 채팅방 이름 넘겨줌 이걸 파이어베이스에서 읽어와야될듯
                intent.putExtra("roomName", roomName);
                intent.putExtra("friendEmail", friendEmail);

                startActivity(intent);
            }
        });

        adapter.setChatRoomLongClickListener(new ChatRoomAdapter.ChatRoomLongClickListener() {
            @Override
            public void onChatRoomLongClick(DocumentSnapshot documentSnapshot, int position) {
                DeleteRoomDialog dialog = DeleteRoomDialog.getInstance(new DeleteRoomDialog.DeleteRoomListener() {
                    @Override
                    public void onComplete() {
                        final CollectionReference myRef = fStore.collection("chatRooms").document(user.getEmail()).collection("rooms")
                                .document(friendEmail).collection("messages");
                        myRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    DocumentReference deleteMsg = myRef.document(document.getId());
                                    deleteMsg.delete();
                                }
                            }
                        });
                        DocumentReference deleteRoom = fStore.collection("chatRooms").document(user.getEmail())
                                .collection("rooms").document(friendEmail);
                        deleteRoom.delete();
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });

        return view;
    }

    private void initRecyclerView() {
        user = fAuth.getCurrentUser();
        CollectionReference chatRoomRef = fStore.collection("chatRooms/" + user.getEmail() + "/rooms");
        Query query = chatRoomRef;
        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query, ChatRoom.class)
                .build();

        adapter = new ChatRoomAdapter(options);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
