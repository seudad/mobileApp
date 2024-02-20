package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewMainMenu extends AppCompatActivity {

    static int timeout = 3000;//3000;
    static int delay = 50;
    static int period = 4000;//4000;

    static final private int CHOOSE_PARAM = 0;

    static private int[] nRam = new int[32];
    static private int[] nInputRegistersData = new int[64];

    private ListView lvParameter;

    private MainScreenParameterListAdaptor adapter;
    private List<MainScreenParameter> mParameterList;

    static private int badLinkCounter;
    static private int nSecurityLevel;
    static private int nStartPosition;

    static private int appMode;
    static private String sLocalIP;
    static private String sGlobalIP;
    static private String sPortNumber;

    static private int device_address;
    static private int device_algorithm;
    static private String device_name;
    static private String device_ip;
    static private String port_number;
    static private int speed;
    static private String device_id;

    private OnRequestBack<short[]> onRequestBackReadHoldingRegisters;
    private OnRequestBack<short[]> onRequestBackReadInputRegisters;
    private OnRequestBack<String> onRequestBackModbusInit;

    static private boolean netInitSuccessFlag;
    static private boolean paramReadSuccessFlag;

    private Timer myTimer1;
    private ProgressBar wait_bar;

    private SharedPreferences sPref;

    private WifiManager wifiManager;

    private int memPtr;
    private int mainParCnt;


    // названия уставок
    private String[] sp_names = {
            "Резерв",                                       // 0
            "t воздуха в режиме Зима",                      // 1
            "t воздуха в режиме Лето",                      // 2
            "t перехода в режим Зима",                      // 3
            String.valueOf((char) 916) + "t перехода в режим Лето",                          // 4
            String.valueOf((char) 916) + "t перехода в режим Нагрев",                        // 5
            String.valueOf((char) 916) + "t перехода в режим Охлаждение",                    // 6
            String.valueOf((char) 916) + "t включения ТЭН",                                  // 7
            String.valueOf((char) 916) + "t отключения ТЭН",                                 // 8
            "Max t ТЭН",                                    // 9
            "Max t фреона",                                 // 10
            String.valueOf((char) 916) + "t включения продува",                              // 11
            String.valueOf((char) 916) + "t отключения продува",                             // 12
            String.valueOf((char) 916) + "t вкл.компрессора в режиме Нагрев",                // 13
            String.valueOf((char) 916) + "t откл.компрессора в режиме Нагрев",               // 14
            String.valueOf((char) 916) + "t вкл.компрессора в режиме Охлаждение",            // 15
            String.valueOf((char) 916) + "t откл.компрессора в режиме Охлаждение",           // 16
            "Y приточного вентилятора (скорость 1)",        // 17
            "Y приточного вентилятора (скорость 2)",        // 18
            "Y приточного вентилятора (скорость 3)",        // 19
            "Y приточного вентилятора (скорость 4)",        // 20
            "Y приточного вентилятора (скорость 5)",        // 21
            "Y приточного вентилятора (скорость 6)",        // 22
            "Y приточного вентилятора (скорость 7)",        // 23
            "Y вентиляторов аварийная",                     // 24
            "ΔY вентиляторов (П-В)",                        // 25
            "Min t фреона в режиме Нагрев",                 // 26
            "Min t фреона в режиме Охлаждение",             // 27
            "Y нач.компрессора",                            // 28
            "Min " + String.valueOf((char) 916) + "t фреона (Т3-Т4)",                        // 29
            "t включения режима Оттайка",                   // 30
            "t отключения режима Оттайка",                  // 31
            "t обратной воды в режиме Ожидание",            // 32
            "t угрозы замерзания по обратной воде",         // 33
            "t прогрева калорифера при уличной t 0" + String.valueOf((char) 176) + "C",      // 34
            "t прогрева калорифера при уличной t -25" + String.valueOf((char) 176) + "C",    // 35
            String.valueOf((char) 916) + "t включения регулятора по обратной воде",          // 36
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде",         // 37
            "t воздуха начальная",                          // 38
            "Y нач.клапана на теплоносителе",               // 39
            "Min t приточного воздуха",                     // 40
            "Max t приточного воздуха",                     // 41
            String.valueOf((char) 916) + "t включения ККБ", // 42
            String.valueOf((char) 916) + "t отключения ККБ",// 43
            "t наружного воздуха включения ККБ",            // 44
            "Влажность воздуха",                            // 45
            String.valueOf((char) 916) + "Rh отключения системы",// 46
            "t воздуха после испарителя",                   // 47
            "t наружного воздуха вкл.прогрева заслонки",    // 48
            "t воздуха за рекуператором",                   // 49
            "t воздуха за рекуператором аварийная",         // 50
            "Y нач.рекуператора",                           // 51
            "t воды",                                       // 52
            String.valueOf((char) 916) + "t включения продува ТЭН",                          // 53
            String.valueOf((char) 916) + "t отключения продува ТЭН",                         // 54
            "Y нач.ТЭН",                                    // 55
            "t контура отопления при t 0" + String.valueOf((char) 176) + "C", // 56
            "t контура отопления при t -25" + String.valueOf((char) 176) + "C", // 57
            "t контура ГВС",                                // 58
            "t контура вентиляции",                         // 59
            "t воздуха после увлажнителя",                  // 60
            String.valueOf((char) 916) + "Rh отключения увлажнителя",                        // 61
            "t воздуха",                                    // 62
            "t воздуха в камере смешения" + String.valueOf((char) 176) + "C",                // 63
            "Y min наружной заслонки",                      // 64
            "t max воздуха" + String.valueOf((char) 176) + "C",                              // 65
            String.valueOf((char) 916) + "t воздуха" + String.valueOf((char) 176) + "C",     // 66
            "Номер группы для фанкойла 1",                  // 67
            "Номер группы для фанкойла 2",                  // 68
            "Номер группы для фанкойла 3",                  // 69
            "Номер группы для фанкойла 4",                  // 70
            "t воздуха min",                                // 71
            "t контура ТП1 при t 5" + String.valueOf((char) 176) + "C", // 72
            "t контура ТП1 при t 0" + String.valueOf((char) 176) + "C", // 73
            "t контура ТП1 при t -25" + String.valueOf((char) 176) + "C", // 74
            "t контура ТП2 при t 5" + String.valueOf((char) 176) + "C", // 75
            "t контура ТП2 при t 0" + String.valueOf((char) 176) + "C", // 76
            "t контура ТП2 при t -25" + String.valueOf((char) 176) + "C", // 77
            "t контура ТП3 при t 5" + String.valueOf((char) 176) + "C", // 78
            "t контура ТП3 при t 0" + String.valueOf((char) 176) + "C", // 79
            "t контура ТП3 при t -25" + String.valueOf((char) 176) + "C", // 80
            "t1 воздуха в режиме Ночь",                     // 81
            "t1 воздуха в режиме День",                     // 82
            "t2 воздуха в режиме Ночь",                     // 83
            "t2 воздуха в режиме День",                     // 84
            "t3 воздуха в режиме Ночь",                     // 85
            "t3 воздуха в режиме День",                     // 86
            "t контура вентиляции при t 5" + String.valueOf((char) 176) + "C", // 87
            "t контура вентиляции при t 0" + String.valueOf((char) 176) + "C", // 88
            "t контура вентиляции при t -25" + String.valueOf((char) 176) + "C", // 89
            "t контура отопления при t 5" + String.valueOf((char) 176) + "C", // 90
            String.valueOf((char) 916) + "Y вентиляторов (П-В) режим 2",      // 91
            String.valueOf((char) 916) + "Y приточного вентилятора режим 2",  // 92
            String.valueOf((char) 916) + "t1 min воздуха",      // 93
            String.valueOf((char) 916) + "t2 min воздуха",      // 94
            String.valueOf((char) 916) + "t3 min воздуха",      // 95
            "t приточного воздуха",                             // 96
            "t воздуха в помещении",                            // 97
            String.valueOf((char) 916) + "t включения оттайки при 0" + String.valueOf((char) 176) + "C",      // 98
            String.valueOf((char) 916) + "t включения оттайки при -25" + String.valueOf((char) 176) + "C",    // 99
            "t входящей воды",                                  // 100
            String.valueOf((char) 916) + "t включения компрессора",                                           // 101
            String.valueOf((char) 916) + "t отключения компрессора",                                          // 102
            "t воды min",                                       // 103
            String.valueOf((char) 916) + "t аварии замораживания",                                            // 104
            String.valueOf((char) 916) + "t сброса аварии замораживания",                                     // 105
            "Каскадный коэффициент",                            // 106
            "t контура отопления 1 при t +5" + String.valueOf((char) 176) + "C", // 107
            "t контура отопления 1 при t -5" + String.valueOf((char) 176) + "C", // 108
            "t контура отопления 1 при t -25" + String.valueOf((char) 176) + "C", // 109
            "t контура отопления 2 при t +5" + String.valueOf((char) 176) + "C", // 110
            "t контура отопления 2 при t -5" + String.valueOf((char) 176) + "C", // 111
            "t контура отопления 2 при t -25" + String.valueOf((char) 176) + "C", // 112
            "t контура отопления 3 при t +5" + String.valueOf((char) 176) + "C", // 113
            "t контура отопления 3 при t -5" + String.valueOf((char) 176) + "C", // 114
            "t контура отопления 3 при t -25" + String.valueOf((char) 176) + "C", // 115
            "t воздуха после калорифера 1",                   // 116
            "t обратной воды 1 в режиме Ожидание",            // 117
            "t угрозы замерзания по обратной воде 1",         // 118
            "t прогрева калорифера 1 при уличной t 0" + String.valueOf((char) 176) + "C",      // 119
            "t прогрева калорифера 1 при уличной t -25" + String.valueOf((char) 176) + "C",    // 120
            String.valueOf((char) 916) + "t включения регулятора по обратной воде 1",          // 121
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде 1",         // 122
            "t воздуха начальная 1",                          // 123
            "Y нач.клапана 1 на теплоносителе",               // 124
            "t обратной воды 2 в режиме Ожидание",            // 125
            "t угрозы замерзания по обратной воде 2",         // 126
            "t прогрева калорифера 2 при уличной t 0" + String.valueOf((char) 176) + "C",      // 127
            "t прогрева калорифера 2 при уличной t -25" + String.valueOf((char) 176) + "C",    // 128
            String.valueOf((char) 916) + "t включения регулятора по обратной воде 2",          // 129
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде 2",         // 130
            "t воздуха начальная 2",                          // 131
            "Y нач.клапана 2 на теплоносителе",               // 132
            String.valueOf((char) 916) + "t включения регулятора по обратной воде 1",          // 133
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде 1",         // 134
            String.valueOf((char) 916) + "t включения регулятора по обратной воде 2",          // 135
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде 2",         // 136
            String.valueOf((char) 916) + "t включения регулятора по обратной воде 3",          // 137
            String.valueOf((char) 916) + "t отключения регулятора по обратной воде 3",         // 138
            "t перехода в режим Зима1",                           // 139
            "t перехода в режим Зима2",                           // 140
            String.valueOf((char) 916) + "t перехода в режим",    // 141
            "Min давление в контуре",                             // 142
            "t контура обходных дорожек",                         // 143
            "t контура подогрева воды",                           // 144
            "t ОВ контура отопления при t +5" + String.valueOf((char) 176) + "C", // 145
            "t ОВ контура отопления при t -5" + String.valueOf((char) 176) + "C", // 146
            "t ОВ контура отопления при t -25" + String.valueOf((char) 176) + "C", // 147
            "Y заслонки рециркуляции",                            // 148
            "Y наружной заслонки",                                // 149
            "Y нач.рег.заслонкой рециркуляции",                   // 150
            "t наружного воздуха точка 1",                        // 151
            "t наружного воздуха точка 2",                        // 152
            "t наружного воздуха точка 3",                        // 153
            "t контура отопления при t1",                         // 154
            "t контура отопления при t2",                         // 155
            "t контура отопления при t3",                         // 156
            String.valueOf((char) 916) + "t контура отопления в режиме НОЧЬ",    // 157
            "Y вкл.котла 1",                                      // 158
            "Y откл.котла 1",                                     // 159
            "Y вкл.котла 2",                                      // 160
            "Y откл.котла 2",                                     // 161
            "Y вкл.котла 3",                                      // 162
            "Y откл.котла 3",                                     // 163
            String.valueOf((char) 916) + "P1",                    // 164
            String.valueOf((char) 916) + "P2",                    // 165
            String.valueOf((char) 916) + "P3",                    // 166
            String.valueOf((char) 916) + "P4",                    // 167
            "t контура ТП",                                       // 168
            "t выходящей воды",                                   // 169
            "Max ток двигателя",                                  // 170
    };

    public NewMainMenu() {
        onRequestBackReadHoldingRegisters = null;
        onRequestBackReadInputRegisters = null;
        onRequestBackModbusInit = null;

        netInitSuccessFlag = false;
        paramReadSuccessFlag = false;

        nSecurityLevel = 3;//0;
        nStartPosition = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_main);
        setTitle("Параметры");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Get WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        final Intent intent = getIntent();
        appMode = intent.getIntExtra("app_mode", 1);
        device_algorithm = intent.getIntExtra("device_algorithm", 101);
        device_name = intent.getStringExtra("system_name");
        device_ip = intent.getStringExtra("device_ip");
        device_address = intent.getIntExtra("device_address", 2);
        port_number = intent.getStringExtra("port_number");
        speed = intent.getIntExtra("speed", 1);
        nSecurityLevel = intent.getIntExtra("security_level", 0);

        device_id = intent.getStringExtra("serial_number");
        if (device_algorithm != 986) {
            if (appMode == 2 || (Integer.parseInt(device_id) >= 1200 && Integer.parseInt(device_id) < 5000)) {
                timeout = 500;
                period = 1000;
                netInitSuccessFlag = true;
            }
        }

        wait_bar = (ProgressBar) findViewById(R.id.progressBarWaitMain);
        wait_bar.setVisibility(View.VISIBLE);



        lvParameter = (ListView) findViewById(R.id.listNewMain);
        mParameterList = new ArrayList<>();


        // add data for list
        mParameterList.add(new MainScreenParameter("", "Идет загрузка данных...", "", "",1, "", "", false));

        adapter = new MainScreenParameterListAdaptor(getApplicationContext(), R.layout.main_screen_item_list1, mParameterList);
        lvParameter.setAdapter(adapter);

        // Modbus init
        onRequestBackModbusInit = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                netInitSuccessFlag = true;
                badLinkCounter = 0;
            }

            @Override
            public void onFailed(String msg) {
                netInitSuccessFlag = false;
                if (badLinkCounter < 10)
                    badLinkCounter++;
            }
        };

        // ReadInputRegisters
        onRequestBackReadInputRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                for (int i = 0; i < nInputRegistersData.length; i++)
                    nInputRegistersData[i] = data[i];

                badLinkCounter = 0;
                paramReadSuccessFlag = true;
            }

            @Override
            public void onFailed(String msg) {
                netInitSuccessFlag = false;
                if (badLinkCounter < 10)
                    badLinkCounter++;
            }
        };

        // ReadHoldingRegisters
        onRequestBackReadHoldingRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                for (int i = 0; i < nRam.length; i++)
                    nRam[i] = data[i];

                badLinkCounter = 0;
            }

            @Override
            public void onFailed(String s) {
                netInitSuccessFlag = false;
                if (badLinkCounter < 10)
                    badLinkCounter++;
            }
        };

        lvParameter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < nStartPosition) {

                    MainScreenParameter par = mParameterList.get(position);
                    Intent questionIntent = new Intent(getApplicationContext(), NewParamChangeActivity.class);
                    questionIntent.putExtra("par_name", par.getName());
                    questionIntent.putExtra("par_val", par.getValue());
                    questionIntent.putExtra("par_units", par.getUnits());
                    questionIntent.putExtra("device_address", device_address);
                    questionIntent.putExtra("par_position", position);
                    questionIntent.putExtra("device_algorithm", device_algorithm);
                    startActivityForResult(questionIntent, CHOOSE_PARAM);

                    return;
                }

                position = position - nStartPosition;
                switch (position) {
                    case 0:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 5);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 6);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 7);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 8);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 1);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 2);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 3);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 7:
                        if (nSecurityLevel >= 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 4);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 8:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 13);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 9:
                        if (nSecurityLevel >= 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 9);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 10:
                        /*if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            startActivity(intent);
                        } else {*/
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        //}
                        break;
                    case 11:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 10);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            intent.putExtra("port_number", port_number);
                            intent.putExtra("serial_number", device_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    /*case 12:
                        if (nSecurityLevel > 2) {
                            Intent intent = new Intent(getApplicationContext(), SetpointsActivity.class);
                            intent.putExtra("par", 11);
                            intent.putExtra("app_mode", appMode);
                            intent.putExtra("security_level", nSecurityLevel);
                            intent.putExtra("device_address", device_address);
                            intent.putExtra("device_ip", device_ip);
                            intent.putExtra("device_algorithm", device_algorithm);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }
                        break;*/
                    default:
                        break;
                }
            }
        });

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

        }, delay, period);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PARAM) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra("param_back_index", 0);

                if (index == 999) {
                    speed = data.getIntExtra("param_back_val", 0);
                } else {
                    int val = data.getIntExtra("param_back_val", 0);
                    nRam[index] = val;
                }
            }
        }
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

        if (myTimer1 != null) {

            myTimer1.cancel();
            myTimer1 = null;

        } else {

            /*sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
            String sSSID = sPref.getString("SSID","");
            WifiInfo wifi_inf = wifiManager.getConnectionInfo();
            if (wifi_inf.getSSID() != sSSID) {
                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration i : list) {
                    if (i.SSID != null && i.SSID.equals("\"" + sSSID + "\"")) {

                        wifiManager.disableNetwork(wifi_inf.getNetworkId());

                        boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);

                        break;
                    }
                }
            }*/
        }

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, delay, period);
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

            if (!paramReadSuccessFlag) {
                wait_bar.setVisibility(View.VISIBLE);
            } else {
                wait_bar.setVisibility(View.INVISIBLE);
            }


            if (!netInitSuccessFlag) {

                ModbusReq.getInstance().destory();

                if (appMode == 1 || appMode == 2) {
                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost("10.10.100.254")
                            .setPort(Integer.parseInt(port_number))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(timeout)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);
                }

                if (appMode == 3) {
                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost(device_ip)
                            .setPort(Integer.parseInt(port_number))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(timeout)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);
                }

                wait_bar.setVisibility(View.VISIBLE);

                return;
            }

            if (netInitSuccessFlag) {
                ModbusReq.getInstance().readHoldingRegisters(onRequestBackReadHoldingRegisters,
                        device_address, 2304, nRam.length);

                ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                        device_address, 304, nInputRegistersData.length);
            }

            if (!paramReadSuccessFlag) return;

            mParameterList.clear();


            mParameterList.add(new MainScreenParameter("", "Основные", "",
                    "",4, "", "", false));

            if (device_algorithm == 888 || device_algorithm == 889 || device_algorithm == 991 ||device_algorithm == 201 ) {
                nStartPosition = 2;
            } else {

                mainParCnt = 0;
                for (int i = 0; i < 48; i++) {
                    switch (nInputRegistersData[i]) {
                        case 1:
                        case 2:
                        case 52:
                        case 58:
                        case 59:
                        case 62:
                        case 81:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 86:
                        case 96:
                        case 97:
                        case 100:
                        case 116:
                            mParameterList.add(new MainScreenParameter("", sp_names[nInputRegistersData[i]], Integer.toString(nRam[i]),
                                    String.valueOf((char) 176), 0, "", "", false));
                            mainParCnt++;
                            break;

                        case 45:
                            mParameterList.add(new MainScreenParameter("", sp_names[nInputRegistersData[i]], Integer.toString(nRam[i]),
                                    "%", 0, "", "", false));
                            mainParCnt++;
                            break;
                    }
                }

                mParameterList.add(new MainScreenParameter("", "Скорость", Integer.toString(speed),
                        "", 0, "", "", false));

                nStartPosition = mainParCnt + 3;
            }




            mParameterList.add(new MainScreenParameter("", "Все", "",
                    "",4, "", "", false));



            mParameterList.add(new MainScreenParameter("", "Универсальные входы", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Дискретные входы", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Аналоговые выходы", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Дискретные выходы", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Уставки", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Регуляторы", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Таймеры", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Текущие аварии", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Журнал аварий", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Дата и Время", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Графики", ">",
                    "",0, "", "", false));
            mParameterList.add(new MainScreenParameter("", "Системные", ">",
                    "",0, "", "", false));
            //mParameterList.add(new MainScreenParameter("", "Конфигурация", ">",
            //        "",0, "", "", false));



            adapter.notifyDataSetChanged();
        }
    };
}
