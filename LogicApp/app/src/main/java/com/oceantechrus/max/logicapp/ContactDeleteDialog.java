package com.oceantechrus.max.logicapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class ContactDeleteDialog extends AppCompatDialogFragment {
    private ContactDeleteDialog.ContactDeleteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View v = inflater.inflate(R.layout.contact_delete_dialog, null);

        builder.setView(v);
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            listener.applyContactDeleteTexts("cancel");
        }
    });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            listener.applyContactDeleteTexts("ok");
        }
    });

        return builder.create();
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (ContactDeleteDialog.ContactDeleteDialogListener) context;
    }

public interface ContactDeleteDialogListener {
    void applyContactDeleteTexts(String result);
}
}
