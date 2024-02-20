package com.oceantechrus.max.logicapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



public class IntroActivity extends AppCompatActivity implements View.OnClickListener,
        NetParamDialog.NetParamDialogListener{

    private SharedPreferences sPref;

    private RadioButton radioButtonDirect;
    private RadioButton radioButtonRS485;
    private RadioButton radioButtonRouter;

    private TextView textViewDescription;

    private ImageView imageViewIntro;

    static private int appMode;
    static private boolean largeScreen;
    static private int orientation;

    static private String sGatewayIP;
    static private String sPortNumber;
    static private String sDeviceGroupSize;
    static private Boolean bRemoteAccess;

    static private String sSSID;

    private boolean dialogOnFlag;

    WifiManager wifiManager;

    public static final int PERMISSIONS_REQUEST_CODE = 0;


    public IntroActivity() {
        dialogOnFlag = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Get WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        //checkPermissionsWifiScan();
        checkPermissionsAndSendSMS();


        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        appMode = sPref.getInt("APP_MODE", 1);

        sGatewayIP = sPref.getString("GATEWAY_IP", "192.168.1.1");
        sPortNumber = sPref.getString("PORT_NUMBER", "8899");
        sDeviceGroupSize = sPref.getString("DEV_NUMBER", "1");
        bRemoteAccess = sPref.getBoolean("REMOTE_ACCESS_KEY", false);

        // delete network
        /*sSSID = sPref.getString("SSID","");
        if (!sSSID.isEmpty()) {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("SSID", "");
            ed.commit();
        }*/

        orientation = getResources().getConfiguration().orientation;

        // Screen size in pixels
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi=(double)screenWidth/(double)dens;
        double hi=(double)screenHeight/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);

        if (screenInches >= 6.8)
            largeScreen = true;

        radioButtonDirect = (RadioButton) findViewById(R.id.buttonConnectionDirect);
        radioButtonRS485 = (RadioButton) findViewById(R.id.buttonConnectionRS485);
        radioButtonRouter = (RadioButton) findViewById(R.id.buttonConnectionRouter);

        textViewDescription = (TextView) findViewById(R.id.textIntroDescription);

        imageViewIntro = (ImageView) findViewById(R.id.imageIntro);

        radioButtonDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonDirect.setChecked(true);
                radioButtonRS485.setChecked(false);
                radioButtonRouter.setChecked(false);

                textViewDescription.setText(R.string.intro_description_AP);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.ap_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.ap_mode_100);
                }

                appMode = 1;

                int mode = sPref.getInt("APP_MODE", 1);
                if (appMode != mode) {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("APP_MODE", appMode);
                    ed.commit();
                }
            }
        });

        radioButtonRS485.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonDirect.setChecked(false);
                radioButtonRS485.setChecked(true);
                radioButtonRouter.setChecked(false);

                textViewDescription.setText(R.string.intro_description_RS485);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.rs485_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.rs485_mode_100);
                }

                appMode = 2;

                int mode = sPref.getInt("APP_MODE", 1);
                if (appMode != mode) {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("APP_MODE", appMode);
                    ed.commit();
                }
            }
        });

        radioButtonRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonDirect.setChecked(false);
                radioButtonRS485.setChecked(false);
                radioButtonRouter.setChecked(true);

                textViewDescription.setText(R.string.intro_description_STA);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.sta_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.sta_mode_100);
                }

                appMode = 3;

                int mode = sPref.getInt("APP_MODE", 1);
                if (appMode != mode) {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("APP_MODE", appMode);
                    ed.commit();
                }
            }
        });

        switch (appMode) {
            case 1:
                radioButtonDirect.setChecked(true);
                radioButtonRS485.setChecked(false);
                radioButtonRouter.setChecked(false);
                appMode = 1;

                textViewDescription.setText(R.string.intro_description_AP);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.ap_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.ap_mode_100);
                }
                break;
            case 2:
                radioButtonDirect.setChecked(false);
                radioButtonRS485.setChecked(true);
                radioButtonRouter.setChecked(false);
                appMode = 2;

                textViewDescription.setText(R.string.intro_description_RS485);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.rs485_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.rs485_mode_100);
                }
                break;
            case 3:
                radioButtonDirect.setChecked(false);
                radioButtonRS485.setChecked(false);
                radioButtonRouter.setChecked(true);
                appMode = 3;

                textViewDescription.setText(R.string.intro_description_STA);
                if (!largeScreen || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageViewIntro.setImageResource(R.drawable.sta_mode_50);
                } else {
                    imageViewIntro.setImageResource(R.drawable.sta_mode_100);
                }
                break;
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOnFlag = true;

                // data
                Bundle bundle = new Bundle();
                bundle.putString("GATEWAY_IP_KEY", sGatewayIP);
                bundle.putString("PORT_NUMBER_KEY", sPortNumber);
                bundle.putString("DEV_NUMBER", sDeviceGroupSize);
                bundle.putBoolean("REMOTE_ACCESS_KEY", bRemoteAccess);

                // dialog
                NetParamDialog netParamDialog = new NetParamDialog();
                netParamDialog.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                netParamDialog.show(transaction, "net_param");
            }
        });

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);


    }

    private void checkPermissionsAndSendSMS() {
        String permission = Manifest.permission.SEND_SMS;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //    Toast.makeText(this, "╨в╤А╨╡╨▒╤Г╨╡╤В╤Б╤П ╤А╨░╨╖╤А╨╡╤И╨╕╤В╤М ╨┤╨╛╤Б╤В╤Г╨┐", Toast.LENGTH_SHORT).show();
            //} else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            //}
        }
    }

    private void checkPermissionsWifiScan() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //    Toast.makeText(this, "Требуется разрешить доступ к сервису", Toast.LENGTH_SHORT).show();
            //} else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            //}
        } else {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    }
                } else {
                    finish();
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:

               // if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               //     ActivityCompat.requestPermissions((MainActivity)getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
               // } else {

                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("APP_MODE", appMode);
                    ed.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
               // }
                break;
        }
    }

    @Override
    public void applyNetParamTexts(String gatewayIP, String portNumber, String devNumber, Boolean remoteAccessFlag, Boolean cancelFlag) {
        if (gatewayIP.isEmpty() || portNumber.isEmpty()) {
            if (!cancelFlag) {
                Toast.makeText(getApplicationContext(), "Некорректное значение", Toast.LENGTH_SHORT).show();
            }
            dialogOnFlag = false;
            return;
        }

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        sGatewayIP = gatewayIP;
        sPortNumber = portNumber;
        sDeviceGroupSize = devNumber;
        bRemoteAccess = remoteAccessFlag;


        ed.putString("GATEWAY_IP", sGatewayIP);
        ed.putString("PORT_NUMBER", sPortNumber);
        ed.putString("DEV_NUMBER", sDeviceGroupSize);
        ed.putBoolean("REMOTE_ACCESS_KEY", bRemoteAccess);

        ed.commit();

        Toast.makeText(getApplicationContext(), "Параметры изменены", Toast.LENGTH_SHORT).show();

        dialogOnFlag = false;
    }
}
