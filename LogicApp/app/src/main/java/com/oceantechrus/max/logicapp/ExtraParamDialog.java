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

public class ExtraParamDialog extends AppCompatDialogFragment {
    private CheckBox checkBoxFire;
    private CheckBox checkBoxChiller;
    private ExtraParamDialog.ExtraParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.extra_param_dialog, null);

        builder.setView(v);
        builder.setTitle("Дополнительные параметры");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyNewExtraParam(false, false, true);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                listener.applyNewExtraParam(!checkBoxChiller.isChecked(), !checkBoxFire.isChecked(), false);
            }
        });

        checkBoxFire = v.findViewById(R.id.checkBoxFire);
        checkBoxChiller = v.findViewById(R.id.checkBoxChiller);

        if (this.getArguments().getInt("chiller_flag") == 0) {
                checkBoxChiller.setChecked(true);
        } else {
            checkBoxChiller.setChecked(false);
        }

        if (this.getArguments().getInt("fan2_flag") == 0) {
            checkBoxFire.setChecked(true);
        } else {
            checkBoxFire.setChecked(false);
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (ExtraParamDialog.ExtraParamDialogListener) context;
    }

    public interface ExtraParamDialogListener {
        void applyNewExtraParam(boolean chiller_flag, boolean fan2_flag, boolean cancel_flag);
    }
}
