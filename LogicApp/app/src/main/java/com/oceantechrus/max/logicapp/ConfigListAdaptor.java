package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ConfigListAdaptor extends ArrayAdapter<ConfigParam> {
    private LayoutInflater inflater;
    private int layout;
    private List<ConfigParam> mParameterList;

    // constructor
    public ConfigListAdaptor(@NonNull Context context, int resource, List<ConfigParam> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewConfigParamName);
        TextView tvFlag = (TextView) v.findViewById(R.id.textViewConfigParamFlag);

        ConfigParam configParam = mParameterList.get(position);

        // set text
        tvName.setText(configParam.getName());

        if (configParam.isEnabled())
            tvFlag.setText("Есть");
        else
            tvFlag.setText("Нет");

        return v;
    }
}
