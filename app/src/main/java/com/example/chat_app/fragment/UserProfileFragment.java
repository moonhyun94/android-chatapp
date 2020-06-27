package com.example.chat_app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chat_app.LoginActivity;
import com.example.chat_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private EditText userStatusMsgEditText;
    private TextInputEditText userNickNameEditText;
    private ImageButton userNickNameEditButton, userStatusMsgEditButton;
    private ImageView userProfilePicView;
    private TextView userNameText, userEmailText, userPhoneNumText;
    private Button logOutButton, deleteAccountButton;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private ListenerRegistration profileListener;
    private DocumentReference currentUserRef;
    private StorageReference mStorageRef;
    private static final int PICK_FROM_ALBUM = 1;
    private Uri uri;
    private CollectionReference friendRef;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = fAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference(currentUser.getEmail()).child("profile");

        profileListener = currentUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null && !documentSnapshot.exists()) return;

                String currentUserEmail = documentSnapshot.getString("email");
                String currentUserNickName = documentSnapshot.getString("nickName");
                String currentUserName = documentSnapshot.getString("name");
                String currentUserProfilePic = documentSnapshot.getString("profile_pic_url");
                String currentUserPhoneNum = documentSnapshot.getString("phoneNumber");
                String currentUserStatusMsg = documentSnapshot.getString("statusMsg");

                if (!currentUserProfilePic.isEmpty() && currentUserProfilePic.length() > 0) {
                    Picasso.get()
                            .load(currentUserProfilePic)
                            .placeholder(R.drawable.ic_default)
                            .into(userProfilePicView);
                } else {
                    userProfilePicView.setImageResource(R.drawable.ic_default);
                }

                userNickNameEditText.setText(currentUserNickName);
                userPhoneNumText.setText(currentUserPhoneNum);
                userNameText.setText(currentUserName);
                userEmailText.setText(currentUserEmail);
                userStatusMsgEditText.setText(currentUserStatusMsg);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        profileListener.remove();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        userNameText = (TextView) view.findViewById(R.id.user_profile_name);
        userEmailText = (TextView) view.findViewById(R.id.user_profile_email);
        userPhoneNumText = (TextView) view.findViewById(R.id.user_profile_phoneNum);

        //userNickNameEditText = (EditText) view.findViewById(R.id.user_profile_editNickNameText);
        userNickNameEditText = (TextInputEditText) view.findViewById(R.id.user_profile_editNickNameText);
        userStatusMsgEditText = (EditText) view.findViewById(R.id.user_profile_editStatusMsgText);

        userNickNameEditButton = (ImageButton) view.findViewById(R.id.user_profile_editNickNameBtn);
        userStatusMsgEditButton = (ImageButton) view.findViewById(R.id.user_profile_editStatusMsgBtn);

        userProfilePicView = (ImageView) view.findViewById(R.id.user_profile_image);

        logOutButton = (Button) view.findViewById(R.id.user_profile_logOutBtn);
        deleteAccountButton = (Button) view.findViewById(R.id.user_profile_deleteAccountBtn);

        currentUser = fAuth.getCurrentUser();
        currentUserRef = fStore.collection("users").document(currentUser.getEmail());

        onCreateClick();
        return view;
    }

    private void onCreateClick() {
        userNickNameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNickNameEditText.isEnabled()) {
                    final String newNickName = userNickNameEditText.getText().toString().trim();
                    if (newNickName.isEmpty()) {
                        userNickNameEditText.setError("닉네임을 입력해주세요");
                        return;
                    }

                    currentUserRef.update("nickName", newNickName);

                    final CollectionReference myFriendRef = fStore.collection("friends").document(currentUser.getEmail()).collection("follow");

                    myFriendRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                // 내 정보 수정을 친구의 친구 목록에 수정 시키기
                                final String getFriendEmail = queryDocumentSnapshot.getString("email");
                                DocumentReference updateMyProfileToFriendRef = fStore.collection("friends")
                                        .document(getFriendEmail).collection("follow").document(currentUser.getEmail());

                                // 친구의 채팅방 필드 업데이트 용
                                DocumentReference updateMyProfileToFriendChatRoomRef = fStore.collection("chatRooms").document(getFriendEmail)
                                        .collection("rooms").document(currentUser.getEmail());


                                // 친구의 메세지 목록에 내 닉네임 업데이트 해주기
                                final CollectionReference updateChatDocumentRef = fStore.collection("chatRooms").document(getFriendEmail)
                                        .collection("rooms").document(currentUser.getEmail()).collection("messages");

                                updateChatDocumentRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    List<String> documentIdsList = new ArrayList<>();

                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot myChatDocumentQuerySnapShot : queryDocumentSnapshots) {
                                            String senderReceiverEmailCheck = myChatDocumentQuerySnapShot.getString("sender");
                                            if (senderReceiverEmailCheck.equals(currentUser.getEmail())) {
                                                documentIdsList.add(myChatDocumentQuerySnapShot.getId());
                                            }
                                        }
                                        updateChatNickName(documentIdsList, getFriendEmail, newNickName);
                                    }
                                });

                                updateMyProfileToFriendRef.update("nickName", newNickName);
                                updateMyProfileToFriendChatRoomRef.update("roomName", newNickName);
                                updateMyProfileToFriendChatRoomRef.update("nickName", newNickName);
                            }
                        }
                    });

                    userNickNameEditText.setEnabled(false);
                } else {
                    userNickNameEditText.setEnabled(true);
                }
            }
        });

        userStatusMsgEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userStatusMsgEditText.isEnabled()) {
                    final String newStatusMsg = userStatusMsgEditText.getText().toString();

                    currentUserRef.update("statusMsg", newStatusMsg);

                    final CollectionReference myFriendRef = fStore.collection("friends").document(currentUser.getEmail()).collection("follow");

                    myFriendRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                String getFriendEmail = queryDocumentSnapshot.getString("email");
                                DocumentReference updateMyProfileToFriendRef = fStore.collection("friends")
                                        .document(getFriendEmail).collection("follow").document(currentUser.getEmail());
                                updateMyProfileToFriendRef.update("statusMsg", newStatusMsg);
                            }
                        }
                    });
                    userStatusMsgEditText.setEnabled(false);
                } else {
                    userStatusMsgEditText.setEnabled(true);
                }
            }
        });
        // 프로필 이미지 관련
        userProfilePicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAlbum();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteUserDialog dialog = DeleteUserDialog.getInstance(new DeleteUserDialog.DeleteUserListener() {
                    @Override
                    public void onComplete() {
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

                                    DocumentReference reference2 =friendRef.document(email);
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
                        deleteAccount();
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteAccount() {
        profileListener.remove();
        DocumentReference userRef = fStore.collection("users").document(currentUser.getEmail());
        userRef.delete();
        backToLogin();
    }

    private void backToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        currentUser.delete();
    }

    private void updateChatNickName(List<String> modifyList, String friendEmail, String newNickName) {
        for (String documentId : modifyList) {
            DocumentReference updateChatNickNameRef = fStore.collection("chatRooms").document(friendEmail)
                    .collection("rooms").document(currentUser.getEmail()).collection("messages").document(documentId);

            updateChatNickNameRef.update("nickName", newNickName);
        }

    }

    private void initAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            uri = data.getData();
            userProfilePicView.setImageURI(uri);

            upLoadImage();
        }
    }

    private void upLoadImage() {
        if (uri != null) {
            final StorageReference fileRef = mStorageRef.child(currentUser.getEmail());
            Task<UploadTask.TaskSnapshot> uploadTask = fileRef.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downLoadUri = task.getResult();
                        final String uriToString = downLoadUri.toString();
                        currentUserRef.update("profile_pic_url", uriToString);

                        final CollectionReference myFriendRef = fStore.collection("friends").document(currentUser.getEmail()).collection("follow");
                        myFriendRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    // 내 프로필 사진 수정을 친구의 친구 목록에 수정 시키기
                                    final String getFriendEmail = queryDocumentSnapshot.getString("email");
                                    DocumentReference updateMyProfileToFriendRef = fStore.collection("friends")
                                            .document(getFriendEmail).collection("follow").document(currentUser.getEmail());
                                    updateMyProfileToFriendRef.update("profile_pic_url", uriToString);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}



