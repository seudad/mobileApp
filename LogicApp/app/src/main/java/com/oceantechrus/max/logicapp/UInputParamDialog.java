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

public class UInputParamDialog extends AppCompatDialogFragment {
    private EditText editTextUiHandModeValue;
    private EditText editTextUiMinValue;
    private EditText editTextUiMaxValue;
    private RadioButton radioButtonUiModeAuto;
    private RadioButton radioButtonUiModeHand;
    private EditText editTextUiOffsetValue;
    private RadioButton radioButtonUiNi1000TK5000;
    private RadioButton radioButtonUi010V;
    private RadioButton radioButtonUi020MA;
    private RadioButton radioButtonUi420MA;
    private RadioButton radioButtonUiDryContact;
    private RadioButton radioButtonUiPt1000;
    private boolean newDeviceFlag;
    private UInputParamDialog.UInputParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.ui_param_dialog, null);

        newDeviceFlag = false;

        builder.setView(v);
        builder.setTitle("Редактор параметров входа");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump values
                listener.applyNewUiParamValues(65535, 100, 0, false, 0, 0);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean newHandMode;
                String str;

                str = editTextUiHandModeValue.getText().toString();
                if (str.isEmpty() || str.length() > 5) {
                    str = "65356";
                }

                int newHandModeValue = Integer.parseInt(str);

                str = editTextUiMaxValue.getText().toString();
                if (str.isEmpty() || str.length() > 5) {
                    str = "65356";
                }

                int newMaxValue = Integer.parseInt(str);

                str = editTextUiMinValue.getText().toString();
                if (str.isEmpty() || str.length() > 5) {
                    str = "65356";
                }

                int newMinValue = Integer.parseInt(str);


                if (radioButtonUiModeAuto.isChecked())
                    newHandMode = false;
                else
                    newHandMode = true;


                str = editTextUiOffsetValue.getText().toString();
                if (str.isEmpty() || str.length() > 3) {
                    str = "65356";
                }

                int newOffsetValue = Integer.parseInt(str);

                str = editTextUiMinValue.getText().toString();
                if (str.isEmpty() || str.length() > 3) {
                    str = "65356";
                }

                int newSensor = 0;
                if (!newDeviceFlag) {
                    if (radioButtonUiNi1000TK5000.isChecked())
                        newSensor = 0;
                    if (radioButtonUi010V.isChecked())
                        newSensor = 1;
                    if (radioButtonUi020MA.isChecked())
                        newSensor = 2;
                    if (radioButtonUi420MA.isChecked())
                        newSensor = 3;
                    if (radioButtonUiDryContact.isChecked())
                        newSensor = 4;
                    if (radioButtonUiPt1000.isChecked())
                        newSensor = 5;
                } else {
                    if (radioButtonUiNi1000TK5000.isChecked())
                        newSensor = 10;
                    if (radioButtonUi010V.isChecked())
                        newSensor = 11;
                    if (radioButtonUi020MA.isChecked())
                        newSensor = 12;
                    if (radioButtonUi420MA.isChecked())
                        newSensor = 13;
                    if (radioButtonUiDryContact.isChecked())
                        newSensor = 14;
                    if (radioButtonUiPt1000.isChecked())
                        newSensor = 15;
                }

                listener.applyNewUiParamValues(newHandModeValue, newMaxValue, newMinValue, newHandMode, newOffsetValue, newSensor);
            }
        });

        editTextUiHandModeValue = v.findViewById(R.id.editTextUiHandValue);
        editTextUiHandModeValue.setText(Integer.toString(this.getArguments().getInt("UI_HAND_MODE_VALUE_KEY")));

        editTextUiMinValue = v.findViewById(R.id.editTextUiMinValue);
        editTextUiMinValue.setText(Integer.toString(this.getArguments().getInt("UI_MIN_VALUE_KEY")));

        editTextUiMaxValue = v.findViewById(R.id.editTextUiMaxValue);
        editTextUiMaxValue.setText(Integer.toString(this.getArguments().getInt("UI_MAX_VALUE_KEY")));

        editTextUiOffsetValue = v.findViewById(R.id.editTextUiOffsetValue);
        editTextUiOffsetValue.setText(Integer.toString(this.getArguments().getInt("UI_OFFSET_VALUE_KEY")));

        radioButtonUiModeAuto = v.findViewById(R.id.radioButtonUiAuto);
        radioButtonUiModeHand = v.findViewById(R.id.radioButtonUiHand);
        if (this.getArguments().getBoolean("UI_MODE_KEY"))
            radioButtonUiModeHand.setChecked(true);
        else
            radioButtonUiModeAuto.setChecked(true);

        radioButtonUiNi1000TK5000 = v.findViewById(R.id.radioButtonUiNi1000TK5000);
        radioButtonUi010V = v.findViewById(R.id.radioButtonUi010V);
        radioButtonUi020MA = v.findViewById(R.id.radioButtonUi020MA);
        radioButtonUi420MA = v.findViewById(R.id.radioButtonUi420MA);
        radioButtonUiDryContact = v.findViewById(R.id.radioButtonUiDryContact);
        radioButtonUiPt1000 = v.findViewById(R.id.radioButtonUiPT1000);
        switch (this.getArguments().getInt("UI_SENSOR_KEY")) {
            case 0:
                radioButtonUiNi1000TK5000.setChecked(true);
                break;
            case 1:
                radioButtonUi010V.setChecked(true);
                break;
            case 2:
                radioButtonUi020MA.setChecked(true);
                break;
            case 3:
                radioButtonUi420MA.setChecked(true);
                break;
            case 4:
                radioButtonUiDryContact.setChecked(true);
                break;
            case 5:
                radioButtonUiPt1000.setChecked(true);
                break;
            case 10:
                radioButtonUiNi1000TK5000.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
            case 11:
                radioButtonUi010V.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
            case 12:
                radioButtonUi020MA.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
            case 13:
                radioButtonUi420MA.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
            case 14:
                radioButtonUiDryContact.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
            case 15:
                radioButtonUiPt1000.setChecked(true);
                radioButtonUiNi1000TK5000.setText("Канал 1");
                radioButtonUi010V.setText("Канал 2");
                radioButtonUi020MA.setText("Канал 3");
                radioButtonUi420MA.setText("Канал 4");
                radioButtonUiDryContact.setText("Канал 5");
                radioButtonUiPt1000.setText("Канал 6");
                newDeviceFlag = true;
                break;
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (UInputParamDialog.UInputParamDialogListener) context;
    }

    public interface UInputParamDialogListener {
        void applyNewUiParamValues(int newHandModeValue, int newMaxValue, int newMinValue, boolean newHandMode,
                                   int newOffset, int newSensor);
    }
}
