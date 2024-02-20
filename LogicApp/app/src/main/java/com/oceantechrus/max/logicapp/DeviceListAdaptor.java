package com.oceantechrus.max.logicapp;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DeviceListAdaptor extends ArrayAdapter<Device> {
    private LayoutInflater inflater;
    private int layout;
    private List<Device> mDeviceList;

    // constructor
    public DeviceListAdaptor(@NonNull Context context, int resource, List<Device> mDeviceList) {
        super(context, resource, mDeviceList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mDeviceList = mDeviceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewDevName);
        TextView tvSerialNumber = (TextView) v.findViewById(R.id.textViewDevSerialNumber);
        TextView tvDevMode = (TextView) v.findViewById(R.id.textViewDevMode);
        TextView tvDevStatus = (TextView) v.findViewById(R.id.textViewDevStatus);
        TextView tvDevTime = (TextView) v.findViewById(R.id.textViewDevTime);

        Device dev = mDeviceList.get(position);

        // set name text
        String name = dev.getName();
        //name.trim();
        //if (!dev.getSsid().isEmpty()) {
        //    name += "[Сеть:" + dev.getSsid() + "]";
        //}
        tvName.setText(name);

        // set Id text
        if (dev.getId().isEmpty()) {
            tvSerialNumber.setText("");
            tvDevMode.setText("");
            tvDevMode.setTextColor(Color.DKGRAY);
            tvDevStatus.setText("");
            tvDevStatus.setTextColor(Color.DKGRAY);
            tvDevTime.setText("");
            tvDevTime.setTextColor(Color.DKGRAY);

            tvName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tvName.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);

            tvSerialNumber.setText("Серийный номер: " + dev.getId());
            tvSerialNumber.setTextColor(Color.LTGRAY);

            // set mode text
            if (dev.getSystem_status() == 1) {
                tvDevMode.setText("Режим: Включено");
                tvDevMode.setTextColor(Color.rgb(0,128,0));
            } else if (dev.getSystem_status() == 0) {
                tvDevMode.setText("Режим: Отключено");
                tvDevMode.setTextColor(Color.LTGRAY);
            } else if (dev.getSystem_status() == 3) {
                tvDevMode.setText("Режим: День");
                tvDevMode.setTextColor(Color.rgb(0,128,0));
            } else if (dev.getSystem_status() == 2) {
                tvDevMode.setText("Режим: Ночь");
                tvDevMode.setTextColor(Color.LTGRAY);
            }

            // set alarm status text
            if (dev.getAlarm_status() == 1) {
                tvDevStatus.setText("Состояние: Авария");
                tvDevStatus.setTextColor(Color.RED);
            } else {
                tvDevStatus.setText("Состояние: Норма");
                tvDevStatus.setTextColor(Color.LTGRAY);
            }

            tvDevTime.setText("Время: " + Integer.toString(dev.getHours()) + ":" + Integer.toString(dev.getMinutes()));
        }

        return v;
    }
}
