package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class WriteFileActivity extends AppCompatActivity implements DeviceResetDialog.DeviceResetDialogListener{

    private Intent intent;
    private String path;
    private int device_address;

    static private FileInputStream fis;
    static private byte dataArray[];
    static private int dataArrayCounter;
    static private int result;
    static private int fragments;
    static private int last_fragment_length;
    static private int cur_fragment_number;
    static private boolean firmwareFlag;
    static private int loadingPercent;
    static private int state;

    static private boolean created;
    static private boolean firstPacket;

    static private int crc;

    private ProgressBar progressBar;
    private TextView textView;

    private Timer myTimer1;

    static private boolean netInitSuccessFlag;

    public static final int FRAGMENT_DATA_LENGTH = 114;
    public static final int FRAGMENT_HEADER_LENGTH = 6;

    private int badLinkCounter;

    static private int appMode;
    static private String sLocalIP;
    static private String sGlobalIP;
    static private String sPortNumber;
    static private String device_ip;

    private SharedPreferences sPref;

    private OnRequestBack<String> onRequestBackWriteRegisters;
    private OnRequestBack<String> onRequestBackWriteCoil;
    private OnRequestBack<short[]> onRequestBackReadInputRegisters;
    private OnRequestBack<String> onRequestBackModbusInit;


    public WriteFileActivity() {
        onRequestBackWriteRegisters = null;
        onRequestBackWriteCoil = null;
        onRequestBackReadInputRegisters = null;
        onRequestBackModbusInit = null;

        myTimer1 = null;
        firmwareFlag = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_file);

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        sLocalIP = sPref.getString("LOCAL_IP", "10.10.100.254");
        sGlobalIP = sPref.getString("GLOBAL_IP", "192.168.1.1");
        sPortNumber = sPref.getString("PORT_NUMBER", "8899");


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        intent = getIntent();
        path = intent.getStringExtra("file_path");
        device_ip = intent.getStringExtra("device_ip");
        device_address = 2;
        appMode = intent.getIntExtra("app_mode", 2);

        if (path.contains("logic.bin"))
            firmwareFlag = true;

        progressBar = (ProgressBar) findViewById(R.id.progressBarWaitSetpoints);
        progressBar.setVisibility(View.VISIBLE);

        textView = (TextView) findViewById(R.id.textViewWriteFile);

        if (!created)
            textView.setText("Идет загрузка 0%");


        // WriteRegister
        onRequestBackWriteRegisters = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                if (cur_fragment_number == fragments) {
                    ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                            device_address, 0x000E, 2);
                } else {
                    cur_fragment_number++;
                    makeFirmwarePacket();
                }

                badLinkCounter = 0;
            }

            @Override
            public void onFailed(String s) {
                if (badLinkCounter < 5) {
                    badLinkCounter++;
                    makeFirmwarePacket();
                } else {
                    state = 3;
                }
            }
        };

        // WriteCoil
        onRequestBackWriteCoil = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailed(String msg) {
                //Toast.makeText(getApplicationContext(), "MODBUS function 5 failed " + msg, Toast.LENGTH_SHORT).show();
            }
        };

        // ReadInputRegisters
        onRequestBackReadInputRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                if ((data[0] + (data[1]<<8)) == crc) {
                    state = 2;
                } else {
                    state = 3;
                }
            }

            @Override
            public void onFailed(String msg) {
                //Toast.makeText(getApplicationContext(), "MODBUS function 4 failed " + msg, Toast.LENGTH_SHORT).show();
                state = 3;
            }
        };

        // Modbus init
        onRequestBackModbusInit = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                netInitSuccessFlag = true;
            }

            @Override
            public void onFailed(String msg) {
                netInitSuccessFlag = false;
            }
        };

        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 500, 4000);

        if (created)
            return;

        fis = null;

        fragments = 0;
        last_fragment_length = 0;
        cur_fragment_number = 0;
        dataArrayCounter = 0;
        loadingPercent = 0;
        state = 0;
        badLinkCounter = 0;

        // Reading firmware file
        try {
            fis = new FileInputStream(path);
            dataArray = new byte[fis.available()];
            result = 0;
            do {
                result = fis.read(dataArray);
            } while (result != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // calculate CRC
        crc = 0xffff;
        for (int i = 0; i < dataArray.length; i++) {
            crc = usModbus_crc16_calc(crc, dataArray[i]);
        }

        // Calculate number of fragments, lst fragment data length
        fragments = dataArray.length/FRAGMENT_DATA_LENGTH;
        last_fragment_length = dataArray.length - fragments*FRAGMENT_DATA_LENGTH;
        if (last_fragment_length != 0)
            fragments++;

        cur_fragment_number = 1;
        dataArrayCounter = 0;
        state = 1;
        created = true;
        firstPacket = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        sLocalIP = sPref.getString("LOCAL_IP", "10.10.100.254");
        sGlobalIP = sPref.getString("GLOBAL_IP", "192.168.1.1");
        sPortNumber = sPref.getString("PORT_NUMBER", "8899");

        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 500, 4000);
    }

    private int usModbus_crc16_calc( int usCRC_temp, int usData )
    {
        int index_CC = 0;
        int nLSB     = 0;

        usCRC_temp = ( ( usCRC_temp^usData ) | 0xff00 ) & ( usCRC_temp | 0xff );

        for( index_CC = 0; index_CC < 8; index_CC++ )
        {
            nLSB = usCRC_temp & 0x0001;
            usCRC_temp >>= 1;

            if( nLSB != 0 )
            {
                usCRC_temp = usCRC_temp^0xa001;
            }
        }

        return usCRC_temp;
    }

    public void makeFirmwarePacket() {
        int fragment_length;
        int packet_length;
        short outputArray[];

        if (cur_fragment_number == fragments) {
            fragment_length = last_fragment_length;
        } else {
            fragment_length = FRAGMENT_DATA_LENGTH;
        }
        packet_length = fragment_length + FRAGMENT_HEADER_LENGTH;

        // output array
        outputArray = new short[packet_length];

        outputArray[0] = (short)cur_fragment_number;
        outputArray[1] = (short)fragments;
        outputArray[2] = 0;
        outputArray[3] = 0;
        outputArray[4] = (short)(crc & 0x00ff);
        outputArray[5] = (short) ((crc>>8) & 0x00ff);

        if (firmwareFlag)
            outputArray[2] = 1;

        if (badLinkCounter != 0)
            dataArrayCounter -= fragment_length;

        // data
        for (int i = FRAGMENT_HEADER_LENGTH; i < packet_length; i++) {
            outputArray[i] = dataArray[dataArrayCounter++];
        }

        outputArray[3] = dataArray[dataArrayCounter-1];

        ModbusReq.getInstance().writeRegisters(onRequestBackWriteRegisters,
                device_address, 0xff00, outputArray);
    }

    public void deviceResetDialog() {

        DeviceResetDialog deviceResetDialog = new DeviceResetDialog();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        deviceResetDialog.show(transaction, "dev_reset");
    }

    @Override
    public void applyTexts(String result) {
        int offset;

        if (firmwareFlag)
            offset = 4;
        else
            offset = 3;

        if (result == "ok") {
            ModbusReq.getInstance().writeCoil(onRequestBackWriteCoil,
                    device_address, offset, true);
        }
        finish();
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here
            if (!netInitSuccessFlag) {
                ModbusReq.getInstance().init(onRequestBackModbusInit);
                textView.setText("Нет подключения к сети...");
                return;
            }


            if (netInitSuccessFlag && !firstPacket) {
                makeFirmwarePacket();
                firstPacket = true;
            }

            switch (state) {
                case 0:
                    break;
                case 1:
                    loadingPercent = cur_fragment_number*100/fragments;
                    textView.setText("Идет загрузка " + Integer.toString(loadingPercent) + "%");
                    break;
                case 2:
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText("Загрузка завершена");
                    created = false;
                    state = 0;
                    deviceResetDialog();
                    break;
                case 3:
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText("Произошел сбой при загрузке");
                    created = false;
                    state = 0;
                    break;
                default:
                    break;
            }
        }
    };
}
