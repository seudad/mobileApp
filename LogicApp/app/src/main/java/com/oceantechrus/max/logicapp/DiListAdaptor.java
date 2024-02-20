package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DiListAdaptor extends ArrayAdapter<DInput> {
    private LayoutInflater inflater;
    private int layout;
    private List<DInput> mParameterList;

    // constructor
    public DiListAdaptor(@NonNull Context context, int resource, List<DInput> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewDiName);
        TextView tvState = (TextView) v.findViewById(R.id.textViewDiState);
        TextView tvHandMode = (TextView) v.findViewById(R.id.textViewDiMode);
        TextView tvHandModeState = (TextView) v.findViewById(R.id.textViewDiHandState);
        TextView tvInversion = (TextView) v.findViewById(R.id.textViewDiInversion);

        DInput di = mParameterList.get(position);

        // set text
        tvName.setText(di.getName());

        if (di.getState() == 0)
            tvState.setText("текущее состояние: Замкнуто");
        else
            tvState.setText("текущее состояние: Разомкнуто");

        if (di.isHand_mode())
            tvHandMode.setText("режим: Ручной");
        else
            tvHandMode.setText("режим: Автоматический");

        if (di.getHand_mode_state() == 0)
            tvHandModeState.setText("сост.в ручн.режиме: Замкнуто");
        else
            tvHandModeState.setText("сост.в ручн.режиме: Разомкнуто");

        if (di.isInversion())
            tvInversion.setText("инверсия: Включена");
        else
            tvInversion.setText("инверсия: Отключена");

        return v;
    }
}
