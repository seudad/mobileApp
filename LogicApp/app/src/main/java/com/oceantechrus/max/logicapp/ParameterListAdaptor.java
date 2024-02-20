package com.oceantechrus.max.logicapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ParameterListAdaptor extends ArrayAdapter<Parameter>{

    private LayoutInflater inflater;
    private int layout;
    private List<Parameter> mParameterList;

    // constructor
    public ParameterListAdaptor(@NonNull Context context, int resource, List<Parameter> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.textViewName);
        TextView tvValue = (TextView) v.findViewById(R.id.textViewValue);

        Parameter par = mParameterList.get(position);

        // set text
        tvName.setText(par.getName());

        if (par.getValue() == "-999")
            tvValue.setText("");
        else
            tvValue.setText(par.getValue());

        return v;
    }
}
