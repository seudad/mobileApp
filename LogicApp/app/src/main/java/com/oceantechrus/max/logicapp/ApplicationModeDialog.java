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

public class ApplicationModeDialog extends AppCompatDialogFragment {
    private RadioButton radioButtonLocalMode;
    private RadioButton radioButtonRemoteMode;
    private RadioButton radioButtonDemoMode;
    private ApplicationModeDialog.ApplicationModeDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.app_mode_dialog, null);

        builder.setView(v);
        builder.setTitle("Режим работы приложения");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyNewApplicationMode(-999);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int newMode = 0;

                if (radioButtonLocalMode.isChecked())
                    newMode = 0;
                else

                if (radioButtonRemoteMode.isChecked())
                    newMode = 1;
                else

                if (radioButtonDemoMode.isChecked())
                    newMode = 2;

                listener.applyNewApplicationMode(newMode);
            }
        });

        radioButtonLocalMode = v.findViewById(R.id.radioButtonLocalMode);
        radioButtonRemoteMode = v.findViewById(R.id.radioButtonRemoteMode);
        radioButtonDemoMode = v.findViewById(R.id.radioButtonDemoMode);

        switch (this.getArguments().getInt("APP_MODE_KEY")) {
            case 0:
                radioButtonLocalMode.setChecked(true);
                break;
            case 1:
                radioButtonRemoteMode.setChecked(true);
                break;
            case 2:
                radioButtonDemoMode.setChecked(true);
                break;
            default:
                break;
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (ApplicationModeDialog.ApplicationModeDialogListener) context;
    }

    public interface ApplicationModeDialogListener {
        void applyNewApplicationMode(int newMode);
    }
}
