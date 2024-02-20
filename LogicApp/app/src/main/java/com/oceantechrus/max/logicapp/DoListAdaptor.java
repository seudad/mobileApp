package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DoListAdaptor extends ArrayAdapter<DOutput> {
    private LayoutInflater inflater;
    private int layout;
    private List<DOutput> mParameterList;

    // constructor
    public DoListAdaptor(@NonNull Context context, int resource, List<DOutput> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewDoName);
        TextView tvState = (TextView) v.findViewById(R.id.textViewDoState);
        TextView tvHandMode = (TextView) v.findViewById(R.id.textViewDoMode);
        TextView tvHandModeState = (TextView) v.findViewById(R.id.textViewDoHandState);
        TextView tvInversion = (TextView) v.findViewById(R.id.textViewDoInversion);

        DOutput dOutput = mParameterList.get(position);

        // set text
        tvName.setText(dOutput.getName());

        if (dOutput.getState() == 1)
            tvState.setText("текущее состояние: Замкнуто");
        else
            tvState.setText("текущее состояние: Разомкнуто");

        if (dOutput.isHand_mode())
            tvHandMode.setText("режим: Ручной");
        else
            tvHandMode.setText("режим: Автоматический");

        if (dOutput.getHand_mode_state() == 1)
            tvHandModeState.setText("сост.в ручн.режиме: Замкнуто");
        else
            tvHandModeState.setText("сост.в ручн.режиме: Разомкнуто");

        if (dOutput.isInversion())
            tvInversion.setText("инверсия: Включена");
        else
            tvInversion.setText("инверсия: Отключена");

        return v;
    }
}
