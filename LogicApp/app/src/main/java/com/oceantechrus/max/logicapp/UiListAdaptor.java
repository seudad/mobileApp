package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UiListAdaptor extends ArrayAdapter<UInput> {
    private LayoutInflater inflater;
    private int layout;
    private List<UInput> mParameterList;

    // constructor
    public UiListAdaptor(@NonNull Context context, int resource, List<UInput> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewUiName);
        TextView tvValue = (TextView) v.findViewById(R.id.textViewUiValue);
        TextView tvHandMode = (TextView) v.findViewById(R.id.textViewUiMode);
        TextView tvHandModeValue = (TextView) v.findViewById(R.id.textViewUiHandValue);
        TextView tvOffset = (TextView) v.findViewById(R.id.textViewUiOffset);
        TextView tvOffsetR = (TextView) v.findViewById(R.id.textViewUiOffsetR);
        TextView tvMin = (TextView) v.findViewById(R.id.textViewUiMin);
        TextView tvMax = (TextView) v.findViewById(R.id.textViewUiMax);
        TextView tvSensor = (TextView) v.findViewById(R.id.textViewUiSensor);

        UInput ui = mParameterList.get(position);

        // set text
        switch (ui.getSensor()) {
            case 0:
                tvSensor.setText("тип датчика: Ni1000TK5000");
                break;
            case 1:
                tvSensor.setText("тип датчика: 0-10В");
                break;
            case 2:
                tvSensor.setText("тип датчика: 0-20мА");
                break;
            case 3:
                tvSensor.setText("тип датчика: 4-20мА");
                break;
            case 4:
                tvSensor.setText("тип датчика: Сухой контакт");
                break;
            case 5:
                tvSensor.setText("тип датчика: Pt1000");
                break;
            case 10:
                tvSensor.setText("номер канала: 1");
                break;
            case 11:
                tvSensor.setText("номер канала: 2");
                break;
            case 12:
                tvSensor.setText("номер канала: 3");
                break;
            case 13:
                tvSensor.setText("номер канала: 4");
                break;
            case 14:
                tvSensor.setText("номер канала: 5");
                break;
            case 15:
                tvSensor.setText("номер канала: 6");
                break;
            default:
                tvSensor.setText("тип датчика: Неизвестно");
                break;
        }


        tvName.setText(ui.getName());

        if (ui.getSensor() != 4) {
            if (ui.getValue() != -999)
                tvValue.setText("текущее значение: " + String.valueOf(ui.getValue()));
            else
                tvValue.setText("текущее значение: 0");

            if (ui.isHand_mode())
                tvHandMode.setText("режим: Ручной");
            else
                tvHandMode.setText("режим: Автоматический");

            tvHandModeValue.setText("значение в ручном режиме: " + String.valueOf(ui.getHand_mode_value()));
            tvOffset.setText("смещение: " + String.valueOf(ui.getOffset()));
            tvOffsetR.setText("смещение R: " + String.valueOf(ui.getOffsetR()));
            tvMin.setText("минимальное значение: " + String.valueOf(ui.getMin()));
            tvMax.setText("максимальное значение: " + String.valueOf(ui.getMax()));
        } else {
            if (ui.getValue() == 0)
                tvValue.setText("текущее состояние: Замкнуто");
            else
                tvValue.setText("текущее состояние: Разомкнуто");

            tvHandMode.setText("режим: Не используется");
            tvHandModeValue.setText("значение в ручном режиме: Не используется");
            tvOffset.setText("смещение: Не используется");
            tvOffsetR.setText("смещение R: Не используется");
            tvMin.setText("минимальное значение: Не используется");
            tvMax.setText("максимальное значение: Не используется");
        }


        return v;
    }
}
