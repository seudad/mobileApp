package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.FloatingActionButton;
import androidx.core.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.util.Timer;
import java.util.TimerTask;


public class MainVentActivity extends AppCompatActivity implements View.OnClickListener,
        LoginDialog.LoginDialogListener, FanSpeedNumDialog.FanSpeedNumDialogListener,
        CondFanSpeedNumDialog.FanSpeedNumDialogListener,
        SetParamDialog.SetParamDialogListener, ExtraParamDialog.ExtraParamDialogListener,
        AlgParamDialog.AlgParamDialogListener{

    private String[] login = {
            "developer", "14142",
            "svirin", "9267905278",
            "sit", "9028538383",
            "user", "1234",
            "user", "23456",
            "user", "911"
    };

    private String[] wd = {
            "Вс",
            "Пн",
            "Вт",
            "Ср",
            "Чт",
            "Пт",
            "Сб"
    };

    // массив текущих значений Ui, Yi
    // Ui
    // 0 - U1
    // 1 - U2
    // 2 - U3
    // 3 - U4
    // 4 - U5
    // 5 - U6
    // 6 - U7
    // 7 - U8
    // Yi
    // 8 -  Y1
    // 9 -  Y2
    // 10 - Y3
    // 11 - Y4

    // 12 - Y1 Name
    // 13 - Y2 Name
    // 14 - Y3 Name
    // 15 - Y4 Name

    // 16 - U1 Name
    // 17 - U2 Name
    // 18 - U3 Name
    // 19 - U4 Name
    // 20 - U5 Name
    // 21 - U6 Name
    // 22 - U7 Name
    // 23 - U8 Name

    // 24 - D1 Name
    // 25 - D2 Name
    // 26 - D3 Name
    // 27 - D4 Name
    // 28 - D5 Name
    // 29 - D6 Name
    // 30 - D7 Name
    // 31 - D8 Name

    // 32 - Q1 Name
    // 33 - Q2 Name
    // 34 - Q3 Name
    // 35 - Q4 Name
    // 36 - Q5 Name
    // 37 - Q6 Name
    // 38 - Q7 Name
    // 39 - Q8 Name
    // 40 - Q9 Name
    // 41 - Q10 Name

    //static private int[] nInputRegistersData = new int[12];
    static private int[] nInputRegistersData = new int[64];

    // массив состояний Ui, Qi, Di, Alarms
    // 0 -  U1
    // 1 -  U2
    // 2 -  U3
    // 3 -  U4
    // 4 -  U5
    // 5 -  U6
    // 6 -  U7
    // 7 -  U8

    // 8 -  Q1 насос
    // 9 -  Q2 приточный вентилятор
    // 10 - Q3 вытяжной вентилятор
    // 11 - Q4 сигнал общей аварии
    // 12 - Q5 приточная заслонка
    // 13 - Q6 вытяжная заслонка
    // 14 - Q7 ТЭН, насос орошения, обогрев заслонки
    // 15 - Q8 ККБ
    // 16 - Q9 В3, рекуператор
    // 17 - Q10 РАБОТА

    // 32 - D1 термостат
    // 33 - D2 dP приточного вентилятора
    // 34 - D3 dP приточного фильтра
    // 35 - D4 Режим ручной/автомат
    // 36 - D5 Пожар
    // 37 - D6 Ответ от насоса
    // 38 - D7 dP вытяжного вентилятора
    // 39 - D8 dP вытяжного фильтра

    // 48 - Авария датчика на U1
    // 49 - Авария насоса
    // 50 - Авария датчика на U2
    // 51 - Авария датчика на U3
    // 52 - Авария по термостату
    // 53 - Авария по обратной воде
    // 54 - Авария прогрева
    // 55 - Авария приточного вентилятора
    // 56 - Авария вытяжного вентилятора
    // 57 - Авария по приточному фильтру
    // 58 - Авария по вытяжному фильтру
    // 59 - Авария Пожар!
    // 60 - Авария датчика на U4
    // 61 - Авария датчика на U5
    // 62 - Авария рекуператора
    // 63 - Угроза обмерзания рекуператора
    // 64 - Авария ККБ
    // 65
    // 66 - Перегрев ТЭН
    // 67 - Авария вентилятора В2
    // 68 - авария вентилятора В3
    // 69
    static private boolean[] bDiscretInputsData = new boolean[70];


    // массив RAM
    // 0 - Режим Зима/Лето (0/1)
    // 1 - Текущее значение температуры перехода в Лето
    // 2 - Состояние системы (0/1)
    // 3 - Режим ручной/автомат (1/0)
    // 4 - Блокировка пуска системы со скады (0/1)
    // 5 - Блокировка пуска системы с пульта (0/1)
    // 6 -
    // 7 - Разрешение включения насоса летом (0/1)
    // 8 - Флаг работы регулятора по воде в режиме День (0/1)
    // 9 - Состояние прогрева калорифера Off/On/Completed/Failed (0/1/2/3)
    // 10 - Состояние клапана нагрева при аварии замерз
    // 11 - Состояние клапана нагрева при аварии датчика воды
    // 12 - Состояние клапана нагрева летом
    // 13
    // 14
    // 15
    // 16
    // 17
    // 18
    // 19
    static private int[] nRam = new int[32];


    static private int nRemoteStartBlockFlag;
    static private int nSecurityLevel;


    private Timer myTimer1;

    private ImageView imageFan;
    private ImageView imageFan1;
    private ImageView imageFan2;            // pump blades
    private ImageView imageFan4;            // fan2 blades
    private ImageView imageFreqConv;
    private ImageView imageAlarm;
    private ImageView imageSystemStartBlock;
    private ImageView imageWarmUp;
    private ImageView imageSeason;
    private ImageView imageYellowArrow;
    private ImageView imageBlueArrow;
    private ImageView imageOutdoor;
    private ImageView imageRoom;
    private ImageView imageChannel;
    private ImageView imageNetStatus;


    private TextView textOutdoorTemp;
    private TextView textOutdoorString;
    private TextView textTempVal;
    private TextView textChannelString;
    private TextView textWaterTemp;
    private TextView textRoomTemp;
    private TextView textMainReg;
    private TextView textChillerReg;
    private TextView textSystemState;
    private TextView textHandMode;
    private TextView textFanSpeedNum;
    private TextView textSeason;
    private TextView textWater;
    private TextView textAlarmMessage;
    private TextView textWarmUp;

    private TextView textInputName1;
    private TextView textInputName2;
    private TextView textInputName3;
    private TextView textInputName4;

    private TextView textOutputName1;
    private TextView textOutputName2;
    private TextView textOutputName3;
    private TextView textOutputName4;

    private TextView textInputValue1;
    private TextView textInputValue2;
    private TextView textInputValue3;
    private TextView textInputValue4;

    private TextView textOutputValue1;
    private TextView textOutputValue2;
    private TextView textOutputValue3;
    private TextView textOutputValue4;

    private TextView textTime;
    private TextView textDate;

    private Configuration configuration;

    private FloatingActionButton buttonEdit;

    /*private Calendar calendar;
    private String currentTime;
    private String currentDate;
    private Date date;
    private int minutes;
    private int hours;
    private int date_date;
    private int month;
    private int year;
    private int week_day;*/

    private boolean dialogOnFlag;
    private boolean largeScreen;
    private boolean newScreen;

    private int dialogId;       // 1 - temperature, 2 - fan speed

    static private int badLinkCounter;
    static private int tempBadLinkCounter;

    private ImageButton buttonMenu;
    private ImageButton buttonSwitchOnOff;
    private ImageButton buttonLogin;

    private OnRequestBack<short[]> onRequestBackReadInputRegisters;
    private OnRequestBack<boolean[]> onRequestBackReadDiscreteInputs;
    private OnRequestBack<short[]> onRequestBackReadHoldingRegisters;
    private OnRequestBack<String> onRequestBackWriteRegister;

    private OnRequestBack<String> onRequestBackModbusInit;
    static private boolean netInitSuccessFlag;


    // temporary variables
    static private int tempOutdoorValue;
    static private int tempChannelValue;
    static private int tempWaterValue;
    static private int tempRoomValue;

    static private int tempHeaterValve;
    static private int tempChillerValve;
    static private int tempFanSpeed;
    static private int tempDamperReg;
    static private int tempElHeaterReg;

    static private boolean tempPumpState;
    static private boolean tempFanState;
    static private boolean tempAlarmState;
    static private boolean tempDamperState;

    static private boolean tempHandModeFlag;

    static private int tempSeason;          // Winter, Summer
    static private int tempCondSeason;      // Auto, Ventilation, Heating, Chilling
    static private int tempSystemState;
    static private int tempOutdoorSensorFlag;
    static private int tempFanSpeedNumber;
    static private int tempRoomSensorFlag;
    static private int tempFan2Flag;
    static private int tempWarmUpFlag;
    static private int tempChillerFlag;
    static private int tempCascadeFlag;

    static private int device_address;
    static private int temp_device_address;
    static private int device_algorithm;
    static private String device_name;
    static private String device_ip;


    static private int appMode;
    static private String sLocalIP;
    static private String sGlobalIP;
    static private String sPortNumber;

    private SharedPreferences sPref;
    private boolean lowLevelFreeAccessMode;
    private boolean ignitionMode;
    private boolean chillerDisabled;

    static private int demoMessageDelayCounter;

    static private boolean bReDraw;

    static SetParamDialog tempSpDialog;
    static FanSpeedNumDialog fanSpeedNumDialog1;
    static CondFanSpeedNumDialog fanSpeedNumDialog2;

    static boolean newIntent;

    // configuration
    static boolean bWaterHeater;
    static boolean bElHeater;
    static boolean bElHeater1;
    static boolean bElHeater2;
    static boolean bElHeater3;
    static boolean bChiller;
    static boolean bHPump1;         // radiator sensors
    static boolean bHPump2;         // pipe sensors
    static boolean bOutdoorTSensor;
    static boolean bOutdoorHSensor;
    static boolean bWaterTSensor;
    static boolean bRoomTSensor;
    static boolean bRoomHSensor;
    static boolean bElHeaterTSensor;
    static boolean bChannelAfterHumTSensor;
    static boolean bChannelMixTSensor;
    static boolean bFan1;
    static boolean bFan2;
    static boolean bAnalogDamper;
    static boolean bDigitalDamper;
    static boolean bChannelTSensor;
    static boolean bChannelHSensor;
    static boolean bFan1Speed;
    static boolean bFan2Speed;
    static boolean bChillerValve;
    static boolean bHotWaterTSensor;
    static boolean bFanSpeedSwitch;
    static boolean bCompSpeed;
    static boolean bAfterRadiatorTSensor;

    static int alarmFlagIndex;
    static int fan1Index;
    static int fan2Index;
    static int handModeIndex;
    static int analogDamperIndex;
    static int digitalDamperIndex;
    static int channelAfterHumTSensorIndex;
    static int waterTSensorIndex;
    static int channelTSensorIndex;
    static int channelHSensorIndex;
    static int outdoorTSensorIndex;
    static int outdoorHSensorIndex;
    static int roomTSensorIndex;
    static int roomHSensorIndex;
    static int channelMixTSensorIndex;
    static int waterHeaterValveIndex;
    static int fan1SpeedIndex;
    static int fan2SpeedIndex;
    static int chillerValveIndex;
    static int chillerIndex;
    static int elheaterTSensorIndex;
    static int elheaterIndex;
    static int elheater1Index;
    static int elheater2Index;
    static int elheater3Index;
    static int hotWaterTSensorIndex;
    static int compIndex;
    static int compSpeedIndex;
    static int hotFreonTSensorIndex;
    static int coldFreonTSensorIndex;
    static int afterRadiatorTSensorIndex;


    private ProgressBar wait_bar;
    static private boolean paramReadSuccessFlag;


    // constructor
    public MainVentActivity() {
        myTimer1 = null;

        dialogOnFlag = false;
        largeScreen = false;

        netInitSuccessFlag = false;
        paramReadSuccessFlag = false;

        dialogId = 0;

        onRequestBackReadInputRegisters = null;
        onRequestBackReadDiscreteInputs = null;
        onRequestBackReadHoldingRegisters = null;
        onRequestBackWriteRegister = null;

        onRequestBackModbusInit = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vent_main);

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        lowLevelFreeAccessMode = sPref.getBoolean("LOW_LEVEL_FREE_ACCESS_MODE", false);

        Intent intent = getIntent();
        appMode = intent.getIntExtra("app_mode", 1);
        device_algorithm = intent.getIntExtra("device_algorithm", 101);
        device_name = intent.getStringExtra("system_name");
        device_ip = intent.getStringExtra("device_ip");
        device_address = intent.getIntExtra("device_address", 2);


        //netInitSuccessFlag = false;

        /*if (appMode == 1) {
            ModbusReq.getInstance().setParam(new ModbusParam()
                    .setHost("10.10.100.254")
                    .setPort(Integer.parseInt("8899"))
                    .setEncapsulated(false)
                    .setKeepAlive(true)
                    .setTimeout(3000)
                    .setRetries(0))
                    .init(onRequestBackModbusInit);
        }

        if (appMode == 3) {
            ModbusReq.getInstance().setParam(new ModbusParam()
                    .setHost(device_ip)
                    .setPort(Integer.parseInt("8899"))
                    .setEncapsulated(false)
                    .setKeepAlive(true)
                    .setTimeout(3000)
                    .setRetries(0))
                    .init(onRequestBackModbusInit);
        }*/





        String sTemp;

        // to redraw the screen
        newScreen = true;

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



        // Menu button
        buttonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        buttonMenu.setOnClickListener(this);

        // Switch ON/OFF button
        buttonSwitchOnOff = (ImageButton) findViewById(R.id.imageButtonSwitchOnOff);
        buttonSwitchOnOff.setOnClickListener(this);

        // LOGIN button
        buttonLogin = (ImageButton) findViewById(R.id.imageButtonLogin);
        buttonLogin.setOnClickListener(this);


        // Outdoor T sensor
        textOutdoorTemp = (TextView)findViewById(R.id.textViewOutdoorTemp);
        sTemp = Integer.toString(tempOutdoorValue);
        sTemp += String.valueOf((char)176);
        sTemp += "C";
        textOutdoorTemp.setText(sTemp);

        imageOutdoor = (ImageView) findViewById(R.id.imageViewOutdoorTemp);
        textOutdoorString = (TextView)findViewById(R.id.textViewOutdoorString);
        if (tempOutdoorSensorFlag == 0) {
            imageOutdoor.setVisibility(View.INVISIBLE);
            textOutdoorTemp.setVisibility(View.INVISIBLE);
            if (largeScreen)
            textOutdoorString.setVisibility(View.INVISIBLE);
        } else {
            imageOutdoor.setVisibility(View.VISIBLE);
            textOutdoorTemp.setVisibility(View.VISIBLE);
            if (largeScreen)
            textOutdoorString.setVisibility(View.VISIBLE);
        }

        // T sensor
        textChannelString = (TextView)findViewById(R.id.textViewChannelString);
        textTempVal = (TextView)findViewById(R.id.textViewTemp);
        textTempVal.setOnClickListener(this);
        sTemp = Integer.toString(tempChannelValue);
        sTemp += String.valueOf((char)176);
        sTemp += "C";
        textTempVal.setText(sTemp);
        textTempVal.setTextColor(Color.YELLOW);




        // HeaterValve (ChillerValve)
        if (!largeScreen) {
            textMainReg = (TextView) findViewById(R.id.textViewMainReg);
            sTemp = Integer.toString(tempHeaterValve);
            sTemp += "%";
            textMainReg.setText(sTemp);
            textMainReg.setTextColor(Color.RED);

            if (device_algorithm == 201 || device_algorithm == 108 || device_algorithm == 109) {
                textMainReg.setVisibility(View.INVISIBLE);
            }


            if (device_algorithm == 889) {
                // T2 sensor
                TextView textTempVal2 = (TextView) findViewById(R.id.textViewTemp2);
                sTemp = Integer.toString(tempChannelValue);
                sTemp += String.valueOf((char)176);
                sTemp += "C";
                textTempVal2.setText(sTemp);
                textTempVal2.setTextColor(Color.YELLOW);

                // T2 sensor
                TextView textTempVal3 = (TextView) findViewById(R.id.textViewTemp3);
                sTemp = Integer.toString(tempChannelValue);
                sTemp += String.valueOf((char)176);
                sTemp += "C";
                textTempVal3.setText(sTemp);
                textTempVal3.setTextColor(Color.YELLOW);
            } else {
                // T2 sensor
                TextView textTempVal2 = (TextView) findViewById(R.id.textViewTemp2);
                textTempVal2.setVisibility(View.INVISIBLE);

                // T2 sensor
                TextView textTempVal3 = (TextView) findViewById(R.id.textViewTemp3);
                textTempVal3.setVisibility(View.INVISIBLE);
            }
        }


        // T room sensor
        textRoomTemp = (TextView)findViewById(R.id.textViewRoomTemp);
        textRoomTemp.setOnClickListener(this);
        sTemp = Integer.toString(tempRoomValue);
        sTemp += String.valueOf((char)176);
        sTemp += "C";
        textRoomTemp.setText(sTemp);
        textRoomTemp.setTextColor(Color.YELLOW);
        textRoomTemp.setVisibility(View.INVISIBLE);

        imageRoom = (ImageView) findViewById(R.id.imageViewRoom);
        imageRoom.setVisibility(View.INVISIBLE);
        imageRoom.setOnClickListener(this);


        // Remote start block flag
        imageSystemStartBlock = (ImageView) findViewById(R.id.imageViewSystemStartBlock);
        imageSystemStartBlock.setBackgroundResource(R.drawable.icons8switchon25);

        // fan image (on top)
        imageFan = (ImageView) findViewById(R.id.imageViewFan);
        imageFan.setBackgroundResource(R.drawable.animation);
        imageFan.setOnClickListener(this);

        ImageView imageTransp1 = (ImageView) findViewById(R.id.imageViewTransp1);
        imageTransp1.setOnClickListener(this);

        AnimationDrawable anim1 = (AnimationDrawable) imageFan.getBackground();
        if (tempFanState) {
            anim1.start();
        } else {
            anim1.stop();
        }

        if (device_algorithm == 201)
            imageFan.setVisibility(View.INVISIBLE);



        if (largeScreen) {

            TextView textSystemName = (TextView) findViewById(R.id.textViewName);
            textSystemName.setText(device_name);


            // INPUT & OUTPUT VALUES
            textInputName1 = (TextView) findViewById(R.id.textViewInputName1);
            textInputName1.setVisibility(View.INVISIBLE);
            textInputName2 = (TextView) findViewById(R.id.textViewInputName2);
            textInputName2.setVisibility(View.INVISIBLE);
            textInputName3 = (TextView) findViewById(R.id.textViewInputName3);
            textInputName3.setVisibility(View.INVISIBLE);
            textInputName4 = (TextView) findViewById(R.id.textViewInputName4);
            textInputName4.setVisibility(View.INVISIBLE);

            textOutputName1 = (TextView) findViewById(R.id.textViewOutputName1);
            textOutputName1.setVisibility(View.INVISIBLE);
            textOutputName2 = (TextView) findViewById(R.id.textViewOutputName2);
            textOutputName2.setVisibility(View.INVISIBLE);
            textOutputName3 = (TextView) findViewById(R.id.textViewOutputName3);
            textOutputName3.setVisibility(View.INVISIBLE);
            textOutputName4 = (TextView) findViewById(R.id.textViewOutputName4);
            textOutputName4.setVisibility(View.INVISIBLE);

            textInputValue1 = (TextView) findViewById(R.id.textViewInputValue1);
            textInputValue1.setVisibility(View.INVISIBLE);
            textInputValue2 = (TextView) findViewById(R.id.textViewInputValue2);
            textInputValue2.setVisibility(View.INVISIBLE);
            textInputValue3 = (TextView) findViewById(R.id.textViewInputValue3);
            textInputValue3.setVisibility(View.INVISIBLE);
            textInputValue4 = (TextView) findViewById(R.id.textViewInputValue4);
            textInputValue4.setVisibility(View.INVISIBLE);

            textOutputValue1 = (TextView) findViewById(R.id.textViewOutputValue1);
            textOutputValue1.setVisibility(View.INVISIBLE);
            textOutputValue2 = (TextView) findViewById(R.id.textViewOutputValue2);
            textOutputValue2.setVisibility(View.INVISIBLE);
            textOutputValue3 = (TextView) findViewById(R.id.textViewOutputValue3);
            textOutputValue3.setVisibility(View.INVISIBLE);
            textOutputValue4 = (TextView) findViewById(R.id.textViewOutputValue4);
            textOutputValue4.setVisibility(View.INVISIBLE);


            // alarm image
            textAlarmMessage = (TextView)findViewById(R.id.textViewAlarmMessage);
            if (!tempAlarmState)
                textAlarmMessage.setVisibility(View.INVISIBLE);
            else
                textAlarmMessage.setVisibility(View.VISIBLE);

            // Fan (fan1 animation)
            imageFan1 = (ImageView) findViewById(R.id.imageViewFan2);
            imageFan1.setBackgroundResource(R.drawable.animation1);
            imageFan1.setOnClickListener(this);

            AnimationDrawable anim2 = (AnimationDrawable) imageFan1.getBackground();
            if (tempFanState) {
                anim2.start();
                imageFan1.setVisibility(View.VISIBLE);
            } else {
                anim2.stop();
                imageFan1.setVisibility(View.INVISIBLE);
            }

            if (device_algorithm == 201)
                imageFan1.setVisibility(View.INVISIBLE);




            configuration = getResources().getConfiguration();
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                // Fan2 (fan2 animation)
                imageFan4 = (ImageView) findViewById(R.id.imageViewFan4);
                imageFan4.setBackgroundResource(R.drawable.animation1);

                // Damper 2
                ImageView imageSystem2 = (ImageView) findViewById(R.id.imageViewSystem1);
                imageSystem2.setBackgroundResource(R.drawable.out1);
                imageSystem2.setOnClickListener(this);

                if (device_algorithm == 201) {
                    imageFan4.setVisibility(View.INVISIBLE);
                    imageSystem2.setVisibility(View.INVISIBLE);
                }

                buttonEdit = (FloatingActionButton) findViewById(R.id.editMainVentButton);
                if (device_algorithm == 10201) {
                    buttonEdit.setVisibility(View.VISIBLE);
                    buttonEdit.setOnClickListener(this);
                } else {
                    buttonEdit.setVisibility(View.INVISIBLE);
                }
            }

            // SYSTEM image
            ImageView imageSystem = (ImageView) findViewById(R.id.imageViewSystem);
            imageSystem.setOnClickListener(this);


            // Damper regulators
            configuration = getResources().getConfiguration();
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TextView textDamperReg = (TextView) findViewById(R.id.textViewDamperReg);
                sTemp = Integer.toString(tempDamperReg);
                sTemp += "%";
                textDamperReg.setText(sTemp);
                textDamperReg.setTextColor(Color.YELLOW);
                textDamperReg.setVisibility(View.INVISIBLE);
            }

            // Arrow images
            imageYellowArrow = (ImageView) findViewById(R.id.imageViewYellowArrow);
            imageBlueArrow = (ImageView) findViewById(R.id.imageViewBlueArrow);
            imageYellowArrow.setVisibility(View.INVISIBLE);
            imageBlueArrow.setVisibility(View.INVISIBLE);

            if (!tempFanState || device_algorithm == 201) {
                imageYellowArrow.setVisibility(View.INVISIBLE);
                imageBlueArrow.setVisibility(View.INVISIBLE);
            } else {
                imageYellowArrow.setVisibility(View.VISIBLE);
                imageBlueArrow.setVisibility(View.VISIBLE);
            }

            // text "ручной/автомат"
            // Hand Mode Switch
            textHandMode = (TextView) findViewById(R.id.textViewHandMode);
            textHandMode.setTextColor(Color.GREEN);
            textHandMode.setText("автомат");

        } else {
            textWater = (TextView) findViewById(R.id.textViewWater);
            textWater.setVisibility(View.INVISIBLE);

            imageChannel = (ImageView) findViewById(R.id.imageViewTemp);
            imageChannel.setOnClickListener(this);
        }

        // fan speed number
        textFanSpeedNum = (TextView)findViewById(R.id.textViewFanSpeedNum);
        if (!tempFanState) {
            sTemp = "[Откл]";
            textFanSpeedNum.setText(sTemp);
        } else {
            sTemp = Integer.toString(nRam[13]) + "(";
            sTemp += Integer.toString(tempFanSpeed) + "%)";
            textFanSpeedNum.setText(sTemp);
        }

        if (device_algorithm == 201 || device_algorithm == 10004) {
            textFanSpeedNum.setVisibility(View.INVISIBLE);
        }

        // alarm image
        imageAlarm = (ImageView)findViewById(R.id.imageViewAlarm);
        imageAlarm.setOnClickListener(this);
        if (!tempAlarmState)
            imageAlarm.setVisibility(View.INVISIBLE);
        else
            imageAlarm.setVisibility(View.VISIBLE);

        ImageView imageTransp2 = (ImageView) findViewById(R.id.imageViewTransp2);
        imageTransp2.setOnClickListener(this);

        // warm up image
        imageWarmUp = (ImageView) findViewById(R.id.imageViewWarmUpMode);
        textWarmUp = (TextView) findViewById(R.id.textViewWarmUp);
        if (tempWarmUpFlag != 1 || device_algorithm == 201) {
            imageWarmUp.setVisibility(View.INVISIBLE);
            textWarmUp.setVisibility(View.INVISIBLE);
        } else {
            imageWarmUp.setVisibility(View.VISIBLE);
            textWarmUp.setVisibility(View.VISIBLE);
        }


        // season image
        imageSeason = (ImageView) findViewById(R.id.imageViewSeason);
        if (tempCondSeason == 0)
            imageSeason.setBackgroundResource(R.drawable.icons8sun25);
        else
            imageSeason.setBackgroundResource(R.drawable.icons8snow25);

        // Season text
        textSeason = (TextView) findViewById(R.id.textViewSeason);
        if (tempSeason == 1) {
            textSeason.setText("Лето");
            if (!largeScreen)
                textSeason.setTextColor(Color.YELLOW);
        } else {
            textSeason.setText("Зима");
            if (!largeScreen)
                textSeason.setTextColor(Color.CYAN);
        }

        if (tempOutdoorSensorFlag == 0) {
            if (!largeScreen)
                textSeason.setVisibility(View.INVISIBLE);
        } else {
            if (!largeScreen)
                textSeason.setVisibility(View.VISIBLE);
        }

        if (device_algorithm == 30101)
            textSeason.setVisibility(View.INVISIBLE);

        // system state text
        textSystemState = (TextView) findViewById(R.id.textViewSystemState);
        if (tempSystemState == 0)
            textSystemState.setText("Откл.");
        else
            textSystemState.setText("Вкл.");


        // net status image
        imageNetStatus = (ImageView) findViewById(R.id.imageViewNetState);
        if (tempBadLinkCounter > 5)
            imageNetStatus.setBackgroundResource(R.drawable.icons8wifioff25);
        else
            imageNetStatus.setBackgroundResource(R.drawable.icons8wifion25);



        wait_bar = (ProgressBar) findViewById(R.id.progressBarWaitSetpoints);
        wait_bar.setVisibility(View.VISIBLE);


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

        // ReadDiscreteInputs
        onRequestBackReadDiscreteInputs = new OnRequestBack<boolean[]>() {
            @Override
            public void onSuccess(boolean[] booleans) {
                for (int i = 0; i < bDiscretInputsData.length; i++)
                    bDiscretInputsData[i] = booleans[i];

                badLinkCounter = 0;
            }

            @Override
            public void onFailed(String s) {
                //Toast.makeText(getApplicationContext(), "MODBUS function 2 failed " + s, Toast.LENGTH_SHORT).show();
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

        // WriteRegister
        onRequestBackWriteRegister = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailed(String s) {
                //Toast.makeText(getApplicationContext(), "MODBUS function 6 failed " + s, Toast.LENGTH_SHORT).show();
            }
        };

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

        },500 , 4000);

        if (device_algorithm == 10201) {
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (netInitSuccessFlag) {
                        dialogOnFlag = true;

                        // data
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("IGNITION_KEY", ignitionMode);
                        bundle.putBoolean("CHILLER_DISABLED_KEY", chillerDisabled);

                        // dialog
                        AlgParamDialog algParamDialog = new AlgParamDialog();
                        algParamDialog.setArguments(bundle);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        algParamDialog.show(transaction, "alg_param");
                    }
                }
            });
        }
    }


    public void openLoginDialog() {
        dialogOnFlag = true;
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(getSupportFragmentManager(), "login");
    }

    public void openFanSpeedNumDialog() {
        dialogOnFlag = true;

        // data
        Bundle bundle = new Bundle();
        bundle.putInt("FAN_SPEED_NUM_KEY", nRam[13]);

        // dialog
        dialogId = 3;
        fanSpeedNumDialog2 = new CondFanSpeedNumDialog();
        fanSpeedNumDialog2.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fanSpeedNumDialog2.show(transaction, "fanspeednum");
    }

    public void openTempSetpointDialog() {
        dialogOnFlag = true;
        dialogId = 1;

        // data
        Bundle bundle = new Bundle();
        bundle.putInt("PARAM_VAL_KEY", nRam[23]);
        bundle.putInt("SEASON_KEY", nRam[0]);
        bundle.putInt("ALG_KEY", device_algorithm);
        bundle.putBoolean("MAIN_SCREEN_KEY", true);

        // dialog
        /*SetParamDialog */tempSpDialog = new SetParamDialog();
        tempSpDialog.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        tempSpDialog.show(transaction, "param");
    }

    public void openExtraParamDialog() {
        dialogOnFlag = true;

        // data
        Bundle bundle = new Bundle();
        bundle.putInt("fan2_flag", tempFan2Flag);
        bundle.putInt("chiller_flag", tempChillerFlag);

        // dialog
        ExtraParamDialog extraParamDialog = new ExtraParamDialog();
        extraParamDialog.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        extraParamDialog.show(transaction, "extra_param");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }

        /*if (netInitSuccessFlag) {
            ModbusReq.getInstance().destory();
            netInitSuccessFlag = false;
        }
        newIntent = true;*/
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void onClick(View v) {
        if (dialogOnFlag) {
            switch (dialogId) {
                case 1:
                    tempSpDialog.dismiss();
                    dialogId = 0;
                    dialogOnFlag = false;
                    break;
                case 2:
                    fanSpeedNumDialog1.dismiss();
                    dialogId = 0;
                    dialogOnFlag = false;
                    break;
                case 3:
                    fanSpeedNumDialog2.dismiss();
                    dialogId = 0;
                    dialogOnFlag = false;
                    break;
                default:
                    return;
            }
        }

        switch (v.getId()){
            case R.id.imageButtonMenu:
                // TODO: call activity
                if (nSecurityLevel != 0) {
                    //if (netInitSuccessFlag) {
                        //ModbusReq.getInstance().destory();
                        //netInitSuccessFlag = false;
                    //}
                    Intent intent = new Intent(this, MenuActivity.class);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    intent.putExtra("device_address", device_address);
                    startActivity(intent);

                    //newIntent = true;
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageButtonSwitchOnOff:
                if (nSecurityLevel != 0 || lowLevelFreeAccessMode) {
                    if (nRemoteStartBlockFlag == 0) {
                        nRemoteStartBlockFlag = 1;
                        imageSystemStartBlock.setBackgroundResource(R.drawable.icons8switchoff25);
                    } else {
                        nRemoteStartBlockFlag = 0;
                        imageSystemStartBlock.setBackgroundResource(R.drawable.icons8switchon25);
                    }


                    ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                            device_address, 3077, nRemoteStartBlockFlag);

                    ModbusReq.getInstance().readHoldingRegisters(onRequestBackReadHoldingRegisters,
                            device_address, 3072, 14);
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewTemp:
                if (device_algorithm == 202)
                    break;

                if (nSecurityLevel != 0 || lowLevelFreeAccessMode) {
                    if (tempCascadeFlag == 0 && !(nRam[0] != 0 && (device_algorithm == 102 || (device_algorithm >= 10201 && device_algorithm <= 10299))))
                        openTempSetpointDialog();
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.textViewRoomTemp:
            case R.id.imageViewRoom:
                if (device_algorithm == 202)
                    break;

                if (nSecurityLevel != 0) {
                    if (tempCascadeFlag != 0 ||
                            (nRam[0] != 0 && (device_algorithm == 102 || (device_algorithm >= 10201 && device_algorithm <= 10299))))

                        openTempSetpointDialog();
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageViewAlarm:
            case R.id.imageViewTransp2:
                if (nSecurityLevel != 0 || lowLevelFreeAccessMode) {
                    //if (netInitSuccessFlag) {
                        //ModbusReq.getInstance().destory();
                        //netInitSuccessFlag = false;
                    //}
                    Intent intent = new Intent(this, SetpointsActivity.class);
                    intent.putExtra("par", 4);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("security_level", nSecurityLevel);
                    intent.putExtra("device_address", device_address);
                    intent.putExtra("device_ip", device_ip);
                    startActivity(intent);

                    //newIntent = true;
                } else {
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageButtonLogin:
                openLoginDialog();
                break;
            case R.id.imageViewFan:
            case R.id.imageViewTransp1:
                if (device_algorithm == 202 || bFanSpeedSwitch)
                    break;

                if (nSecurityLevel != 0 || lowLevelFreeAccessMode)
                    openFanSpeedNumDialog();
                else
                    Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editMainVentButton:
                if (nSecurityLevel != 0)
                    openExtraParamDialog();
                break;
            default:
                break;
        }

        if (largeScreen) {
            if (v.getId() == R.id.imageViewSystem || v.getId() == R.id.imageViewSystem1) {
                if (device_algorithm != 201 && device_algorithm != 108) {

                    if (nSecurityLevel != 0 || lowLevelFreeAccessMode) {
                        if (tempCascadeFlag == 0 && !(nRam[0] != 0 && (device_algorithm == 102 || (device_algorithm >= 10201 && device_algorithm <= 10299))))
                            openTempSetpointDialog();
                    } else {
                        Toast.makeText(this, "Нет доступа", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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

                //if (!newIntent)
                    ModbusReq.getInstance().destory();
                //else
                //    newIntent = false;

                if (appMode == 1 || appMode == 2) {
                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost("10.10.100.254")
                            .setPort(Integer.parseInt("8899"))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(3000)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);
                }

                if (appMode == 3) {
                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost(device_ip)
                            .setPort(Integer.parseInt("8899"))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(3000)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);
                }

                wait_bar.setVisibility(View.VISIBLE);
            }

            if (netInitSuccessFlag) {
                ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                        device_address, 256, nInputRegistersData.length);

                ModbusReq.getInstance().readDiscreteInput(onRequestBackReadDiscreteInputs,
                        device_address, 256, bDiscretInputsData.length);

                ModbusReq.getInstance().readHoldingRegisters(onRequestBackReadHoldingRegisters,
                        device_address, 3072, nRam.length);
            }




            if (nRam[7] == 1) {
                chillerDisabled = false;
            } else {
                chillerDisabled = true;
            }

            if (nRam[22] == 1) {
                ignitionMode = false;
            } else  {
                ignitionMode = true;
            }



            // determine configuration
            for (int i = 12; i < 16; i++) {
                switch (nInputRegistersData[i]) {
                    case 1:
                        // heater valve
                        bWaterHeater = true;
                        waterHeaterValveIndex = i-12;
                        break;
                    case 2:
                        // chiller valve
                        bChiller = true;
                        bChillerValve = true;
                        chillerValveIndex = i-12;
                        break;
                    case 3:
                        // fan1 speed
                        bFan1Speed = true;
                        fan1SpeedIndex = i-12;
                        break;
                    case 4:
                        // fan2 speed
                        bFan2Speed = true;
                        fan2SpeedIndex = i-12;
                        break;
                    case 5:
                        // fan speed
                        bFan1Speed = true;
                        bFan2Speed = true;
                        fan1SpeedIndex = i-12;
                        fan2SpeedIndex = i-12;
                        break;
                    case 6:
                        // comp speed
                        bCompSpeed = true;
                        compSpeedIndex = i-12;
                        break;
                    case 7:
                        // analog damper
                        bAnalogDamper = true;
                        analogDamperIndex = i-12;
                        break;
                    case 12:
                        // elheater
                        bElHeater = true;
                        break;
                }
            }

            for (int i = 16; i < 24; i++) {
                switch (nInputRegistersData[i]) {
                    case 1:
                        // outdoor t sensor
                        bOutdoorTSensor = true;
                        outdoorTSensorIndex = i-16;
                        break;
                    case 2:
                        // channel t sensor
                        bChannelTSensor = true;
                        channelTSensorIndex = i-16;
                        break;
                    case 3:
                        // HPump2
                        bHPump2 = true;
                        hotFreonTSensorIndex = i-16;
                        break;
                    case 4:
                        // HPump2
                        bHPump2 = true;
                        coldFreonTSensorIndex = i-16;
                        break;
                    case 5:
                    case 19:
                        // room t sensor
                        bRoomTSensor = true;
                        roomTSensorIndex = i-16;
                        break;
                    case 6:
                        // water t sensor
                        bWaterTSensor = true;
                        bWaterHeater = true;
                        waterTSensorIndex = i-16;
                        break;
                    case 7:
                    case 8:
                        // HPump1
                        bHPump1 = true;
                        break;
                    case 9:
                    case 10:
                        bFanSpeedSwitch = true;
                        break;
                    case 15:
                        // water t sensor
                        bElHeaterTSensor = true;
                        elheaterTSensorIndex = i-16;
                        break;

                    case 21:
                        // t sensor after radiator
                        bAfterRadiatorTSensor = true;
                        afterRadiatorTSensorIndex = i-16;
                        break;
                    case 24:
                        // outdoor rh sensor
                        bOutdoorHSensor = true;
                        outdoorHSensorIndex = i-16;
                        break;
                    case 25:
                        // channel rh sensor
                        bChannelHSensor = true;
                        channelHSensorIndex = i-16;
                        break;
                    case 26:
                    case 27:
                        // room rh sensor
                        bRoomHSensor = true;
                        roomHSensorIndex = i-16;
                        break;
                    case 38:
                        // hot water sensor
                        bHotWaterTSensor = true;
                        hotWaterTSensorIndex = i-16;
                        break;
                    case 44:
                        // channel t sensor after hum
                        bChannelAfterHumTSensor = true;
                        channelAfterHumTSensorIndex = i-16;
                        break;
                    case 46:
                        // channel t sensor in mix box
                        bChannelMixTSensor = true;
                        channelMixTSensorIndex = i-16;
                        break;
                }
            }

            for (int i = 24; i < 32; i++) {
                switch (nInputRegistersData[i]) {
                    case 7:
                        // hand mode
                        handModeIndex = i-24;
                        break;
                }
            }

            for (int i = 32; i < 42; i++) {
                switch (nInputRegistersData[i]) {
                    case 2:
                        // digital damper
                        bDigitalDamper = true;
                        digitalDamperIndex = i-32;
                        break;
                    case 3:
                        // fan1
                        bFan1 = true;
                        fan1Index = i-32;
                        break;
                    case 4:
                        // fan 2
                        bFan2 = true;
                        fan2Index = i-32;
                        break;
                    case 5:
                        // compressor
                        bHPump1 = true;
                        compIndex = i-32;
                        break;
                    case 7:
                        // compressor
                        bHPump2 = true;
                        break;
                    case 9:
                        // elheater
                        bElHeater = true;
                        elheaterIndex = i-32;
                        break;
                    case 10:
                        // elheater
                        bElHeater = true;
                        break;
                    case 11:
                        // elheater
                        bElHeater = true;
                        bElHeater1 = true;
                        elheater1Index = i-32;
                        break;
                    case 12:
                        // elheater
                        bElHeater = true;
                        bElHeater2 = true;
                        elheater2Index = i-32;
                        break;
                    case 13:
                        // elheater
                        bElHeater = true;
                        bElHeater3 = true;
                        elheater3Index = i-32;
                        break;
                    case 23:
                    case 25:
                        // chiller
                        bChiller = true;
                        chillerIndex = i-32;
                        break;
                    case 15:
                        alarmFlagIndex = i-32;
                        break;
                }
            }

            String sTemp;
            newScreen = true;   //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


            if (largeScreen && device_algorithm == 900) {

                ImageView imageSystem = (ImageView) findViewById(R.id.imageViewSystem);
                imageSystem.setVisibility(View.INVISIBLE);

                // temperature 1
                double f;
                f = ((float)(nInputRegistersData[0]))/10.0;
                sTemp = Double.toString(f);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textInputValue1.setText(sTemp);
                textInputValue1.setVisibility(View.VISIBLE);
                textInputName1.setText(" t1 воздуха");
                textInputName1.setVisibility(View.VISIBLE);

                // temperature 2
                f = ((float)(nInputRegistersData[2]))/10.0;
                sTemp = Double.toString(f);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textInputValue2.setText(sTemp);
                textInputValue2.setVisibility(View.VISIBLE);
                textInputName2.setText(" t2 воздуха");
                textInputName2.setVisibility(View.VISIBLE);

                // temperature 3
                f = ((float)(nInputRegistersData[4]))/10.0;
                sTemp = Double.toString(f);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textInputValue3.setText(sTemp);
                textInputValue3.setVisibility(View.VISIBLE);
                textInputName3.setText(" t3 воздуха");
                textInputName3.setVisibility(View.VISIBLE);

                // temperature 4
                f = ((float)(nInputRegistersData[6]))/10.0;
                sTemp = Double.toString(f);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textInputValue4.setText(sTemp);
                textInputValue4.setVisibility(View.VISIBLE);
                textInputName4.setText(" t4 воздуха");
                textInputName4.setVisibility(View.VISIBLE);

                // humidity 1
                f = ((float)(nInputRegistersData[1]))/10.0;
                sTemp = Double.toString(f);
                sTemp += "%";
                textOutputValue1.setText(sTemp);
                textOutputValue1.setVisibility(View.VISIBLE);
                textOutputName1.setText(" Влажность1");
                textOutputName1.setVisibility(View.VISIBLE);

                // humidity 1
                f = ((float)(nInputRegistersData[3]))/10.0;
                sTemp = Double.toString(f);
                sTemp += "%";
                textOutputValue2.setText(sTemp);
                textOutputValue2.setVisibility(View.VISIBLE);
                textOutputName2.setText(" Влажность2");
                textOutputName2.setVisibility(View.VISIBLE);

                // humidity 1
                f = ((float)(nInputRegistersData[5]))/10.0;
                sTemp = Double.toString(f);
                sTemp += "%";
                textOutputValue3.setText(sTemp);
                textOutputValue3.setVisibility(View.VISIBLE);
                textOutputName3.setText(" Влажность3");
                textOutputName3.setVisibility(View.VISIBLE);

                // humidity 1
                f = ((float)(nInputRegistersData[7]))/10.0;
                sTemp = Double.toString(f);
                sTemp += "%";
                textOutputValue4.setText(sTemp);
                textOutputValue4.setVisibility(View.VISIBLE);
                textOutputName4.setText(" Влажность4");
                textOutputName4.setVisibility(View.VISIBLE);

            }


            // FAN2 FLAG
            if (largeScreen) {
                configuration = getResources().getConfiguration();
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ImageView imageSystem2 = (ImageView) findViewById(R.id.imageViewSystem1);

                    if (!bFan2 || device_algorithm == 201) {
                        imageSystem2.setVisibility(View.INVISIBLE);
                    } else {
                        imageSystem2.setVisibility(View.VISIBLE);

                        if (bAnalogDamper) {
                            if (nInputRegistersData[analogDamperIndex + 8] == 0)
                                imageSystem2.setBackgroundResource(R.drawable.out1);
                            else
                                imageSystem2.setBackgroundResource(R.drawable.out2);
                        } else if (bDigitalDamper) {
                            if (!bDiscretInputsData[digitalDamperIndex + 8])
                                imageSystem2.setBackgroundResource(R.drawable.out1);
                            else
                                imageSystem2.setBackgroundResource(R.drawable.out2);
                        } else {
                            if (!bDiscretInputsData[fan1Index + 8])
                                imageSystem2.setBackgroundResource(R.drawable.out1);
                            else
                                imageSystem2.setBackgroundResource(R.drawable.out2);
                        }
                    }
                }
            }

            // ROOM SENSOR FLAG
            if (!bRoomTSensor || ((bRoomTSensor && !bChannelTSensor))) {
                textRoomTemp.setVisibility(View.INVISIBLE);
                imageRoom.setVisibility(View.INVISIBLE);
            } else {
                textRoomTemp.setVisibility(View.VISIBLE);
                imageRoom.setVisibility(View.VISIBLE);
            }



            // OUTDOOR SENSOR FLAG
            if (!bOutdoorTSensor) {
                imageOutdoor.setVisibility(View.INVISIBLE);
                textOutdoorTemp.setVisibility(View.INVISIBLE);

                if (!largeScreen)
                    textSeason.setVisibility(View.INVISIBLE);
                else
                    textOutdoorString.setVisibility(View.INVISIBLE);
            } else {
                imageOutdoor.setVisibility(View.VISIBLE);
                textOutdoorTemp.setVisibility(View.VISIBLE);

                if (!largeScreen)
                    textSeason.setVisibility(View.VISIBLE);
                else
                    textOutdoorString.setVisibility(View.VISIBLE);
            }

            // ALARM
            if (device_algorithm != 319 && device_algorithm != 30304) {
                if (bDiscretInputsData[alarmFlagIndex + 8]) {
                    imageAlarm.setVisibility(View.VISIBLE);
                    if (largeScreen)
                        textAlarmMessage.setVisibility(View.VISIBLE);
                } else {
                    imageAlarm.setVisibility(View.INVISIBLE);
                    if (largeScreen)
                        textAlarmMessage.setVisibility(View.INVISIBLE);
                }
            }


            // FAN
            AnimationDrawable anim1 = (AnimationDrawable) imageFan.getBackground();
            if (bFan1 || bFan2 || bFan1Speed || bFan2Speed) {
                if (device_algorithm == 319) {
                    if (bDiscretInputsData[digitalDamperIndex + 8]) {
                        anim1.start();
                    } else {
                        anim1.stop();
                    }
                } else {
                    if (bDiscretInputsData[fan1Index + 8]) {
                        anim1.start();
                    } else {
                        anim1.stop();
                    }
                }
            } else {
                anim1.setVisible(false, false);
            }

            if (largeScreen) {
                imageFan1 = (ImageView) findViewById(R.id.imageViewFan2);
                AnimationDrawable anim2 = (AnimationDrawable) imageFan1.getBackground();

                if (device_algorithm == 319) {
                    if (bDiscretInputsData[digitalDamperIndex + 8]) {
                        anim2.start();
                        imageFan1.setVisibility(View.VISIBLE);
                    } else {
                        anim2.stop();
                        imageFan1.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (bDiscretInputsData[fan1Index + 8]) {
                        anim2.start();
                        imageFan1.setVisibility(View.VISIBLE);
                    } else {
                        anim2.stop();
                        imageFan1.setVisibility(View.INVISIBLE);
                    }
                }

                configuration = getResources().getConfiguration();
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Fan2
                    imageFan4 = (ImageView) findViewById(R.id.imageViewFan4);
                    AnimationDrawable anim4 = (AnimationDrawable) imageFan4.getBackground();

                    if (device_algorithm == 319) {
                        if (bFan2 && bDiscretInputsData[digitalDamperIndex + 8]) {
                            anim4.start();
                            imageFan4.setVisibility(View.VISIBLE);
                        } else {
                            anim4.stop();
                            imageFan4.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (bFan2 && bDiscretInputsData[fan2Index + 8]) {
                            anim4.start();
                            imageFan4.setVisibility(View.VISIBLE);
                        } else {
                            anim4.stop();
                            imageFan4.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                // Arrow images
                imageYellowArrow = (ImageView) findViewById(R.id.imageViewYellowArrow);
                imageBlueArrow = (ImageView) findViewById(R.id.imageViewBlueArrow);

                if (device_algorithm == 319) {
                    if (!bDiscretInputsData[digitalDamperIndex + 8]) {
                        imageYellowArrow.setVisibility(View.INVISIBLE);
                        imageBlueArrow.setVisibility(View.INVISIBLE);
                    } else {
                        imageYellowArrow.setVisibility(View.VISIBLE);
                        imageBlueArrow.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!bDiscretInputsData[fan1Index + 8]) {
                        imageYellowArrow.setVisibility(View.INVISIBLE);
                        imageBlueArrow.setVisibility(View.INVISIBLE);
                    } else {
                        imageYellowArrow.setVisibility(View.VISIBLE);
                        imageBlueArrow.setVisibility(View.VISIBLE);
                    }
                }
            }

            // Tab screen
            if (largeScreen) {

                // text "ручной/автомат"
                // Hand Mode Switch
                if (bDiscretInputsData[handModeIndex + 32] != tempHandModeFlag || newScreen) {
                    textHandMode = (TextView) findViewById(R.id.textViewHandMode);
                    if (bDiscretInputsData[handModeIndex + 32]) {
                        textHandMode.setTextColor(Color.RED);
                        textHandMode.setText("ручной");
                    } else {
                        textHandMode.setTextColor(Color.GREEN);
                        textHandMode.setText("автомат");
                    }

                    tempHandModeFlag = bDiscretInputsData[handModeIndex + 32];
                }

                // Damper 1
                ImageView imageSystem = (ImageView) findViewById(R.id.imageViewSystem);

                // water heater system
                if (bWaterHeater && !bChiller && !bElHeater && !bHPump1 && !bHPump2) {
                    if (bAnalogDamper) {
                        if (nInputRegistersData[analogDamperIndex + 8] == 0)
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_on);
                    } else if (bDigitalDamper) {
                        if (!bDiscretInputsData[digitalDamperIndex + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_on);
                    } else {
                        if (!bDiscretInputsData[fan1Index + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_filter_off_damper_on);
                    }

                    // water temperature
                    sTemp = Integer.toString(nInputRegistersData[waterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue1.setText(sTemp);
                    textInputValue1.setVisibility(View.VISIBLE);
                    textInputName1.setText(" t обр.воды");
                    textInputName1.setVisibility(View.VISIBLE);

                    // mix box temperature
                    if (bChannelMixTSensor) {
                        sTemp = Integer.toString(nInputRegistersData[channelMixTSensorIndex]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textInputValue2.setText(sTemp);
                        textInputValue2.setVisibility(View.VISIBLE);
                        textInputName2.setText("t кам.смеш");
                        textInputName2.setVisibility(View.VISIBLE);
                    }

                    if (bAfterRadiatorTSensor) {
                        sTemp = Integer.toString(nInputRegistersData[afterRadiatorTSensorIndex]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textInputValue2.setText(sTemp);
                        textInputValue2.setVisibility(View.VISIBLE);
                        textInputName2.setText("t после исп.");
                        textInputName2.setVisibility(View.VISIBLE);
                    }

                    // heater valve
                    sTemp = Integer.toString(nInputRegistersData[waterHeaterValveIndex + 8]);
                    sTemp += "%";
                    textOutputValue1.setText(sTemp);
                    textOutputValue1.setVisibility(View.VISIBLE);
                    textOutputName1.setText("  Калорифер");
                    textOutputName1.setVisibility(View.VISIBLE);

                    // fan1 speed
                    if (bFan1Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan1SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue2.setText(sTemp);
                        textOutputValue2.setVisibility(View.VISIBLE);
                        textOutputName2.setText("  Прит.вент.");
                        textOutputName2.setVisibility(View.VISIBLE);
                    }

                    // fan2 speed
                    if (bFan2Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue3.setText(sTemp);
                        textOutputValue3.setVisibility(View.VISIBLE);
                        textOutputName3.setText("  Выт.вент.");
                        textOutputName3.setVisibility(View.VISIBLE);
                    }

                    // analog damper
                    if (bAnalogDamper) {
                        sTemp = Integer.toString(nInputRegistersData[analogDamperIndex + 8]);
                        sTemp += "%";
                        textOutputValue4.setText(sTemp);
                        textOutputValue4.setVisibility(View.VISIBLE);
                        textOutputName4.setText("Наружн.засл.");
                        textOutputName4.setVisibility(View.VISIBLE);
                    }
                }

                // water heater and chiller system
                if (bWaterHeater && bChiller) {
                    if (bAnalogDamper) {
                        if (nInputRegistersData[analogDamperIndex + 8] == 0)
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else if (bDigitalDamper) {
                        if (!bDiscretInputsData[digitalDamperIndex + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else {
                        if (!bDiscretInputsData[fan1Index + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    }

                    // water temperature
                    sTemp = Integer.toString(nInputRegistersData[waterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue1.setText(sTemp);
                    textInputValue1.setVisibility(View.VISIBLE);
                    textInputName1.setText("t обр.воды");
                    textInputName1.setVisibility(View.VISIBLE);

                    // heater valve
                    sTemp = Integer.toString(nInputRegistersData[waterHeaterValveIndex + 8]);
                    sTemp += "%";
                    textOutputValue1.setText(sTemp);
                    textOutputValue1.setVisibility(View.VISIBLE);
                    textOutputName1.setText("Калорифер");
                    textOutputName1.setVisibility(View.VISIBLE);

                    // chiller
                    if (bChillerValve) {
                        sTemp = Integer.toString(nInputRegistersData[chillerValveIndex + 8]);
                        sTemp += "%";
                    } else {
                        if (bDiscretInputsData[chillerIndex + 8]) {
                            sTemp = "ВКЛ.";
                        } else {
                            sTemp = "ОТКЛ.";
                        }
                    }
                    textOutputValue2.setText(sTemp);
                    textOutputValue2.setVisibility(View.VISIBLE);
                    textOutputName2.setText("Охладитель");
                    textOutputName2.setVisibility(View.VISIBLE);

                    // fan1 speed
                    if (bFan1Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan1SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue3.setText(sTemp);
                        textOutputValue3.setVisibility(View.VISIBLE);
                        textOutputName3.setText("Прит.вент.");
                        textOutputName3.setVisibility(View.VISIBLE);
                    }

                    // fan2 speed
                    if (bFan2Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue4.setText(sTemp);
                        textOutputValue4.setVisibility(View.VISIBLE);
                        textOutputName4.setText("Выт.вент.");
                        textOutputName4.setVisibility(View.VISIBLE);
                    }
                }


                // electric heater system
                if (bElHeater && !bWaterHeater && !bChiller && !bHPump1 && !bHPump2) {
                    if (bAnalogDamper) {
                        if (nInputRegistersData[analogDamperIndex + 8] == 0)
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_on);
                    } else if (bDigitalDamper) {
                        if (!bDiscretInputsData[digitalDamperIndex + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_on);
                    } else {
                        if (!bDiscretInputsData[fan1Index + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_elheater_filter_off_damper_on);
                    }

                    // elheater temperature
                    sTemp = Integer.toString(nInputRegistersData[elheaterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue1.setText(sTemp);
                    textInputValue1.setVisibility(View.VISIBLE);
                    textInputName1.setText("t ТЭН");
                    textInputName1.setVisibility(View.VISIBLE);

                    // elheater power
                    sTemp = Integer.toString(nRam[10]);
                    sTemp += "%";
                    textInputValue2.setText(sTemp);
                    textInputValue2.setVisibility(View.VISIBLE);
                    textInputName2.setText("  ТЭН");
                    textInputName2.setVisibility(View.VISIBLE);


                    textInputName3.setVisibility(View.INVISIBLE);
                    textInputValue3.setVisibility(View.INVISIBLE);
                    textInputName4.setVisibility(View.INVISIBLE);
                    textInputValue4.setVisibility(View.INVISIBLE);


                    // fan1 speed
                    if (bFan1Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue1.setText(sTemp);
                        textOutputValue1.setVisibility(View.VISIBLE);
                        textOutputName1.setText("Прит.вент.");
                        textOutputName1.setVisibility(View.VISIBLE);
                    } else {
                        textOutputName1.setVisibility(View.INVISIBLE);
                        textOutputValue1.setVisibility(View.INVISIBLE);
                    }

                    // elheater
                    if (bElHeater || bElHeater1) {
                        if (bElHeater) {
                            if (bDiscretInputsData[elheaterIndex + 8]) {
                                sTemp = "ВКЛ.";
                            } else {
                                sTemp = "ОТКЛ.";
                            }
                        }

                        if (bElHeater1) {
                            if (bDiscretInputsData[elheater1Index + 8]) {
                                sTemp = "ВКЛ.";
                            } else {
                                sTemp = "ОТКЛ.";
                            }
                        }

                        textOutputName2.setText("     ТЭН");
                        textOutputName2.setVisibility(View.VISIBLE);
                        textOutputValue2.setText(sTemp);
                        textOutputValue2.setVisibility(View.VISIBLE);
                    }

                    // elheater2
                    if (bElHeater2) {
                        if (bDiscretInputsData[elheater2Index + 8]) {
                            sTemp = "ВКЛ.";
                        } else {
                            sTemp = "ОТКЛ.";
                        }
                        textOutputName3.setText("    ТЭН2");
                        textOutputName3.setVisibility(View.VISIBLE);
                        textOutputValue3.setText(sTemp);
                        textOutputValue3.setVisibility(View.VISIBLE);
                    }

                    // elheater3
                    if (bElHeater3) {
                        if (bDiscretInputsData[elheater3Index + 8]) {
                            sTemp = "ВКЛ.";
                        } else {
                            sTemp = "ОТКЛ.";
                        }
                        textOutputName4.setText("    ТЭН3");
                        textOutputName4.setVisibility(View.VISIBLE);
                        textOutputValue4.setText(sTemp);
                        textOutputValue4.setVisibility(View.VISIBLE);
                    }
                }


                // water heater and heater pump (planeta) system
                if (bWaterHeater && bHPump2) {
                    if (bAnalogDamper) {
                        if (nInputRegistersData[analogDamperIndex + 8] == 0)
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else if (bDigitalDamper) {
                        if (!bDiscretInputsData[digitalDamperIndex + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else {
                        if (!bDiscretInputsData[fan1Index + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    }

                    // water temperature
                    sTemp = Integer.toString(nInputRegistersData[waterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue1.setText(sTemp);
                    textInputValue1.setVisibility(View.VISIBLE);
                    textInputName1.setText("t обр.воды");
                    textInputName1.setVisibility(View.VISIBLE);

                    if (device_algorithm != 319) {
                        // hot freon temperature
                        sTemp = Integer.toString(nInputRegistersData[hotFreonTSensorIndex]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textInputValue2.setText(sTemp);
                        textInputValue2.setVisibility(View.VISIBLE);
                        textInputName2.setText("t гор.газов");
                        textInputName2.setVisibility(View.VISIBLE);

                        // cold freon temperature
                        sTemp = Integer.toString(nInputRegistersData[coldFreonTSensorIndex]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textInputValue3.setText(sTemp);
                        textInputValue3.setVisibility(View.VISIBLE);
                        textInputName3.setText("t хол.газов");
                        textInputName3.setVisibility(View.VISIBLE);
                    } else {
                        // humidity
                        sTemp = Integer.toString(nInputRegistersData[waterTSensorIndex+1]);
                        sTemp += "%";
                        textInputValue2.setText(sTemp);
                        textInputValue2.setVisibility(View.VISIBLE);
                        textInputName2.setText("Влажность");
                        textInputName2.setVisibility(View.VISIBLE);
                    }

                    if (device_algorithm == 319) {
                        // heater valve
                        sTemp = Integer.toString(nRam[10]);
                        sTemp += "%";
                        textOutputValue1.setText(sTemp);
                        textOutputValue1.setVisibility(View.VISIBLE);
                        textOutputName1.setText("Калорифер1");
                        textOutputName1.setVisibility(View.VISIBLE);
                    } else {
                        // heater valve
                        sTemp = Integer.toString(nInputRegistersData[waterHeaterValveIndex + 8]);
                        sTemp += "%";
                        textOutputValue1.setText(sTemp);
                        textOutputValue1.setVisibility(View.VISIBLE);
                        textOutputName1.setText("Калорифер");
                        textOutputName1.setVisibility(View.VISIBLE);
                    }

                    // heater pump
                    if (bCompSpeed) {
                        sTemp = Integer.toString(nInputRegistersData[compSpeedIndex + 8]);
                        sTemp += "%";
                    } else {
                        if (device_algorithm == 319) {
                            if (bDiscretInputsData[compIndex + 8] || bDiscretInputsData[compIndex + 12]) {
                                sTemp = "ВКЛ.";
                            } else {
                                sTemp = "ОТКЛ.";
                            }
                        } else {
                            if (bDiscretInputsData[compIndex + 8]) {
                                sTemp = "ВКЛ.";
                            } else {
                                sTemp = "ОТКЛ.";
                            }
                        }
                    }
                    textOutputValue2.setText(sTemp);
                    textOutputValue2.setVisibility(View.VISIBLE);
                    textOutputName2.setText("Компрессор");
                    textOutputName2.setVisibility(View.VISIBLE);

                    if (device_algorithm == 319) {

                        bFan1 = true;

                        // fan1 speed
                        if (bFan1Speed) {
                            sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                            sTemp += "%";
                            textOutputValue3.setText(sTemp);
                            textOutputValue3.setVisibility(View.VISIBLE);
                            textOutputName3.setText("Вентилятор");
                            textOutputName3.setVisibility(View.VISIBLE);

                            sTemp = Integer.toString(nRam[20]);
                            sTemp += "%";
                            textOutputValue4.setText(sTemp);
                            textOutputValue4.setVisibility(View.VISIBLE);
                            textOutputName4.setText("Калорифер2");
                            textOutputName4.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // fan1 speed
                        if (bFan1Speed) {
                            sTemp = Integer.toString(nInputRegistersData[fan1SpeedIndex + 8]);
                            sTemp += "%";
                            textOutputValue3.setText(sTemp);
                            textOutputValue3.setVisibility(View.VISIBLE);
                            textOutputName3.setText("Прит.вент.");
                            textOutputName3.setVisibility(View.VISIBLE);
                        }

                        // fan2 speed
                        if (bFan2Speed) {
                            sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                            sTemp += "%";
                            textOutputValue4.setText(sTemp);
                            textOutputValue4.setVisibility(View.VISIBLE);
                            textOutputName4.setText("Выт.вент.");
                            textOutputName4.setVisibility(View.VISIBLE);
                        }
                    }
                }

                // water heater and heater pump (planeta) system
                if (bElHeater && bHPump2) {
                    if (bAnalogDamper) {
                        if (nInputRegistersData[analogDamperIndex + 8] == 0)
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else if (bDigitalDamper) {
                        if (!bDiscretInputsData[digitalDamperIndex + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    } else {
                        if (!bDiscretInputsData[fan1Index + 8])
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_off);
                        else
                            imageSystem.setBackgroundResource(R.drawable.p_wheater_chiller_filter_off_damper_on);
                    }

                    // elheater temperature
                    sTemp = Integer.toString(nInputRegistersData[elheaterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue1.setText(sTemp);
                    textInputValue1.setVisibility(View.VISIBLE);
                    textInputName1.setText("           t ТЭН");
                    textInputName1.setVisibility(View.VISIBLE);


                    // hot freon temperature
                    sTemp = Integer.toString(nInputRegistersData[hotFreonTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue2.setText(sTemp);
                    textInputValue2.setVisibility(View.VISIBLE);
                    textInputName2.setText(" t гор.газов");
                    textInputName2.setVisibility(View.VISIBLE);

                    // cold freon temperature
                    sTemp = Integer.toString(nInputRegistersData[coldFreonTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textInputValue3.setText(sTemp);
                    textInputValue3.setVisibility(View.VISIBLE);
                    textInputName3.setText(" t хол.газов");
                    textInputName3.setVisibility(View.VISIBLE);



                    // elheater power
                    sTemp = Integer.toString(nRam[10]);
                    sTemp += "%";
                    textOutputValue1.setText(sTemp);
                    textOutputValue1.setVisibility(View.VISIBLE);
                    textOutputName1.setText("            ТЭН");
                    textOutputName1.setVisibility(View.VISIBLE);


                    // heater pump
                    if (bCompSpeed) {
                        sTemp = Integer.toString(nInputRegistersData[compSpeedIndex + 8]);
                        sTemp += "%";
                    } else {
                        if (bDiscretInputsData[compIndex + 8]) {
                            sTemp = "ВКЛ.";
                        } else {
                            sTemp = "ОТКЛ.";
                        }
                    }
                    textOutputValue2.setText(sTemp);
                    textOutputValue2.setVisibility(View.VISIBLE);
                    textOutputName2.setText(" Компрессор");
                    textOutputName2.setVisibility(View.VISIBLE);

                    if (bFan1Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan1SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue3.setText(sTemp);
                        textOutputValue3.setVisibility(View.VISIBLE);
                        textOutputName3.setText("  Прит.вент.");
                        textOutputName3.setVisibility(View.VISIBLE);
                    }

                    // fan2 speed
                    if (bFan2Speed) {
                        sTemp = Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]);
                        sTemp += "%";
                        textOutputValue4.setText(sTemp);
                        textOutputValue4.setVisibility(View.VISIBLE);
                        textOutputName4.setText("   Выт.вент.");
                        textOutputName4.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (bFan1) {
                // Fan speed number
                if (device_algorithm == 319) {
                    if (!bDiscretInputsData[digitalDamperIndex + 8]) {
                        sTemp = "[Откл]";
                        textFanSpeedNum.setText(sTemp);
                    } else {
                        if (!bFan1Speed) {
                            sTemp = "[Вкл]";
                            textFanSpeedNum.setText(sTemp);
                        } else {
                            sTemp = Integer.toString(nRam[13]) + "(";
                            sTemp += Integer.toString(nInputRegistersData[fan2SpeedIndex + 8]) + "%)";
                            textFanSpeedNum.setText(sTemp);
                        }
                    }
                } else {
                    if (!bDiscretInputsData[fan1Index + 8]) {
                        sTemp = "[Откл]";
                        textFanSpeedNum.setText(sTemp);
                    } else {
                        if (!bFan1Speed) {
                            sTemp = "[Вкл]";
                            textFanSpeedNum.setText(sTemp);
                        } else {
                            sTemp = Integer.toString(nRam[13]) + "(";
                            sTemp += Integer.toString(nInputRegistersData[fan1SpeedIndex + 8]) + "%)";
                            textFanSpeedNum.setText(sTemp);
                        }
                    }
                }
                textFanSpeedNum.setVisibility(View.VISIBLE);
            } else {
                textFanSpeedNum.setVisibility(View.INVISIBLE);
            }

            // WARM UP
            if (bWaterHeater) {
                if (nRam[9] == 1) {
                    imageWarmUp.setVisibility(View.VISIBLE);
                    textWarmUp.setVisibility(View.VISIBLE);
                } else {
                    imageWarmUp.setVisibility(View.INVISIBLE);
                    textWarmUp.setVisibility(View.INVISIBLE);
                }

                sTemp = "Прогрев";
                textWarmUp.setText(sTemp);

            } else {
                imageWarmUp.setVisibility(View.INVISIBLE);
                textWarmUp.setVisibility(View.INVISIBLE);
            }


            // SEASON
            if (nRam[0] == 1) {
                textSeason.setText("Лето");
                if (!largeScreen)
                    textSeason.setTextColor(Color.YELLOW);
            } else {
                textSeason.setText("Зима");
                if (!largeScreen)
                    textSeason.setTextColor(Color.CYAN);
            }

            // COND SEASON
            if (nRam[1] == 0) {
                imageSeason.setBackgroundResource(R.drawable.icons8sun25);
                if (!largeScreen)
                    textMainReg.setTextColor(Color.RED);
            } else {
                imageSeason.setBackgroundResource(R.drawable.icons8snow25);
                if (!largeScreen)
                    textMainReg.setTextColor(Color.BLUE);
            }

            // net status
            if (tempBadLinkCounter != badLinkCounter || newScreen) {
                tempBadLinkCounter = badLinkCounter;
                if (tempBadLinkCounter > 5) {
                    imageNetStatus.setBackgroundResource(R.drawable.icons8wifioff25);
                    paramReadSuccessFlag = false;
                } else {
                    imageNetStatus.setBackgroundResource(R.drawable.icons8wifion25);
                }
            }


            // SYSTEM STATE
            if (tempSystemState != nRam[2] || newScreen) {
                tempSystemState = nRam[2];
                if (nRam[2] == 0)
                    textSystemState.setText("Откл.");
                else
                    textSystemState.setText("Вкл.");
            }


            // REMOTE START BLOCK FLAG
            if (nRam[5] != nRemoteStartBlockFlag || newScreen) {
                if (nRam[5] == 0)
                    imageSystemStartBlock.setBackgroundResource(R.drawable.icons8switchon25);
                else
                    imageSystemStartBlock.setBackgroundResource(R.drawable.icons8switchoff25);

                nRemoteStartBlockFlag = nRam[5];
            }

            // OUTDOOR TEMPERATURE
            if (bOutdoorTSensor) {
                sTemp = Integer.toString(nInputRegistersData[outdoorTSensorIndex]);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textOutdoorTemp.setText(sTemp);

                textOutdoorTemp.setVisibility(View.VISIBLE);
                imageOutdoor.setVisibility(View.VISIBLE);

                if (!largeScreen)
                    textSeason.setVisibility(View.VISIBLE);
                else
                    textOutdoorString.setVisibility(View.VISIBLE);

            } else {
                textOutdoorTemp.setVisibility(View.INVISIBLE);
                imageOutdoor.setVisibility(View.INVISIBLE);

                if (!largeScreen)
                    textSeason.setVisibility(View.INVISIBLE);
                else
                    textOutdoorString.setVisibility(View.INVISIBLE);
            }

            // room t sensor
            if (bRoomTSensor && bChannelTSensor) {
                sTemp = Integer.toString(nInputRegistersData[roomTSensorIndex]);
                sTemp += String.valueOf((char) 176);
                sTemp += "C";
                textRoomTemp.setText(sTemp);

                textRoomTemp.setVisibility(View.VISIBLE);
                imageRoom.setVisibility(View.VISIBLE);
            } else {
                textRoomTemp.setVisibility(View.INVISIBLE);
                imageRoom.setVisibility(View.INVISIBLE);
            }

            // MAIN TEMPERATURE
            if (largeScreen) {

                if (bChannelTSensor) {
                    if (tempChannelValue != nInputRegistersData[1] || newScreen) {
                        tempChannelValue = nInputRegistersData[1];
                        bReDraw = true;
                    }

                    sTemp = Integer.toString(tempChannelValue);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);
                    bReDraw = false;

                    textTempVal.setVisibility(View.VISIBLE);
                    textChannelString.setVisibility(View.VISIBLE);

                } else {
                    textTempVal.setVisibility(View.INVISIBLE);
                    textChannelString.setVisibility(View.INVISIBLE);
                }

            } else {

                if (device_algorithm == 889) {
                    TextView textTempVal2 = (TextView)findViewById(R.id.textViewTemp2);
                    sTemp = Integer.toString(nInputRegistersData[1]);
                    sTemp += String.valueOf((char)176);
                    sTemp += "C";
                    textTempVal2.setText(sTemp);
                    textTempVal2.setTextColor(Color.YELLOW);
                    textTempVal2.setVisibility(View.VISIBLE);

                    textTempVal = (TextView)findViewById(R.id.textViewTemp);
                    sTemp = Integer.toString(nInputRegistersData[2]);
                    sTemp += String.valueOf((char)176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);
                    textTempVal.setTextColor(Color.YELLOW);

                    TextView textTempVal3 = (TextView)findViewById(R.id.textViewTemp3);
                    sTemp = Integer.toString(nInputRegistersData[3]);
                    sTemp += String.valueOf((char)176);
                    sTemp += "C";
                    textTempVal3.setText(sTemp);
                    textTempVal3.setTextColor(Color.YELLOW);
                    textTempVal3.setVisibility(View.VISIBLE);

                } else if (bHotWaterTSensor) {
                    sTemp = Integer.toString(nInputRegistersData[hotWaterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);

                    textTempVal.setTextColor(Color.YELLOW);
                    textWater.setVisibility(View.INVISIBLE);

                } else if (bWaterTSensor && nRam[2] == 0 && nRam[0] == 0) {
                    sTemp = Integer.toString(nInputRegistersData[waterTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);

                    textTempVal.setTextColor(Color.BLUE);
                    textWater.setVisibility(View.VISIBLE);

                } else if (bChannelTSensor) {
                    sTemp = Integer.toString(nInputRegistersData[channelTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);

                    textTempVal.setTextColor(Color.YELLOW);
                    textWater.setVisibility(View.INVISIBLE);

                } else if (bRoomTSensor) {
                    sTemp = Integer.toString(nInputRegistersData[roomTSensorIndex]);
                    sTemp += String.valueOf((char) 176);
                    sTemp += "C";
                    textTempVal.setText(sTemp);

                    textTempVal.setTextColor(Color.YELLOW);
                    textWater.setVisibility(View.INVISIBLE);
                }


                /*if (device_algorithm == 108 || device_algorithm == 304 || device_algorithm == 306 || device_algorithm == 109 || device_algorithm == 309) {
                    if (tempChannelValue != nInputRegistersData[1] || newScreen) {
                        sTemp = Integer.toString(nInputRegistersData[1]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textTempVal.setText(sTemp);
                        tempChannelValue = nInputRegistersData[1];
                    }
                    textTempVal.setTextColor(Color.YELLOW);
                    textWater.setVisibility(View.INVISIBLE);
                } else if (device_algorithm != 201) {
                    if (nRam[0] == 0) {
                        if (nRam[2] != 0) {
                            if (tempChannelValue != nInputRegistersData[1] || newScreen) {
                                sTemp = Integer.toString(nInputRegistersData[1]);
                                sTemp += String.valueOf((char) 176);
                                sTemp += "C";
                                textTempVal.setText(sTemp);
                                tempChannelValue = nInputRegistersData[1];
                            }
                            textTempVal.setTextColor(Color.YELLOW);
                            textWater.setVisibility(View.INVISIBLE);
                        } else {
                            if (device_algorithm == 30103) {
                                if (tempWaterValue != nInputRegistersData[5] || newScreen) {
                                    sTemp = Integer.toString(nInputRegistersData[5]);
                                    sTemp += String.valueOf((char) 176);
                                    sTemp += "C";
                                    textTempVal.setText(sTemp);
                                    tempWaterValue = nInputRegistersData[2];
                                }
                            } else {
                                if (tempWaterValue != nInputRegistersData[2] || newScreen) {
                                    sTemp = Integer.toString(nInputRegistersData[2]);
                                    sTemp += String.valueOf((char) 176);
                                    sTemp += "C";
                                    textTempVal.setText(sTemp);
                                    tempWaterValue = nInputRegistersData[2];
                                }
                            }
                            textTempVal.setTextColor(Color.BLUE);
                            textWater.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (tempChannelValue != nInputRegistersData[1] || newScreen) {
                            sTemp = Integer.toString(nInputRegistersData[1]);
                            sTemp += String.valueOf((char) 176);
                            sTemp += "C";
                            textTempVal.setText(sTemp);
                            tempChannelValue = nInputRegistersData[1];
                        }
                        textTempVal.setTextColor(Color.YELLOW);
                        textWater.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (tempWaterValue != nInputRegistersData[2] || newScreen) {
                        sTemp = Integer.toString(nInputRegistersData[2]);
                        sTemp += String.valueOf((char) 176);
                        sTemp += "C";
                        textTempVal.setText(sTemp);
                        tempWaterValue = nInputRegistersData[2];
                    }
                    textTempVal.setTextColor(Color.BLUE);
                    textWater.setVisibility(View.VISIBLE);
                }*/
            }

            if (!largeScreen) {
                // HEATING VALVE (CHILLER VALVE)

                if (bWaterHeater) {
                    sTemp = Integer.toString(nInputRegistersData[waterHeaterValveIndex + 8]);
                    sTemp += "%";
                    textMainReg.setText(sTemp);
                    textMainReg.setTextColor(Color.RED);
                    textMainReg.setVisibility(View.VISIBLE);
                } else if (bElHeater) {
                    sTemp = Integer.toString(nRam[10]);
                    sTemp += "%";
                    textMainReg.setText(sTemp);
                    textMainReg.setTextColor(Color.RED);
                    textMainReg.setVisibility(View.VISIBLE);
                } else {
                    textMainReg.setVisibility(View.INVISIBLE);
                }
                /*if (device_algorithm == 304) {
                    if (bDiscretInputsData[8]) {
                        sTemp = "Вкл";
                    } else {
                        sTemp = "Откл";
                    }
                    textMainReg.setText(sTemp);
                    textMainReg.setTextColor(Color.BLUE);
                } else if (device_algorithm == 201) {
                    if (nInputRegistersData[8] != tempHeaterValve || newScreen) {
                        tempHeaterValve = nInputRegistersData[8];
                        bReDraw = true;
                    }
                } else {
                    if (tempCondSeason == 0) {
                        if (device_algorithm != 30103 && device_algorithm != 305 && device_algorithm != 306 && device_algorithm != 307) {
                            if (nInputRegistersData[8] != tempHeaterValve || newScreen) {
                                tempHeaterValve = nInputRegistersData[8];
                                bReDraw = true;
                            }
                        } else {
                            if (tempSeason == 1) {
                                if (device_algorithm == 305 || device_algorithm == 306 || device_algorithm == 307 || device_algorithm == 309) {
                                    if (device_algorithm != 309) {
                                        if (bDiscretInputsData[15]) {
                                            sTemp = "Вкл";
                                        } else {
                                            sTemp = "Откл";
                                        }
                                    } else {
                                        if (bDiscretInputsData[8]) {
                                            sTemp = "Вкл";
                                        } else {
                                            sTemp = "Откл";
                                        }
                                    }

                                    textMainReg.setText(sTemp);
                                    textMainReg.setTextColor(Color.RED);
                                }

                                if (device_algorithm == 30103) {
                                    if (nInputRegistersData[8] != tempHeaterValve || newScreen) {
                                        tempHeaterValve = nInputRegistersData[8];
                                        bReDraw = true;
                                    }
                                }
                            } else {

                                if (device_algorithm == 307) {
                                    if (bDiscretInputsData[15]) {
                                        sTemp = "Вкл";
                                    } else {
                                        sTemp = "Откл";
                                    }
                                    textMainReg.setText(sTemp);
                                    textMainReg.setTextColor(Color.RED);

                                } else if (nInputRegistersData[11] != tempHeaterValve || newScreen) {
                                    tempHeaterValve = nInputRegistersData[11];
                                    bReDraw = true;
                                }
                            }
                        }
                    } else {
                        if (device_algorithm == 30101 || device_algorithm == 30102 || device_algorithm == 30103) {
                            if (nInputRegistersData[8] != tempHeaterValve || newScreen) {
                                tempHeaterValve = nInputRegistersData[8];
                                bReDraw = true;
                            }
                        } else if (device_algorithm == 305 || device_algorithm == 306 || device_algorithm == 307 || device_algorithm == 309) {
                            if (device_algorithm != 309) {
                                if (bDiscretInputsData[15]) {
                                    sTemp = "Вкл";
                                } else {
                                    sTemp = "Откл";
                                }
                            } else {
                                if (bDiscretInputsData[8]) {
                                    sTemp = "Вкл";
                                } else {
                                    sTemp = "Откл";
                                }
                            }
                            textMainReg.setText(sTemp);
                            textMainReg.setTextColor(Color.BLUE);
                        } else {
                            if (device_algorithm == 102 || (device_algorithm >= 10201 && device_algorithm <= 10299)) {
                                if (tempChillerFlag == 1) {
                                    if (bDiscretInputsData[15]) {
                                        sTemp = "Вкл";
                                    } else {
                                        sTemp = "Откл";
                                    }
                                    textMainReg.setText(sTemp);
                                    textMainReg.setTextColor(Color.BLUE);
                                } else {
                                    if (nInputRegistersData[11] != tempHeaterValve || newScreen) {
                                        tempHeaterValve = nInputRegistersData[11];
                                        bReDraw = true;
                                    }
                                    textMainReg.setTextColor(Color.RED);
                                }
                            } else {
                                if (nInputRegistersData[11] != tempHeaterValve || newScreen) {
                                    tempHeaterValve = nInputRegistersData[11];
                                    bReDraw = true;
                                }
                            }
                        }
                    }
                }

                if (bReDraw) {
                    sTemp = Integer.toString(tempHeaterValve);
                    sTemp += "%";
                    textMainReg.setText(sTemp);
                    bReDraw = false;
                }*/
            }

            // telemetry
            if (largeScreen) {

            }
            newScreen = false;
        }
    };

    @Override
    public void applyTexts(String username, String password) {
        if (username.equals(login[0]) && password.equals(login[1])) {
            nSecurityLevel = 5;
            Toast.makeText(this, "Уровень доступа 5", Toast.LENGTH_SHORT).show();
        }
        else

        if (username.equals(login[2]) && password.equals(login[3])) {
            nSecurityLevel = 4;
            Toast.makeText(this, "Уровень доступа 4", Toast.LENGTH_SHORT).show();
        }
        else

        if (username.equals(login[4]) && password.equals(login[5])) {
            nSecurityLevel = 4;
            Toast.makeText(this, "Уровень доступа 4", Toast.LENGTH_SHORT).show();
        }
        else

        if (username.equals(login[6]) && password.equals(login[7])) {
            nSecurityLevel = 1;
            Toast.makeText(this, "Уровень доступа 1", Toast.LENGTH_SHORT).show();
        }
        else

        if (username.equals(login[8]) && password.equals(login[9])) {
            nSecurityLevel = 2;
            Toast.makeText(this, "Уровень доступа 2", Toast.LENGTH_SHORT).show();
        }
        else

        if (username.equals(login[10]) && password.equals(login[11])) {
            nSecurityLevel = 3;
            Toast.makeText(this, "Уровень доступа 3", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
        }

        dialogOnFlag = false;
    }

    @Override
    public void applyNewFanSpeedNum(int newFanSpeedNum) {
        if (newFanSpeedNum != 0 /*&& appMode != 2*/) {
            ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                    device_address, 3085, newFanSpeedNum);
        }

        dialogOnFlag = false;
        dialogId = 0;
    }

    @Override
    public void applyNewValue(int newValue) {
        int offset;

        /*if (appMode == 2) {
            dialogOnFlag = false;
            return;
        }*/

        if (nRam[0] == 0 || device_algorithm == 30101 || device_algorithm == 30102) {
            offset = 2304;
        } else {
            offset = 2305;
        }

        if (newValue != 65535) {

            if (newValue < 5 || newValue > 40) {
                Toast.makeText(this, "Некорректное значение", Toast.LENGTH_SHORT).show();
            } else {
                ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                        device_address, offset, newValue);
            }
        }

        dialogOnFlag = false;
        dialogId = 0;
    }

    @Override
    public void applyNewExtraParam(boolean chiller_flag, boolean fan2_flag, boolean cancel_flag) {

        short c = 0;
        short f = 0;

        if (!cancel_flag) {

            if (chiller_flag) c = 1;
            if (fan2_flag) f = 1;

            ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                    device_address, 3079, c);

            ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                    device_address, 3094, f);
        }

        dialogOnFlag = false;
    }

    @Override
    public void applyAlgParamTexts(Boolean ignitionFlag, Boolean chillerDisabledFlag, Boolean cancelFlag) {

        if (!cancelFlag) {
            short c1 = 0;
            short c2 = 0;

            Toast.makeText(getApplicationContext(), "Параметры изменены", Toast.LENGTH_SHORT).show();

            if (!ignitionFlag) {
                c1 = 1;
            }

            if (!chillerDisabledFlag) {
                c2 = 1;
            }

            ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                    device_address, 3094, c1);

            ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                    device_address, 3079, c2);
        }

        dialogOnFlag = false;
    }
}

