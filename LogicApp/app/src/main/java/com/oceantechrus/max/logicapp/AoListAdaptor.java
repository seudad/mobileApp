package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AoListAdaptor extends ArrayAdapter<AOutput> {
    private LayoutInflater inflater;
    private int layout;
    private List<AOutput> mParameterList;

    // constructor
    public AoListAdaptor(@NonNull Context context, int resource, List<AOutput> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewAoName);
        TextView tvValue = (TextView) v.findViewById(R.id.textViewAoValue);
        TextView tvHandMode = (TextView) v.findViewById(R.id.textViewAoMode);
        TextView tvHandModeValue = (TextView) v.findViewById(R.id.textViewAoHandValue);
        TextView tvInversion = (TextView) v.findViewById(R.id.textViewAoInversion);
        TextView tvMin = (TextView) v.findViewById(R.id.textViewAoMin);
        TextView tvMax = (TextView) v.findViewById(R.id.textViewAoMax);

        AOutput ao = mParameterList.get(position);

        // set text
        tvName.setText(ao.getName());
        if (ao.getValue() != -999)
            tvValue.setText("текущее значение: " + String.valueOf(ao.getValue()));
        else
            tvValue.setText("текущее значение: 0");

        if (ao.isHand_mode())
            tvHandMode.setText("режим: Ручной");
        else
            tvHandMode.setText("режим: Автоматический");

        tvHandModeValue.setText("значение в ручном режиме: " + String.valueOf(ao.getHand_mode_value()));

        if (ao.isInversion())
            tvInversion.setText("инверсия: Включена");
        else
            tvInversion.setText("инверсия: Отключена");

        tvMin.setText("минимальное значение: " + String.valueOf(ao.getMin()));
        tvMax.setText("максимальное значение: " + String.valueOf(ao.getMax()));

        return v;
    }
}
