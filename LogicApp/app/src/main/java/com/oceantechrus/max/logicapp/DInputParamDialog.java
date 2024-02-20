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

public class DInputParamDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonDiShort;
    private RadioButton radioButtonDiOpen;
    private RadioButton radioButtonDiInversionOff;
    private RadioButton radioButtonDiInversionOn;
    private RadioButton radioButtonDiModeAuto;
    private RadioButton radioButtonDiModeHand;
    private DInputParamDialog.DInputParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.di_param_dialog, null);

        builder.setView(v);
        builder.setTitle("Редактор параметров входа");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump values
                listener.applyNewDiParamValues(false, false, false, true);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean newHandMode;
                boolean newHandModeState;
                boolean newInversion;

                if (radioButtonDiModeAuto.isChecked())
                    newHandMode = false;
                else
                    newHandMode = true;

                if (radioButtonDiShort.isChecked())
                    newHandModeState = false;
                else
                    newHandModeState = true;

                if (radioButtonDiInversionOff.isChecked())
                    newInversion = false;
                else
                    newInversion = true;

                listener.applyNewDiParamValues(newHandModeState, newHandMode, newInversion, false);
            }
        });

        radioButtonDiModeAuto = v.findViewById(R.id.radioButtonDiAuto);
        radioButtonDiModeHand = v.findViewById(R.id.radioButtonDiHand);
        if (this.getArguments().getBoolean("DI_MODE_KEY"))
            radioButtonDiModeHand.setChecked(true);
        else
            radioButtonDiModeAuto.setChecked(true);

        radioButtonDiShort = v.findViewById(R.id.radioButtonDiShort);
        radioButtonDiOpen = v.findViewById(R.id.radioButtonDiOpen);
        if (this.getArguments().getBoolean("DI_HAND_MODE_STATE_KEY"))
            radioButtonDiOpen.setChecked(true);
        else
            radioButtonDiShort.setChecked(true);

        radioButtonDiInversionOff = v.findViewById(R.id.radioButtonDiInversionOff);
        radioButtonDiInversionOn = v.findViewById(R.id.radioButtonDiInversionOn);
        if (this.getArguments().getBoolean("DI_INVERSION_KEY"))
            radioButtonDiInversionOn.setChecked(true);
        else
            radioButtonDiInversionOff.setChecked(true);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (DInputParamDialog.DInputParamDialogListener) context;
    }

    public interface DInputParamDialogListener {
        void applyNewDiParamValues(boolean newHandModeState, boolean newHandMode, boolean newInversion, boolean dump);
    }
}
