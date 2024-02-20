package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    static private int nSecurityLevel;
    static private int device_address;
    static private int appMode;
    static private String device_ip;

    public MenuActivity() {
        this.nSecurityLevel = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        CardView iocard = (CardView) findViewById(R.id.io_card);
        iocard.setOnClickListener(this);

        CardView spcard = (CardView) findViewById(R.id.sp_card);
        spcard.setOnClickListener(this);

        CardView rgcard = (CardView) findViewById(R.id.reg_card);
        rgcard.setOnClickListener(this);

        CardView tmcard = (CardView) findViewById(R.id.tm_card);
        tmcard.setOnClickListener(this);

        CardView alcard = (CardView) findViewById(R.id.al_card);
        alcard.setOnClickListener(this);

        CardView gcard = (CardView) findViewById(R.id.graph_card);
        gcard.setOnClickListener(this);

        CardView tdcard = (CardView) findViewById(R.id.time_date_card);
        tdcard.setOnClickListener(this);

        CardView sysCard = (CardView) findViewById(R.id.sys_card);
        sysCard.setOnClickListener(this);

        CardView configCard = (CardView) findViewById(R.id.config_card);
        configCard.setOnClickListener(this);

        CardView phoneBookCard = (CardView) findViewById(R.id.phone_book_card);
        phoneBookCard.setOnClickListener(this);

        CardView alarmJornalCard = (CardView) findViewById(R.id.journal_card);
        alarmJornalCard.setOnClickListener(this);

        intent = getIntent();
        appMode = intent.getIntExtra("app_mode", 2);
        nSecurityLevel = intent.getIntExtra("security_level", 0);
        device_address = intent.getIntExtra("device_address", 2);
        device_ip = intent.getStringExtra("device_ip");
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.io_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, IOActivity.class);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sp_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 1);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reg_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 2);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tm_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 3);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.al_card:
                // TODO: call activity
                intent = new Intent(this, SetpointsActivity.class);
                intent.putExtra("par", 4);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("security_level", nSecurityLevel);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            case R.id.graph_card:
                // TODO: call activity
                intent = new Intent(this, GraphActivity.class);
                intent.putExtra("app_mode", appMode);
                intent.putExtra("device_address", device_address);
                intent.putExtra("device_ip", device_ip);
                startActivity(intent);
                break;
            case R.id.time_date_card:
                // TODO: call activity
                if (nSecurityLevel > 1) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 9);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sys_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 10);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.config_card:
                // TODO: call activity
                Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                /*if (device_algorithm == 201 || device_algorithm == 30103 || device_algorithm == 304 || device_algorithm == 305 || device_algorithm == 306 || device_algorithm == 307 ||
                        device_algorithm == 113 || device_algorithm == 114 || device_algorithm == 309)
                    break;

                if (nSecurityLevel > 2 || appMode == 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 11);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_algorithm", device_algorithm);
                    intent.putExtra("chiller_flag", chiller_flag);
                    intent.putExtra("room_sensor_flag", room_sensor_flag);
                    intent.putExtra("fan2_flag", fan2_flag);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.phone_book_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 12);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.journal_card:
                // TODO: call activity
                if (nSecurityLevel > 2) {
                    intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 13);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
