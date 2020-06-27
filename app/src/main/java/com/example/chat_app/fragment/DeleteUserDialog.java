package com.example.chat_app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chat_app.R;

public class DeleteUserDialog extends DialogFragment {

    private DeleteUserListener listener;

    public DeleteUserDialog() { }

    public interface DeleteUserListener { void onComplete();}

    public static DeleteUserDialog getInstance(DeleteUserListener listener) {
        DeleteUserDialog dialog = new DeleteUserDialog();
        dialog.listener = listener;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_user, null);

        builder.setView(view)
                .setTitle("회원 탈퇴")
                .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onComplete();
                    }
                }).setNegativeButton("닫기", null);

        return builder.create();
    }
}
