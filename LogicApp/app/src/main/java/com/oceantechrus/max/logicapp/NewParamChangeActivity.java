package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.util.Timer;
import java.util.TimerTask;

public class NewParamChangeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewCurVal;
    private TextView textViewName;
    private TextView textViewWait;
    private Button buttonOK;
    private Button buttonCancel;
    private NumberPicker numberPicker;

    static private int[] nRam = new int[32];

    private int val;
    private int device_address;
    private int device_algorithm;
    private int par_addr;
    private int pos;
    private int index;

    private String paramName;
    private String paramValue;
    private String paramUnits;

    private boolean recordFlag;
    private boolean recordOK;
    private boolean recordFailed;

    private ProgressBar wait_bar;
    private Timer myTimer1;

    private Intent answerIntent;

    private boolean netInitSuccessFlag;

    private OnRequestBack<String> onRequestBackWriteRegister;
    private OnRequestBack<short[]> onRequestBackReadHoldingRegisters;

    public NewParamChangeActivity() {
        onRequestBackWriteRegister = null;
        onRequestBackReadHoldingRegisters = null;

        val = 1;
        par_addr = 0;
        pos = 0;
        index = 0;
        recordFlag = false;
        recordOK = false;
        recordFailed = false;
        netInitSuccessFlag = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_set_param_dialog);

        final Intent intent = getIntent();
        paramName = intent.getStringExtra("par_name");
        paramValue = intent.getStringExtra("par_val");
        paramUnits = intent.getStringExtra("par_units");
        device_address = intent.getIntExtra("device_address", 2);
        pos = intent.getIntExtra("par_position", 0);
        device_algorithm = intent.getIntExtra("device_algorithm", 101);

        numberPicker = (NumberPicker) findViewById(R.id.mainParamPicker);
        textViewName = (TextView) findViewById(R.id.textViewNewSetParamName);
        textViewCurVal = (TextView) findViewById(R.id.textViewCurNewSetParamValue);

        textViewWait = (TextView) findViewById(R.id.textViewSetParamWait);
        textViewWait.setVisibility(View.INVISIBLE);

        buttonOK = (Button) findViewById(R.id.buttonNewParOk);
        buttonOK.setOnClickListener(this);

        buttonCancel = (Button) findViewById(R.id.buttonNewParCancel);
        buttonCancel.setOnClickListener(this);

        wait_bar = (ProgressBar) findViewById(R.id.progressBarWaitParam);
        wait_bar.setVisibility(View.INVISIBLE);

        numberPicker.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (paramName.contains("Влажность")) {
            numberPicker.setMaxValue(70);
            numberPicker.setMinValue(20);
            par_addr = 2310;
        } else if (paramName.matches("Скорость")) {
            numberPicker.setMaxValue(7);
            numberPicker.setMinValue(1);
            par_addr = 3085;
        } else if (device_algorithm == 889) {
            numberPicker.setMaxValue(30);
            numberPicker.setMinValue(10);
            par_addr = 2310 + pos - 1;
        } else {
            numberPicker.setMaxValue(50);
            numberPicker.setMinValue(10);
            par_addr = 2304 + pos - 1;
        }

        textViewName.setText(paramName);

        numberPicker.setValue(Integer.parseInt(paramValue));
        textViewCurVal.setText("Текущее значение:   " + paramValue + paramUnits);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textViewCurVal.setText("Текущее значение:   " + Integer.toString(newVal) + paramUnits);
                val = newVal;
            }
        });

        // WriteRegister
        onRequestBackWriteRegister = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                //Toast.makeText(getApplicationContext(), "Запись прошла успешно", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String s) {
                //Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
            }
        };

        // ReadHoldingRegisters
        onRequestBackReadHoldingRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                for (int i = 0; i < nRam.length; i++)
                    nRam[i] = data[i];

                recordOK = true;
                recordFlag = false;
            }

            @Override
            public void onFailed(String s) {
                netInitSuccessFlag = false;
                recordFailed = true;
                recordFlag = false;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonNewParOk:
                if (!recordFlag) {
                    ModbusReq.getInstance().writeRegister(onRequestBackWriteRegister,
                            device_address, par_addr, val);

                    wait_bar.setVisibility(View.VISIBLE);
                    recordFlag = true;

                    buttonOK.setVisibility(View.INVISIBLE);
                    buttonCancel.setVisibility(View.INVISIBLE);
                    textViewName.setVisibility(View.INVISIBLE);
                    textViewCurVal.setVisibility(View.INVISIBLE);
                    numberPicker.setVisibility(View.INVISIBLE);

                    textViewWait.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.buttonNewParCancel:
                answerIntent = new Intent();
                setResult(RESULT_CANCELED, answerIntent);
                finish();
                break;
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
        }

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 500, 4000);
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

            if (recordFlag) {
                if (paramName.matches("Скорость")) {
                    ModbusReq.getInstance().readHoldingRegisters(onRequestBackReadHoldingRegisters,
                            device_address, 3072, nRam.length);
                } else {
                    ModbusReq.getInstance().readHoldingRegisters(onRequestBackReadHoldingRegisters,
                            device_address, 2304, nRam.length);
                }
            } else {

                if (recordFailed) {
                    recordFailed = false;
                    Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();

                    answerIntent = new Intent();
                    setResult(RESULT_CANCELED, answerIntent);
                    finish();
                    return;
                }

                if (recordOK) {
                    recordOK = false;
                    if (paramName.matches("Скорость")) {
                        index = 13;
                        if (nRam[index] == val) {
                            Toast.makeText(getApplicationContext(), "Запись прошла успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
                        }

                        answerIntent = new Intent();
                        answerIntent.putExtra("param_back_val", val);
                        answerIntent.putExtra("param_back_index", 999);
                        setResult(RESULT_OK, answerIntent);
                        finish();
                        return;

                    } else if (device_algorithm == 889) {
                        index = 6 + pos - 1;
                        if (nRam[index] == val) {
                            Toast.makeText(getApplicationContext(), "Запись прошла успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
                        }
                    } else if (paramName.contains("Влажность")) {
                        index = 6;
                        if (nRam[index] == val) {
                            Toast.makeText(getApplicationContext(), "Запись прошла успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        index = pos - 1;
                        if (nRam[index] == val) {
                            Toast.makeText(getApplicationContext(), "Запись прошла успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
                        }
                    }

                    answerIntent = new Intent();
                    answerIntent.putExtra("param_back_val", val);
                    answerIntent.putExtra("param_back_index", index);
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            }
        }
    };
}
