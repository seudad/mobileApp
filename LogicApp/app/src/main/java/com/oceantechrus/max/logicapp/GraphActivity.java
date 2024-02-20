package com.oceantechrus.max.logicapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class GraphActivity extends AppCompatActivity {
    private LineGraphSeries<DataPoint> seriesChannel1;
    private LineGraphSeries<DataPoint> seriesChannel2;
    private LineGraphSeries<DataPoint> seriesChannel3;
    private GraphView graph;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    private int device_address;

    static private int badLinkCounter;
    static private int tempBadLinkCounter;

    static private int step;

    private Calendar calendar;
    private int minutes;
    private int hours;

    private int startXPoint;
    private int graphNumber;

    // массив текущих значений Ui, Yi
    // Ui
    // 0 - наружная температура
    // 1 - канальная температура
    // 2 - температура обратной воды
    // 3 - температура в помещении
    // 4 - давление в помещении
    // 5 - датчик двери 1
    // 6 - датчик двери 2
    // 7 - датчик двери 3
    // Yi
    // 8 - клапан на теплоносителе
    // 9 - ЧП вентилятора 1
    // 10 - ЧП вентилятора 2
    // 11 -
    private int[] nInputRegistersData = new int[96];

    private Timer myTimer1;

    private OnRequestBack<short[]> onRequestBackReadInputRegisters;

    public GraphActivity() {
        onRequestBackReadInputRegisters = null;

        myTimer1 = null;
        startXPoint = 0;
        graphNumber = 1;

        for (int i = 0; i < nInputRegistersData.length; i++)
            nInputRegistersData[i] = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        Intent intent = getIntent();
        device_address = intent.getIntExtra("device_address", 2);

        // ReadInputRegisters
        onRequestBackReadInputRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                for (int i = 0; i < nInputRegistersData.length; i++)
                    nInputRegistersData[i] = data[i];

                badLinkCounter = 0;

                if (step < graphNumber) {
                    step++;
                }
            }

            @Override
            public void onFailed(String msg) {
                //Toast.makeText(getApplicationContext(), "MODBUS function 4 failed " + msg, Toast.LENGTH_SHORT).show();
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

        }, 500, 3000);

        graph = (GraphView) findViewById(R.id.graphViewGraph);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                String s = "";
                int hh;
                int mm;
                if (isValueX) {

                    value = value + startXPoint;
                    if (value >= 96) value -= 96;

                    hh = (int) (value/4);
                    mm = (int) (value%4);
                    mm *= 15;
                    if (mm != 0)
                        s = Integer.toString(hh) + ":" + Integer.toString(mm);
                    else
                        s = Integer.toString(hh) + ":00";
                    return s;
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getGridLabelRenderer().setNumVerticalLabels(7);
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(100);
        viewport.setMinY(0);
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxX(96);
        viewport.setMinX(0);
        viewport.setScrollable(false);
        viewport.setScalable(true);

        ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                device_address, 0x1800, nInputRegistersData.length);
    }

    public DataPoint[] data() {
        DataPoint[] values = new DataPoint[96];
        int j = 0;

        // Check time
        calendar = Calendar.getInstance();

        minutes = calendar.get(Calendar.MINUTE);
        hours = calendar.get(Calendar.HOUR_OF_DAY);

        minutes = minutes + hours*60;
        minutes /= 15;

        startXPoint = minutes;

        for (int i = minutes; i < 96; i++) {
            if (nInputRegistersData[i] < 0) nInputRegistersData[i] = -nInputRegistersData[i];
            DataPoint v = new DataPoint(j, nInputRegistersData[i]);
            values[j++] = v;
        }

        if (j < 96) {
            for (int i = 0; i < minutes; i++) {
                if (nInputRegistersData[i] < 0) nInputRegistersData[i] = -nInputRegistersData[i];
                DataPoint v = new DataPoint(j, nInputRegistersData[i]);
                values[j++] = v;
            }
        }

        return values;
    }

    public void DataSorting() {
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }

        for (int i = 0; i < nInputRegistersData.length; i++)
            nInputRegistersData[i] = 0;

        step = 0;
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

        }, 500, 3000);
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

            if (tempBadLinkCounter < 5 && step == 1) {
                seriesChannel1 = new LineGraphSeries<DataPoint>(data());
                seriesChannel1.setColor(Color.BLUE);
                seriesChannel1.setTitle("t1");
                graph.addSeries(seriesChannel1);

                //ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                //        device_address, 0x1a00, nInputRegistersData.length);
                step++;
            }

            /*if (tempBadLinkCounter < 5 && step == 2) {
                seriesChannel2 = new LineGraphSeries<DataPoint>(data());
                seriesChannel2.setColor(Color.GREEN);
                seriesChannel2.setTitle("t2");
                graph.addSeries(seriesChannel2);

                ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                        device_address, 0x1b00, nInputRegistersData.length);
            }

            if (tempBadLinkCounter < 5 && step == 3) {
                seriesChannel3 = new LineGraphSeries<DataPoint>(data());
                seriesChannel3.setColor(Color.RED);
                seriesChannel3.setTitle("t3");
                graph.addSeries(seriesChannel3);
                step++;
            }*/
        }
    };
}
