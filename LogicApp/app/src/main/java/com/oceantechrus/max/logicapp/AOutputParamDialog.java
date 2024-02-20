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
import android.widget.RadioButton;

public class AOutputParamDialog extends AppCompatDialogFragment {
    private EditText editTextAoHandModeValue;
    private EditText editTextAoMinValue;
    private EditText editTextAoMaxValue;
    private RadioButton radioButtonAoInversionOff;
    private RadioButton radioButtonAoInversionOn;
    private RadioButton radioButtonAoModeAuto;
    private RadioButton radioButtonAoModeHand;
    private AOutputParamDialog.AOutputParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.ao_param_dialog, null);

        builder.setView(v);
        builder.setTitle("Редактор параметров выхода");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump values
                listener.applyNewAoParamValues(65535, 100, 0, false, false);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean newHandMode;
                boolean newInversion;
                String str;

                str = editTextAoHandModeValue.getText().toString();
                if (str.isEmpty() || str.length() > 3) {
                    str = "65356";
                }

                int newHandModeValue = Integer.parseInt(str);

                str = editTextAoMaxValue.getText().toString();
                if (str.isEmpty() || str.length() > 3) {
                    str = "65356";
                }

                int newMaxValue = Integer.parseInt(str);

                str = editTextAoMinValue.getText().toString();
                if (str.isEmpty() || str.length() > 4) {
                    str = "65356";
                }

                int newMinValue = Integer.parseInt(str);

                if (radioButtonAoModeAuto.isChecked())
                    newHandMode = false;
                else
                    newHandMode = true;

                if (radioButtonAoInversionOff.isChecked())
                    newInversion = false;
                else
                    newInversion = true;

                listener.applyNewAoParamValues(newHandModeValue, newMaxValue, newMinValue, newHandMode, newInversion);
            }
        });

        editTextAoHandModeValue = v.findViewById(R.id.editTextAoHandValue);
        editTextAoHandModeValue.setText(Integer.toString(this.getArguments().getInt("AO_HAND_MODE_VALUE_KEY")));

        editTextAoMinValue = v.findViewById(R.id.editTextYear);
        editTextAoMinValue.setText(Integer.toString(this.getArguments().getInt("AO_MIN_VALUE_KEY")));

        editTextAoMaxValue = v.findViewById(R.id.editTextMonth);
        editTextAoMaxValue.setText(Integer.toString(this.getArguments().getInt("AO_MAX_VALUE_KEY")));

        radioButtonAoModeAuto = v.findViewById(R.id.radioButtonAoAuto);
        radioButtonAoModeHand = v.findViewById(R.id.radioButtonAoHand);
        if (this.getArguments().getBoolean("AO_MODE_KEY"))
            radioButtonAoModeHand.setChecked(true);
        else
            radioButtonAoModeAuto.setChecked(true);

        radioButtonAoInversionOff = v.findViewById(R.id.radioButtonAoInversionOff);
        radioButtonAoInversionOn = v.findViewById(R.id.radioButtonAoInversionOn);
        if (this.getArguments().getBoolean("AO_INVERSION_KEY"))
            radioButtonAoInversionOn.setChecked(true);
        else
            radioButtonAoInversionOff.setChecked(true);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (AOutputParamDialogListener) context;
    }

    public interface AOutputParamDialogListener {
        void applyNewAoParamValues(int newHandModeValue, int newMaxValue, int newMinValue, boolean newHandMode, boolean newInversion);
    }
}
