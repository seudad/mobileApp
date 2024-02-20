package com.oceantechrus.max.logicapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SetSystemNameDialog extends AppCompatDialogFragment {
    private EditText editTextNewSystemName;
    private SetSystemNameDialog.SetSystemNameDialogListener listener;
    private String temp_str;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.system_name_dialog, null);

        builder.setView(v);
        builder.setTitle("Редактор названия системы");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump value
                listener.applyNewSystemName("");
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check the text
                String str = editTextNewSystemName.getText().toString();
                if (str.isEmpty() || str.length() > 19) {
                    str = "";
                }

                listener.applyNewSystemName(str);
            }
        });

        editTextNewSystemName = (EditText) v.findViewById(R.id.editTextSystemName);
        editTextNewSystemName.setText(this.getArguments().getString("PARAM_SYSTEM_NAME_KEY"));

        temp_str = editTextNewSystemName.getText().toString();

        editTextNewSystemName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                if (!s.toString().equals(temp_str)) {
                    if (s.toString().length() > 19) {
                        editTextNewSystemName.setText(temp_str);
                    } else {
                        temp_str = s.toString();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (SetSystemNameDialog.SetSystemNameDialogListener) context;
    }

    public interface SetSystemNameDialogListener {
        void applyNewSystemName(String newVName);
    }
}
