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

public class CondFanSpeedNumDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonFanSpeedNum1;
    private RadioButton radioButtonFanSpeedNum2;
    private RadioButton radioButtonFanSpeedNum3;
    private RadioButton radioButtonFanSpeedNum4;
    private RadioButton radioButtonFanSpeedNum5;
    private RadioButton radioButtonFanSpeedNum6;
    private RadioButton radioButtonFanSpeedNum7;
    private CondFanSpeedNumDialog.FanSpeedNumDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.cond_fan_speed_dialog, null);

        builder.setView(v);
        builder.setTitle("Скорость вентилятора");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyNewFanSpeedNum(0);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int newFanSpeedNum = 1;

                if (radioButtonFanSpeedNum1.isChecked())
                    newFanSpeedNum = 1;
                else

                if (radioButtonFanSpeedNum2.isChecked())
                    newFanSpeedNum = 2;
                else

                if (radioButtonFanSpeedNum3.isChecked())
                    newFanSpeedNum = 3;
                else

                if (radioButtonFanSpeedNum4.isChecked())
                    newFanSpeedNum = 4;
                else

                if (radioButtonFanSpeedNum5.isChecked())
                    newFanSpeedNum = 5;
                else

                if (radioButtonFanSpeedNum6.isChecked())
                    newFanSpeedNum = 6;
                else

                if (radioButtonFanSpeedNum7.isChecked())
                    newFanSpeedNum = 7;

                listener.applyNewFanSpeedNum(newFanSpeedNum);
            }
        });

        radioButtonFanSpeedNum1 = v.findViewById(R.id.radioButtonFanSpeedNum1);
        radioButtonFanSpeedNum2 = v.findViewById(R.id.radioButtonFanSpeedNum2);
        radioButtonFanSpeedNum3 = v.findViewById(R.id.radioButtonFanSpeedNum3);
        radioButtonFanSpeedNum4 = v.findViewById(R.id.radioButtonFanSpeedNum4);
        radioButtonFanSpeedNum5 = v.findViewById(R.id.radioButtonFanSpeedNum5);
        radioButtonFanSpeedNum6 = v.findViewById(R.id.radioButtonFanSpeedNum6);
        radioButtonFanSpeedNum7 = v.findViewById(R.id.radioButtonFanSpeedNum7);
        switch (this.getArguments().getInt("FAN_SPEED_NUM_KEY")) {
            case 1:
                radioButtonFanSpeedNum1.setChecked(true);
                break;
            case 2:
                radioButtonFanSpeedNum2.setChecked(true);
                break;
            case 3:
                radioButtonFanSpeedNum3.setChecked(true);
                break;
            case 4:
                radioButtonFanSpeedNum4.setChecked(true);
                break;
            case 5:
                radioButtonFanSpeedNum5.setChecked(true);
                break;
            case 6:
                radioButtonFanSpeedNum6.setChecked(true);
                break;
            case 7:
                radioButtonFanSpeedNum7.setChecked(true);
                break;
            default:
                break;
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (CondFanSpeedNumDialog.FanSpeedNumDialogListener) context;
    }

    public interface FanSpeedNumDialogListener {
        void applyNewFanSpeedNum(int newFanSpeedNum);
    }
}
