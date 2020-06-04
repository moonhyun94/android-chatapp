package com.example.chat_app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chat_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendAddDialog extends DialogFragment{

    private static final String TAG = "FriendAddDialog";
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private EmailInputListener listener;
    private EditText findFriendByEmailDialogEditText;
    private Button searchBtn;

    public FriendAddDialog(){}

    public static FriendAddDialog getInstance(EmailInputListener listener) {
        FriendAddDialog dialog = new FriendAddDialog();
        dialog.listener = listener;
        return dialog;
    }

    public interface EmailInputListener {
        void onEmailInputComplete(String email);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_friend, null);

        findFriendByEmailDialogEditText = (EditText) view.findViewById(R.id.edt_add_friend);

        builder.setView(view)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String findFriendByEmailDialog = findFriendByEmailDialogEditText.getText().toString().trim();
                        if(!findFriendByEmailDialog.isEmpty()) {
                            listener.onEmailInputComplete(findFriendByEmailDialog);
                        } else {
                            return;
                        }
                    }
                }).setNegativeButton("닫기", null);

        return builder.create();
    }
}
