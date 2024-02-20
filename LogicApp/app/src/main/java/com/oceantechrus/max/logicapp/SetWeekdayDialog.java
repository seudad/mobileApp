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

public class SetWeekdayDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonMon;
    private RadioButton radioButtonTue;
    private RadioButton radioButtonWed;
    private RadioButton radioButtonThu;
    private RadioButton radioButtonFri;
    private RadioButton radioButtonSat;
    private RadioButton radioButtonSun;
    private SetWeekdayDialog.SetWeekdayDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.weekday_dialog, null);

        builder.setView(v);
        builder.setTitle("Редактор названия системы");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump value
                listener.applyNewWeekday(-999);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check the text
                int weekday = -999;
                if (radioButtonMon.isChecked()) {
                    weekday = 1;
                } else if (radioButtonTue.isChecked()) {
                    weekday = 2;
                } else if (radioButtonWed.isChecked()) {
                    weekday = 3;
                } else if (radioButtonThu.isChecked()) {
                    weekday = 4;
                } else if (radioButtonFri.isChecked()) {
                    weekday = 5;
                } else if (radioButtonSat.isChecked()) {
                    weekday = 6;
                } else if (radioButtonSun.isChecked()) {
                    weekday = 7;
                }

                listener.applyNewWeekday(weekday);
            }
        });


        radioButtonMon = v.findViewById(R.id.radioButtonMon);
        radioButtonTue = v.findViewById(R.id.radioButtonTue);
        radioButtonWed = v.findViewById(R.id.radioButtonWed);
        radioButtonThu = v.findViewById(R.id.radioButtonThu);
        radioButtonFri = v.findViewById(R.id.radioButtonFri);
        radioButtonSat = v.findViewById(R.id.radioButtonSat);
        radioButtonSun = v.findViewById(R.id.radioButtonSun);

        radioButtonMon.setChecked(false);
        radioButtonTue.setChecked(false);
        radioButtonWed.setChecked(false);
        radioButtonThu.setChecked(false);
        radioButtonFri.setChecked(false);
        radioButtonSat.setChecked(false);
        radioButtonSun.setChecked(false);

        switch (this.getArguments().getInt("PARAM_WEEKDAY_KEY")) {
            case 0:
                break;
            case 1:
                radioButtonMon.setChecked(true);
                break;
            case 2:
                radioButtonTue.setChecked(true);
                break;
            case 3:
                radioButtonWed.setChecked(true);
                break;
            case 4:
                radioButtonThu.setChecked(true);
                break;
            case 5:
                radioButtonFri.setChecked(true);
                break;
            case 6:
                radioButtonSat.setChecked(true);
                break;
            case 7:
                radioButtonSun.setChecked(true);
                break;
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (SetWeekdayDialog.SetWeekdayDialogListener) context;
    }

    public interface SetWeekdayDialogListener {
        void applyNewWeekday(int newWeekday);
    }
}
