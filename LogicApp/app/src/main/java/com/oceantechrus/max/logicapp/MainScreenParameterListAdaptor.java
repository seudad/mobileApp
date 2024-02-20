package com.oceantechrus.max.logicapp;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;

public class MainScreenParameterListAdaptor extends ArrayAdapter<MainScreenParameter> {
    private LayoutInflater inflater;
    private int layout;
    private List<MainScreenParameter> mParameterList;

    public int switchControlFlag;
    public int switchControl;

    // constructor
    public MainScreenParameterListAdaptor(@NonNull Context context, int resource, List<MainScreenParameter> mParameterList) {
        super(context, resource, mParameterList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.mParameterList = mParameterList;
        this.switchControlFlag = 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainScreenParameter par = mParameterList.get(position);
        TextView tvValue;
        TextView tvName;
        TextView tvSystem;
        TextView tvSystemState;
        TextView tvFanSpeed;
        final CheckBox svBlock;
        Windmill windmill_big;
        //Windmill windmill_small;
        CircleProgressView mCircleView;
        View v;


        // set text
        //tvName.setText(par.getName());

        if (par.getType() == 1) {
            this.layout = R.layout.main_screen_item_list1;
            v = inflater.inflate(this.layout, parent, false);

            /*LinearLayout constraintLayout = v.findViewById(R.id.mainScreenItem1);
            AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
            animationDrawable.setEnterFadeDuration(10);
            animationDrawable.setExitFadeDuration(5000);
            animationDrawable.start();*/

            tvValue = (TextView) v.findViewById(R.id.textViewMainValue1);
            tvName = (TextView) v.findViewById(R.id.textViewMainName1);
            tvSystem = (TextView) v.findViewById(R.id.textViewMainSystem);
            tvSystemState = (TextView) v.findViewById(R.id.textViewSystemState);


            tvSystem.setText(par.getSystem_name());
            tvSystemState.setText(par.getSystem_state());

            tvName.setTextSize(16);
            tvValue.setTextSize(72);

            if (!par.getValue().isEmpty()) {
                if (par.getUnits().isEmpty()) {
                    tvValue.setText(par.getValue());
                } else {
                    tvValue.setText(par.getValue() + par.getUnits());
                }
            }

            tvName.setText(par.getName());

        } else if (par.getType() == 0){
            this.layout = R.layout.main_screen_sensor_item_list;
            v = inflater.inflate(this.layout, parent, false);

            tvValue = (TextView) v.findViewById(R.id.textViewMainValue);
            tvName = (TextView) v.findViewById(R.id.textViewMainName);

            if (!par.getValue().isEmpty()) {
                if (par.getUnits().isEmpty()) {
                    tvValue.setText(par.getValue());
                } else {
                    if (par.getSystem_state().isEmpty()) {
                        tvValue.setText(par.getValue() + par.getUnits());
                    } else {
                        tvValue.setText(par.getValue() + par.getUnits() + "(" + par.getSystem_state() + par.getUnits() + ")");
                    }
                }
            }

            tvName.setText(par.getName());

        } else if (par.getType() == 2){
            this.layout = R.layout.main_screen_control_item_list;
            v = inflater.inflate(this.layout, parent, false);

            tvName = (TextView) v.findViewById(R.id.textViewMainControlName);
            svBlock = (CheckBox) v.findViewById(R.id.checkBoxMainControl);

            svBlock.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View buttonView) {

                    if (svBlock.isChecked()) {
                        switchControlFlag = 1;
                    } else {
                        switchControlFlag = 2;
                    }
                }
            });

            if (switchControlFlag == 0) {
                if (switchControl == 1) svBlock.setChecked(true);
                else svBlock.setChecked(false);

                tvName.setText(par.getName());
            }

        } else if (par.getType() == 3) {
            this.layout = R.layout.main_screen_fan_item;
            v = inflater.inflate(this.layout, parent, false);

            windmill_big = (Windmill) v.findViewById(R.id.windmill_big);
            windmill_big.setWindSpeed(3);
            //windmill_small = (Windmill) v.findViewById(R.id.windmill_small);
            //windmill_small.setWindSpeed(3);

            //windmill_big.startAnimation();
            //windmill_small.startAnimation();


            tvValue = (TextView) v.findViewById(R.id.textViewMainFanRateValue);
            tvName = (TextView) v.findViewById(R.id.textViewMainFanName);
            tvFanSpeed = (TextView) v.findViewById(R.id.textViewMainFanSpeed);

            tvName.setText(par.getName());
            tvFanSpeed.setText("Скорость " + par.getSystem_state());

            if (!par.getValue().isEmpty()) {
                tvValue.setText(par.getValue() + "%");
                if (!par.getValue().matches("0")) windmill_big.startAnimation();
                else windmill_big.stopAnimation();
            } else {
                windmill_big.stopAnimation();
            }

        } else if (par.getType() == 4){
            this.layout = R.layout.main_screen_header;
            v = inflater.inflate(this.layout, parent, false);

            tvName = (TextView) v.findViewById(R.id.textViewMainHeaderName);
            tvName.setText(par.getName());
        } else {
            this.layout = R.layout.main_screen_valve_item;
            v = inflater.inflate(this.layout, parent, false);

            mCircleView = (CircleProgressView) v.findViewById(R.id.circleView);
            mCircleView.setRimColor(Color.rgb(121, 128, 134));
            mCircleView.setBarColor(Color.WHITE);
            mCircleView.setTextColor(Color.WHITE);
            mCircleView.setOuterContourColor(Color.rgb(121, 128, 134));
            mCircleView.setRimWidth(20); // 20
            mCircleView.setBarWidth(20); // 20
            mCircleView.setTextSize(70);// 50

            //value setting
            mCircleView.setMaxValue(100);
            mCircleView.setMinValueAllowed(0);
            mCircleView.setValue(Integer.valueOf(par.getValue()));  // stops animation
            //mCircleView.setValueAnimated(Integer.valueOf(par.getValue()));

            //growing/rotating counter-clockwise
            mCircleView.setDirection(Direction.CW);

            tvValue = (TextView) v.findViewById(R.id.textViewMainValveValue);
            tvName = (TextView) v.findViewById(R.id.textViewMainValveName);

            tvName.setText(par.getName());

            if (!par.getValue().isEmpty())
                tvValue.setText(par.getValue() + "%");
        }

        return v;
    }
}
