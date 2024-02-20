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

public class DOutputParamDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonDoShort;
    private RadioButton radioButtonDoOpen;
    private RadioButton radioButtonDoInversionOff;
    private RadioButton radioButtonDoInversionOn;
    private RadioButton radioButtonDoModeAuto;
    private RadioButton radioButtonDoModeHand;
    private DOutputParamDialog.DOutputParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.do_param_dialog, null);

        builder.setView(v);
        builder.setTitle("Редактор параметров выхода");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump values
                listener.applyNewDoParamValues(false, false, false, true);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean newHandMode;
                boolean newHandModeState;
                boolean newInversion;

                if (radioButtonDoModeAuto.isChecked())
                    newHandMode = false;
                else
                    newHandMode = true;

                if (radioButtonDoShort.isChecked())
                    newHandModeState = true;
                else
                    newHandModeState = false;

                if (radioButtonDoInversionOff.isChecked())
                    newInversion = false;
                else
                    newInversion = true;

                listener.applyNewDoParamValues(newHandModeState, newHandMode, newInversion, false);
            }
        });

        radioButtonDoModeAuto = v.findViewById(R.id.radioButtonDoAuto);
        radioButtonDoModeHand = v.findViewById(R.id.radioButtonDoHand);
        if (this.getArguments().getBoolean("DO_MODE_KEY"))
            radioButtonDoModeHand.setChecked(true);
        else
            radioButtonDoModeAuto.setChecked(true);

        radioButtonDoShort = v.findViewById(R.id.radioButtonDoShort);
        radioButtonDoOpen = v.findViewById(R.id.radioButtonDoOpen);
        if (this.getArguments().getBoolean("DO_HAND_MODE_STATE_KEY"))
            radioButtonDoShort.setChecked(true);
        else
            radioButtonDoOpen.setChecked(true);

        radioButtonDoInversionOff = v.findViewById(R.id.radioButtonDoInversionOff);
        radioButtonDoInversionOn = v.findViewById(R.id.radioButtonDoInversionOn);
        if (this.getArguments().getBoolean("DO_INVERSION_KEY"))
            radioButtonDoInversionOn.setChecked(true);
        else
            radioButtonDoInversionOff.setChecked(true);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (DOutputParamDialog.DOutputParamDialogListener) context;
    }

    public interface DOutputParamDialogListener {
        void applyNewDoParamValues(boolean newHandModeState, boolean newHandMode, boolean newInversion, boolean dump);
    }
}
