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

public class DeleteRoomDialog extends DialogFragment {
    private DeleteRoomDialog.DeleteRoomListener listener;

    public DeleteRoomDialog() { }

    public interface DeleteRoomListener { void onComplete();}

    public static DeleteRoomDialog getInstance(DeleteRoomDialog.DeleteRoomListener listener) {
        DeleteRoomDialog dialog = new DeleteRoomDialog();
        dialog.listener = listener;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_room, null);

        builder.setView(view)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onComplete();
                    }
                }).setNegativeButton("취소", null);

        return builder.create();
    }
}
