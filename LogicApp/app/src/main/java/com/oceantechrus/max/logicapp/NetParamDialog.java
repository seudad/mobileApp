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
import android.widget.EditText;
import android.widget.Toast;

public class NetParamDialog extends AppCompatDialogFragment {
    private EditText editTextGatewayIP;
    private EditText editTextPortNumber;
    private EditText editTextDeviceNumber;
    private CheckBox checkBoxRemoteAccess;
    private NetParamDialog.NetParamDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.net_param_dilaog, null);

        builder.setView(v);
        builder.setTitle("Параметры сети");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyNetParamTexts("", "", "", false,true);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean remoteAccess;
                String gatewayIP = editTextGatewayIP.getText().toString();
                String portNumber = editTextPortNumber.getText().toString();
                String deviceNumber = editTextDeviceNumber.getText().toString();

                if (checkBoxRemoteAccess.isChecked())
                    remoteAccess = true;
                else
                    remoteAccess = false;

                listener.applyNetParamTexts(gatewayIP, portNumber, deviceNumber, remoteAccess,false);
            }
        });

        editTextGatewayIP = v.findViewById(R.id.editTextGatewayIP);
        editTextPortNumber = v.findViewById(R.id.editTextPortNumber);
        editTextDeviceNumber = v.findViewById(R.id.editTextDeviceNumber);
        checkBoxRemoteAccess = v.findViewById(R.id.checkBoxRemoteAccess);


        editTextGatewayIP.setText(this.getArguments().getString("GATEWAY_IP_KEY", "192.168.1.1"));
        editTextPortNumber.setText(this.getArguments().getString("PORT_NUMBER_KEY", "8899"));
        editTextDeviceNumber.setText(this.getArguments().getString("DEV_NUMBER", "1"));

        if (this.getArguments().getBoolean("REMOTE_ACCESS_KEY"))
            checkBoxRemoteAccess.setChecked(true);
        else
            checkBoxRemoteAccess.setChecked(false);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (NetParamDialog.NetParamDialogListener) context;
    }

    public interface NetParamDialogListener {
        void applyNetParamTexts(String gatewayIP, String portNumber, String devNumber, Boolean remoteAccessFlag, Boolean cancelFlag);
    }
}
