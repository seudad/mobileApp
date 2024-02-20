package com.oceantechrus.max.logicapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ContactAddDialog extends AppCompatDialogFragment {
    private EditText editTextContactName;
    private EditText editTextContactNumber;
    private ContactAddDialog.ContactAddDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_contact_dialog, null);

        builder.setView(v);
        builder.setTitle("Добавление контакта");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyContactAddTexts("", "");
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = editTextContactName.getText().toString();
                String number = editTextContactNumber.getText().toString();

                if (number.length() != 12)
                    number = "";

                listener.applyContactAddTexts(name, number);
            }
        });

        editTextContactName = v.findViewById(R.id.editTextContactName);
        editTextContactNumber = v.findViewById(R.id.editTextContactNumber);

        editTextContactNumber.setText("+7");

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (ContactAddDialog.ContactAddDialogListener) context;
    }

    public interface ContactAddDialogListener {
        void applyContactAddTexts(String name, String number);
    }
}
