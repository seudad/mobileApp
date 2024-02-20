package com.oceantechrus.max.logicapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

public class CondModeDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonCondModeAuto;
    private RadioButton radioButtonCondModeVent;
    private RadioButton radioButtonCondModeHeating;
    private RadioButton radioButtonCondModeChilling;
    private CondModeDialog.CondModeDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.season_dialog, null);

        builder.setView(v);
        builder.setTitle("Режим работы");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyNewCondMode(999);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int newCondMode = 0;

                if (radioButtonCondModeAuto.isChecked())
                    newCondMode = 0;
                else if (radioButtonCondModeVent.isChecked())
                    newCondMode = 1;
                else if (radioButtonCondModeHeating.isChecked())
                    newCondMode = 2;
                else if (radioButtonCondModeChilling.isChecked())
                    newCondMode = 3;

                listener.applyNewCondMode(newCondMode);
            }
        });

        radioButtonCondModeAuto = v.findViewById(R.id.radioButtonCondModeAuto);
        radioButtonCondModeVent = v.findViewById(R.id.radioButtonCondModeVent);
        radioButtonCondModeHeating = v.findViewById(R.id.radioButtonCondModeHeating);
        radioButtonCondModeChilling = v.findViewById(R.id.radioButtonCondModeChilling);
        switch (this.getArguments().getInt("COND_MODE_KEY")) {
            case 0:
                radioButtonCondModeAuto.setChecked(true);
                break;
            case 1:
                radioButtonCondModeVent.setChecked(true);
                break;
            case 2:
                radioButtonCondModeHeating.setChecked(true);
                break;
            case 3:
                radioButtonCondModeChilling.setChecked(true);
                break;
            default:
                break;
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (CondModeDialog.CondModeDialogListener) context;
    }

    public interface CondModeDialogListener {
        void applyNewCondMode(int newCondMode);
    }
}
