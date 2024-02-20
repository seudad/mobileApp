package com.oceantechrus.max.logicapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;


public class AlgParamDialog extends AppCompatDialogFragment {
    private CheckBox checkBoxIgnition;
    private CheckBox checkBoxChillerDisabled;
    private AlgParamDialog.AlgParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.alg_param_dialog, null);

        builder.setView(v);
        builder.setTitle("Параметры алгоритма");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyAlgParamTexts(false,false,true);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean ignition;
                boolean chillerDisabled;

                if (checkBoxIgnition.isChecked())
                    ignition = true;
                else
                    ignition = false;

                if (checkBoxChillerDisabled.isChecked())
                    chillerDisabled = true;
                else
                    chillerDisabled = false;

                listener.applyAlgParamTexts(ignition, chillerDisabled,false);
            }
        });

        checkBoxIgnition = v.findViewById(R.id.checkBoxIgnition);
        checkBoxChillerDisabled = v.findViewById(R.id.checkBoxChillerDisabled);

        if (this.getArguments().getBoolean("IGNITION_KEY"))
            checkBoxIgnition.setChecked(true);
        else
            checkBoxIgnition.setChecked(false);

        if (this.getArguments().getBoolean("CHILLER_DISABLED_KEY"))
            checkBoxChillerDisabled.setChecked(true);
        else
            checkBoxChillerDisabled.setChecked(false);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (AlgParamDialog.AlgParamDialogListener) context;
    }

    public interface AlgParamDialogListener {
        void applyAlgParamTexts(Boolean ignitionFlag, Boolean chillerDisabledFlag, Boolean cancelFlag);
    }
}
