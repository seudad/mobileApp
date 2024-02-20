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
import android.widget.RadioButton;

public class SetTimeProgramDialog extends AppCompatDialogFragment {
    private String str;
    private TimeProgram timeProgram;
    private EditText editTextNewTpSecOn;
    private EditText editTextNewTpMinOn;
    private EditText editTextNewTpHourOn;
    private EditText editTextNewTpSecOff;
    private EditText editTextNewTpMinOff;
    private EditText editTextNewTpHourOff;
    private CheckBox checkBoxMon;
    private CheckBox checkBoxTue;
    private CheckBox checkBoxWed;
    private CheckBox checkBoxThu;
    private CheckBox checkBoxFri;
    private CheckBox checkBoxSat;
    private CheckBox checkBoxSun;
    private RadioButton radioButtonTpEnabled;
    private RadioButton radioButtonTpDisabled;
    private SetTimeProgramDialog.SetTimeProgramDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.time_prog_dialog, null);

        timeProgram = new TimeProgram(0,0,0,0,0,0,0,false);

        builder.setView(v);
        builder.setTitle("");
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send dump value
                timeProgram.setSec_on(65535);
                listener.applyNewTimeProgram(timeProgram);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check the text
                String str;

                str = editTextNewTpSecOn.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setSec_on(Integer.parseInt(str));

                str = editTextNewTpMinOn.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setMin_on(Integer.parseInt(str));

                str = editTextNewTpHourOn.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setHour_on(Integer.parseInt(str));

                str = editTextNewTpSecOff.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setSec_off(Integer.parseInt(str));

                str = editTextNewTpMinOff.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setMin_off(Integer.parseInt(str));

                str = editTextNewTpHourOff.getText().toString();
                if (str.isEmpty() || str.length() > 2) {
                    str = "65356";
                }

                timeProgram.setHour_off(Integer.parseInt(str));

                int weekdays = 0;
                if (checkBoxMon.isChecked())
                    weekdays |= 0x01;
                if (checkBoxTue.isChecked())
                    weekdays |= 0x02;
                if (checkBoxWed.isChecked())
                    weekdays |= 0x04;
                if (checkBoxThu.isChecked())
                    weekdays |= 0x08;
                if (checkBoxFri.isChecked())
                    weekdays |= 0x10;
                if (checkBoxSat.isChecked())
                    weekdays |= 0x20;
                if (checkBoxSun.isChecked())
                    weekdays |= 0x40;

                timeProgram.setWeekdays(weekdays);

                if (radioButtonTpEnabled.isChecked())
                    timeProgram.setEnabled(true);
                else
                    timeProgram.setEnabled(false);


                listener.applyNewTimeProgram(timeProgram);
            }
        });


        editTextNewTpSecOn = (EditText) v.findViewById(R.id.editTextTpSecOn);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_SEC_ON_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpSecOn.setText(str);

        editTextNewTpMinOn = (EditText) v.findViewById(R.id.editTextTpMinutesOn);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_MIN_ON_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpMinOn.setText(str);

        editTextNewTpHourOn = (EditText) v.findViewById(R.id.editTextTpHoursOn);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_HOUR_ON_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpHourOn.setText(str);

        editTextNewTpSecOff = (EditText) v.findViewById(R.id.editTextTpSecOff);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_SEC_OFF_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpSecOff.setText(str);

        editTextNewTpMinOff = (EditText) v.findViewById(R.id.editTextTpMinutesOff);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_MIN_OFF_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpMinOff.setText(str);

        editTextNewTpHourOff = (EditText) v.findViewById(R.id.editTextTpHoursOff);
        str = Integer.toString(this.getArguments().getInt("TIME_PROG_HOUR_OFF_KEY"));
        if (str.length() < 2) str = "0" + str;
        editTextNewTpHourOff.setText(str);

        checkBoxMon = (CheckBox) v.findViewById(R.id.checkBoxTpMon);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x01) != 0)
            checkBoxMon.setChecked(true);
        else
            checkBoxMon.setChecked(false);

        checkBoxTue = (CheckBox) v.findViewById(R.id.checkBoxTpTue);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x02) != 0)
            checkBoxTue.setChecked(true);
        else
            checkBoxTue.setChecked(false);

        checkBoxWed = (CheckBox) v.findViewById(R.id.checkBoxTpWed);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x04) != 0)
            checkBoxWed.setChecked(true);
        else
            checkBoxWed.setChecked(false);

        checkBoxThu = (CheckBox) v.findViewById(R.id.checkBoxTpThu);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x08) != 0)
            checkBoxThu.setChecked(true);
        else
            checkBoxThu.setChecked(false);

        checkBoxFri = (CheckBox) v.findViewById(R.id.checkBoxTpFri);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x10) != 0)
            checkBoxFri.setChecked(true);
        else
            checkBoxFri.setChecked(false);

        checkBoxSat = (CheckBox) v.findViewById(R.id.checkBoxTpSat);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x20) != 0)
            checkBoxSat.setChecked(true);
        else
            checkBoxSat.setChecked(false);

        checkBoxSun = (CheckBox) v.findViewById(R.id.checkBoxTpSun);
        if ((this.getArguments().getInt("TIME_PROG_WEEKDAYS_KEY") & 0x40) != 0)
            checkBoxSun.setChecked(true);
        else
            checkBoxSun.setChecked(false);

        radioButtonTpEnabled = v.findViewById(R.id.radioButtonTpEnabled);
        radioButtonTpDisabled = v.findViewById(R.id.radioButtonTpDisabled);
        if (this.getArguments().getBoolean("TIME_PROG_EN_KEY")) {
            radioButtonTpEnabled.setChecked(true);
            radioButtonTpDisabled.setChecked(false);
        } else {
            radioButtonTpDisabled.setChecked(true);
            radioButtonTpEnabled.setChecked(false);
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (SetTimeProgramDialog.SetTimeProgramDialogListener) context;
    }

    public interface SetTimeProgramDialogListener {
        void applyNewTimeProgram(TimeProgram tp);
    }
}
