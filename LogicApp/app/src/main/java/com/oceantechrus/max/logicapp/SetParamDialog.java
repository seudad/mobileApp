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


public class SetParamDialog extends AppCompatDialogFragment {
    private EditText editTextNewValue;
    private int season;
    private int alg;
    private boolean main_screen;
    private SetParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.set_param_dialog, null);

        season = this.getArguments().getInt("SEASON_KEY");
        alg = this.getArguments().getInt("ALG_KEY");
        main_screen = this.getArguments().getBoolean("MAIN_SCREEN_KEY");

        builder.setView(v);
        if (!main_screen) {
            builder.setTitle("Редактор значения параметра");
        } else {
            if (alg != 30101) {
                if (season == 0) {
                    builder.setTitle("t воздуха в режиме Зима");
                } else {
                    builder.setTitle("t воздуха в режиме Лето");
                }
            } else {
                builder.setTitle("t приточного воздуха");
            }
        }

        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump value
                listener.applyNewValue(65535);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check the text
                String str = editTextNewValue.getText().toString();
                if (str.isEmpty() || str.length() > 5) {
                    str = "65356";
                }

                int newValue = Integer.parseInt(str);
                listener.applyNewValue(newValue);
            }
        });


        editTextNewValue = (EditText) v.findViewById(R.id.editTextNewValue);
        editTextNewValue.setText(Integer.toString(this.getArguments().getInt("PARAM_VAL_KEY")));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (SetParamDialogListener) context;
    }

    public interface SetParamDialogListener {
        void applyNewValue(int newValue);
    }
}
