package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class IOActivity extends AppCompatActivity implements View.OnClickListener{

    static private int nSecurityLevel;
    static private int device_address;
    static private int appMode;
    static private String device_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);

        Intent intent = getIntent();
        appMode = intent.getIntExtra("app_mode", 2);
        nSecurityLevel = intent.getIntExtra("security_level", 0);
        device_address = intent.getIntExtra("device_address", 2);
        device_ip = intent.getStringExtra("device_ip");

        setTitle("Входы/Выходы");

        CardView uicard = (CardView) findViewById(R.id.ui_card);
        uicard.setOnClickListener(this);

        CardView dicard = (CardView) findViewById(R.id.di_card);
        dicard.setOnClickListener(this);

        CardView aocard = (CardView) findViewById(R.id.ao_card);
        aocard.setOnClickListener(this);

        CardView docard = (CardView) findViewById(R.id.do_card);
        docard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ui_card:
                // TODO: call activity
                intent = new Intent(this, SetpointsActivity.class);
                intent.putExtra("par", 5);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("security_level", nSecurityLevel);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            case R.id.di_card:
                // TODO: call activity
                intent = new Intent(this, SetpointsActivity.class);
                intent.putExtra("par", 6);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("security_level", nSecurityLevel);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            case R.id.ao_card:
                // TODO: call activity
                intent = new Intent(this, SetpointsActivity.class);
                intent.putExtra("par", 7);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("security_level", nSecurityLevel);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            case R.id.do_card:
                // TODO: call activity
                intent = new Intent(this, SetpointsActivity.class);
                intent.putExtra("par", 8);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("security_level", nSecurityLevel);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
