package com.oceantechrus.max.logicapp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {//implements View.OnClickListener,
        //ApplicationModeDialog.ApplicationModeDialogListener,
        //NetParamDialog.NetParamDialogListener {

    static int timeout = 3000;//3000;
    static int delay = 50;
    static int period = 5000;//5000;


    private OnRequestBack<short[]> onRequestBackReadInputRegisters;
    private OnRequestBack<String> onRequestBackWriteRegisters;

    private OnRequestBack<String> onRequestBackModbusInit;

    static final private int dev_array_size = 21;


    static private boolean[][] bAlarmFlags = new boolean[dev_array_size][32];

    static private int[] nInputRegistersData = new int[39];
    static private byte[] symbolArray = new byte[dev_array_size];

    static private Device[] dev_array = new Device[dev_array_size];

    static private boolean[] netInitSuccessFlag = new boolean[255];

    static private int appMode;
    static private int curNetDevIndex;
    static private boolean singleDeviceMode;

    static private String sGatewayIP;
    static private String sStartIP;
    static private String sPortNumber;
    static private String sDeviceGroupSize;
    static private Boolean bRemoteAccess;
    static private boolean largeScreen;

    private ProgressBar wait_bar;


    private Timer myTimer1;

    private ListView lvDevice;

    private DeviceListAdaptor adapter;
    private List<Device> mDeviceList;

    private SharedPreferences sPref;

    ArrayList<InetAddress> inetAddresses;
    ArrayList<String> canonicalHostNames;
    byte[] gatewayIP = new byte[4];
    byte[] localGatewayIP = new byte[4];

    static private boolean newIntent;
    static private boolean skipInitCheck;

    WifiManager wifiManager;
    BroadcastReceiver wifiScanReceiver;
    IntentFilter intentFilter;


    static int size;
    private int wifiScanFailureCounter;

    // constructor
    public MainActivity() {
        myTimer1 = null;

        onRequestBackReadInputRegisters = null;
        onRequestBackWriteRegisters = null;
        onRequestBackModbusInit = null;

        curNetDevIndex = -1;
        wifiScanFailureCounter = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        appMode = sPref.getInt("APP_MODE", 1);
        singleDeviceMode = sPref.getBoolean("SINGLE_DEVICE_MODE", false);
        sGatewayIP = sPref.getString("GATEWAY_IP", "192.168.1.1");
        sPortNumber = sPref.getString("PORT_NUMBER", "8899");
        sDeviceGroupSize = sPref.getString("DEV_NUMBER", "1");
        bRemoteAccess = sPref.getBoolean("REMOTE_ACCESS_KEY", false);

        if (appMode == 2) {
            timeout = 500;
            period = 1000;
        }



        size = Integer.valueOf(sDeviceGroupSize);

        wait_bar = (ProgressBar) findViewById(R.id.progressBarWaitDeviceList);
        //wait_bar.setVisibility(View.INVISIBLE);

        curNetDevIndex = -1;


        // Get WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        /*if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);*/

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


        // fetch the gateway IP address
        String sByte1;
        String sByte2;
        String sByte3;
        String sByte4;
        int indexBegin, indexEnd;
        indexBegin = 0;
        indexEnd = 0;

        indexEnd = sGatewayIP.indexOf('.', indexBegin);
        sByte1 = sGatewayIP.substring(indexBegin, indexEnd);
        gatewayIP[0] = (byte)Integer.parseInt(sByte1);
        indexBegin = indexEnd + 1;

        indexEnd = sGatewayIP.indexOf('.', indexBegin);
        sByte2 = sGatewayIP.substring(indexBegin, indexEnd);
        gatewayIP[1] = (byte)Integer.parseInt(sByte2);
        indexBegin = indexEnd + 1;

        indexEnd = sGatewayIP.indexOf('.', indexBegin);
        sByte3 = sGatewayIP.substring(indexBegin, indexEnd);
        gatewayIP[2] = (byte)Integer.parseInt(sByte3);
        indexBegin = indexEnd + 1;

        sByte4 = sGatewayIP.substring(indexBegin);
        gatewayIP[3] = (byte)Integer.parseInt(sByte4);


        // ReadInputRegisters
        onRequestBackReadInputRegisters = new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                for (int i = 0; i < nInputRegistersData.length; i++)
                    nInputRegistersData[i] = data[i];


                for (int i = 12; i < 32; i++) {
                    symbolArray[i-12] = (byte) nInputRegistersData[i];
                }

                String str = EncodingUtils.getString(symbolArray, "windows-1251");

                int index = -1;
                for (int i = 0; i < dev_array_size; i++) {
                    if (appMode == 3 && !bRemoteAccess) {
                        if (dev_array[i] != null && dev_array[i].getIp().compareTo(inetAddresses.get(curNetDevIndex).getHostAddress()) == 0) {
                            index = i;
                            break;
                        }
                    } else {
                        if (dev_array[i] != null && dev_array[i].getAddress() == nInputRegistersData[0]) {
                            index = i;
                            break;
                        }
                    }
                }


                if (index == -1) {
                    for (int i = 0; i < dev_array_size; i++) {
                        if (dev_array[i] == null) {
                            dev_array[i] = new Device("", "", "", "", "","", "",
                                    0, 0, 0, 0, 0, 0);
                            index = i;
                            break;
                        }

                        if (dev_array[i].getName().isEmpty()) {
                            index = i;
                            break;
                        }
                    }
                }



                if (index < 0) return;

                dev_array[index].setName(str);
                dev_array[index].setId(Integer.toHexString(nInputRegistersData[6]) + Integer.toHexString(nInputRegistersData[5]) +
                        Integer.toHexString(nInputRegistersData[4]) + Integer.toHexString(nInputRegistersData[3]) +
                        Integer.toHexString(nInputRegistersData[2]) + Integer.toHexString(nInputRegistersData[1]));

                if (nInputRegistersData[1] == 0xffffffff && nInputRegistersData[2] == 0xffffffff)
                    dev_array[index].setId("неизвестен");

                if (appMode == 3 && !bRemoteAccess) {
                    dev_array[index].setIp(inetAddresses.get(curNetDevIndex).getHostAddress());
                }

                dev_array[index].setAddress(nInputRegistersData[0]);
                dev_array[index].setSystem_status(nInputRegistersData[7]);
                dev_array[index].setAlgorithm_number(nInputRegistersData[9]);
                dev_array[index].setPassword1(Integer.toString(nInputRegistersData[36]));
                dev_array[index].setPassword2(Integer.toString(nInputRegistersData[37]));
                dev_array[index].setPassword3(Integer.toString(nInputRegistersData[38]));
                dev_array[index].setMinutes(nInputRegistersData[32]);
                dev_array[index].setHours(nInputRegistersData[33]);

                int alarm_flag = 0;


                for (int i = 0; i < 16; i++) {
                    bAlarmFlags[index][i] = ((nInputRegistersData[34]&(1<<i))!= 0) ? true : false;
                    if (bAlarmFlags[index][i]) alarm_flag = 1;
                }
                for (int i = 0; i < 16; i++) {
                    bAlarmFlags[index][i+16] = ((nInputRegistersData[35]&(1<<i))!= 0) ? true : false;
                    if (bAlarmFlags[index][i+16]) alarm_flag = 1;
                }

                dev_array[index].setAlarm_status(alarm_flag);

                netInitSuccessFlag[curNetDevIndex] = false;

                /*if (appMode == 1 || size == 1) {
                    String name = dev_array[index].getName();
                    String ipString = dev_array[index].getIp();
                    if (bRemoteAccess) ipString = sGatewayIP;
                    int alg = dev_array[index].getAlgorithm_number();
                    int address = dev_array[index].getAddress();

                    Intent intent;

                    //if (largeScreen) {
                    //    intent = new Intent(getApplicationContext(), MainVentActivity.class);
                    //} else {
                        intent = new Intent(getApplicationContext(), NewMainActivity.class);
                    //}
                    intent.putExtra("device_address", address);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("system_name", name);
                    intent.putExtra("device_ip", ipString);
                    intent.putExtra("device_algorithm", alg);
                    intent.putExtra("port_number", sPortNumber);
                    startActivity(intent);

                    newIntent = true;
                }*/
            }

            @Override
            public void onFailed(String msg) {
                if (curNetDevIndex >= 0) {
                    netInitSuccessFlag[curNetDevIndex] = false;
                }
            }
        };

        // WriteRegister
        onRequestBackWriteRegisters = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailed(String s) {

            }
        };

        // Modbus init
        onRequestBackModbusInit = new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                if (curNetDevIndex >= 0) {
                    netInitSuccessFlag[curNetDevIndex] = true;
                }

                skipInitCheck = false;
            }

            @Override
            public void onFailed(String msg) {
                if (curNetDevIndex >= 0) {
                    //netInitSuccessFlag[curNetDevIndex] = false;
                    //netInitFailCounter[curNetDevIndex]++;

                    if (dev_array[curNetDevIndex] != null && !skipInitCheck) {
                        //dev_array[curNetDevIndex].setName("");
                    }

                    skipInitCheck = false;
                }
            }
        };

        if (appMode == 3 && !bRemoteAccess) {
            new ScanTask(/*tvScanning, tvResult*/).execute();
        }


        if (myTimer1 != null) {
            myTimer1.cancel();
            myTimer1 = null;
        }


        lvDevice = (ListView) findViewById(R.id.listDevices);
        mDeviceList = new ArrayList<>();

        // add data for list
        for (int i = 0; i < dev_array_size; i++) {
            if (dev_array[i] != null && !dev_array[i].getName().isEmpty()) {

                if (dev_array[i].getAlgorithm_number() != 888) {
                    mDeviceList.add(new Device(dev_array[i].getName(), dev_array[i].getId(), dev_array[i].getIp(), "",
                            dev_array[i].getPassword1(), dev_array[i].getPassword2(), dev_array[i].getPassword3(), dev_array[i].getAddress(),
                            dev_array[i].getSystem_status(), dev_array[i].getAlarm_status(), dev_array[i].getAlgorithm_number(), dev_array[i].getMinutes(), dev_array[i].getHours()));
                } else {
                    mDeviceList.add(new Device(dev_array[i].getName(), dev_array[i].getId(), dev_array[i].getIp(), "",
                             dev_array[i].getPassword1(), dev_array[i].getPassword2(), dev_array[i].getPassword3(),dev_array[i].getAddress(),
                            dev_array[i].getSystem_status()+2, dev_array[i].getAlarm_status(), dev_array[i].getAlgorithm_number(), dev_array[i].getMinutes(), dev_array[i].getHours()));
                }
            }
        }

        if (mDeviceList.isEmpty()) {
            mDeviceList.add(new Device("Идет поиск устройств...", "", "", "","","", "",
                    0, 0, 0, 0, 0, 0));
        }


        adapter = new DeviceListAdaptor(getApplicationContext(), R.layout.device_list, mDeviceList);
        lvDevice.setAdapter(adapter);


        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ModbusReq.get+Instance().destory();

                if (dev_array[0] != null && !dev_array[0].getName().isEmpty()) {
                    String name = mDeviceList.get(position).getName();
                    String ipString = mDeviceList.get(position).getIp();
                    String sSerialNumber = mDeviceList.get(position).getId();
                    String sPassword1 = mDeviceList.get(position).getPassword1();
                    String sPassword2 = mDeviceList.get(position).getPassword2();
                    String sPassword3 = mDeviceList.get(position).getPassword3();
                    if (bRemoteAccess) ipString = sGatewayIP;
                    int alg = mDeviceList.get(position).getAlgorithm_number();
                    int address = mDeviceList.get(position).getAddress();
                    if (appMode != 2) address = 2;
                    //Intent intent = new Intent(getApplicationContext(), MainVentActivity.class);
                    Intent intent = new Intent(getApplicationContext(), NewMainActivity.class);
                    intent.putExtra("device_address", address);
                    intent.putExtra("app_mode", appMode);
                    intent.putExtra("system_name", name);
                    intent.putExtra("device_ip", ipString);
                    intent.putExtra("device_algorithm", alg);
                    intent.putExtra("port_number", sPortNumber);
                    intent.putExtra("serial_number", sSerialNumber);
                    intent.putExtra("password1", sPassword1);
                    intent.putExtra("password2", sPassword2);
                    intent.putExtra("password3", sPassword3);
                    startActivity(intent);

                    //wait_bar.setVisibility(View.VISIBLE);
                    newIntent = true;
                }
            }
        });

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, delay, period);
    }

    private class ScanTask extends AsyncTask<Void, String, Void> {


        public ScanTask(/*TextView tvCurrentScanning, TextView tvScanResullt*/) {
            //this.tvCurrentScanning = tvCurrentScanning;
            //this.tvScanResullt = tvScanResullt;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            scanInetAddresses();
            return null;
        }

        private void scanInetAddresses(){
            //May be you have to adjust the timeout
            final int timeout = 500;

            if(inetAddresses == null){
                inetAddresses = new ArrayList<>();
            }
            //inetAddresses.clear();

            if(canonicalHostNames == null){
                canonicalHostNames = new ArrayList<>();
            }
            //canonicalHostNames.clear();

            //scan 192.168.1.xxx
            //int size = Integer.valueOf(sDeviceGroupSize);
            localGatewayIP[0] = gatewayIP[0];
            localGatewayIP[1] = gatewayIP[1];
            localGatewayIP[2] = gatewayIP[2];
            for (byte j = gatewayIP[3]; j < gatewayIP[3]+size; j++) {
                localGatewayIP[3] = j;
                try {
                    InetAddress checkAddress = InetAddress.getByAddress(localGatewayIP);
                    publishProgress(checkAddress.getCanonicalHostName());
                    if (checkAddress.isReachable(timeout)/* && checkAddress.getCanonicalHostName().contains("ESP")*/) {
                        inetAddresses.add(checkAddress);
                        canonicalHostNames.add(checkAddress.getCanonicalHostName());
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    publishProgress(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    publishProgress(e.getMessage());
                }
            }
        }
    }

    public void connectWiFi(ScanResult scanResult) {
        try {

            String networkSSID = scanResult.SSID;
            String networkPass = "911911911";

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (scanResult.capabilities.toUpperCase().contains("WPA")) {
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                conf.preSharedKey = "\"" + networkPass + "\"";
            }

            int networkId = wifiManager.addNetwork(conf);
            WifiInfo wifi_inf = wifiManager.getConnectionInfo();

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {

                    wifiManager.disableNetwork(wifi_inf.getNetworkId());

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);

                    break;
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ошибка подключения к сети...", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (myTimer1 != null) {

            myTimer1.cancel();
            myTimer1 = null;

        } else {
            //getApplicationContext().unregisterReceiver(wifiScanReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        appMode = sPref.getInt("APP_MODE", 1);

        wait_bar.setVisibility(View.INVISIBLE);

        /*if (newIntent) {
            newIntent = false;
            curNetDevIndex = -1;
            skipInitCheck = true;
        }*/

        if (myTimer1 != null) {

            myTimer1.cancel();
            myTimer1 = null;

        } else {
            /*if (wifiScanReceiver == null) {
                wifiScanReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context c, Intent intent) {
                        boolean success = intent.getBooleanExtra(
                                WifiManager.EXTRA_RESULTS_UPDATED, false);
                        if (success) {
                            scanSuccess();
                        } else {
                            // scan failure handling
                            scanFailure();
                        }
                    }
                };
            }
            getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);*/
        }

        myTimer1 = new Timer();
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, delay, period);
    }

    private void scanSuccess() {
        int netSize = 0;
        String strNetwork = "";
        List<ScanResult> results = wifiManager.getScanResults();
        if (results.size() != 0) {
            for (ScanResult scanResult : results) {
                if (scanResult.SSID.contains("OTLogic") && !scanResult.SSID.contains("ConfigAP"))
                    netSize++;
            }

            if (netSize == 1) {
                for (ScanResult scanResult : results) {
                    if (scanResult.SSID.contains("OTLogic") && !scanResult.SSID.contains("ConfigAP")) {

                        WifiInfo wifi_inf = wifiManager.getConnectionInfo();
                        connectWiFi(scanResult);
                        strNetwork = scanResult.SSID;
                    }
                }

                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("SSID", strNetwork);
                ed.commit();

            } else {

            }
        } else {
            Toast.makeText(getApplicationContext(), "wifi сетей не обнаружено...", Toast.LENGTH_SHORT).show();
        }
    }
    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        //List<ScanResult> results = wifiManager.getScanResults();
        //Toast.makeText(getApplicationContext(), "wifi не работает...", Toast.LENGTH_SHORT).show();

        if (wifiScanFailureCounter < 3) {
            wifiScanFailureCounter++;

            Toast.makeText(getApplicationContext(), "Автоматическое подключение к сети не удалось. Выберите сеть Wifi вручную.", Toast.LENGTH_SHORT).show();
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

    private void deviceListInit() {
        for (int i = 0; i < dev_array_size; i++) {
            if (dev_array[i] != null) {
                dev_array[i].setName("");
            }
        }
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here
            //WifiInfo wifi_inf = wifiManager.getConnectionInfo();

            if (appMode == 1) {

                /*if (!wifi_inf.getSSID().contains("OTLogic") && wifiScanFailureCounter < 3) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((MainActivity)getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else {
                        boolean success = wifiManager.startScan();
                        if (!success) {
                            // scan failure handling
                            scanFailure();
                        }
                    }
                }*/
            }

            size = Integer.valueOf(sDeviceGroupSize);


            // direct connection mode
            if (appMode == 1) {
                if (curNetDevIndex < 0 || !netInitSuccessFlag[curNetDevIndex]) {
                    if (curNetDevIndex >= 0)
                        ModbusReq.getInstance().destory();
                    if (++curNetDevIndex >= 1)
                        curNetDevIndex = 0;

                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost("10.10.100.254")
                            .setPort(Integer.parseInt(sPortNumber))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(timeout)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);

                } else {
                    ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                            2, 0, nInputRegistersData.length);
                }
            }

            if (appMode == 2) {
                if (curNetDevIndex < 0 || !netInitSuccessFlag[curNetDevIndex]) {
                    if (curNetDevIndex >= 0) {
                        ModbusReq.getInstance().destory();
                    }
                    if (++curNetDevIndex >= size)
                        curNetDevIndex = 0;

                    ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost("10.10.100.254")
                            .setPort(Integer.parseInt(sPortNumber))
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(timeout)
                            .setRetries(0))
                            .init(onRequestBackModbusInit);

                } else {
                    ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                            curNetDevIndex + 2, 0, nInputRegistersData.length);
                }
            }

            // router connection mode
            if (appMode == 3) {
                if (bRemoteAccess) {
                    if (curNetDevIndex < 0 || !netInitSuccessFlag[curNetDevIndex]) {
                        if (curNetDevIndex >= 0)
                            ModbusReq.getInstance().destory();
                        if (++curNetDevIndex >= 1)
                            curNetDevIndex = 0;

                        ModbusReq.getInstance().setParam(new ModbusParam()
                                .setHost(sGatewayIP)
                                .setPort(Integer.parseInt(sPortNumber))
                                .setEncapsulated(false)
                                .setKeepAlive(true)
                                .setTimeout(timeout)
                                .setRetries(0))
                                .init(onRequestBackModbusInit);

                    } else {
                        ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                                2, 0, nInputRegistersData.length);
                    }
                } else if (!canonicalHostNames.isEmpty()) {
                    if (curNetDevIndex < 0 || !netInitSuccessFlag[curNetDevIndex]) {
                        if (curNetDevIndex >= 0) {
                            ModbusReq.getInstance().destory();
                        }
                        if (++curNetDevIndex >= canonicalHostNames.size())
                            curNetDevIndex = 0;

                        String s1 = inetAddresses.get(curNetDevIndex).getHostAddress();

                        ModbusReq.getInstance().setParam(new ModbusParam()
                                .setHost(inetAddresses.get(curNetDevIndex).getHostAddress())
                                .setPort(Integer.parseInt(sPortNumber))
                                .setEncapsulated(false)
                                .setKeepAlive(true)
                                .setTimeout(timeout)
                                .setRetries(0))
                                .init(onRequestBackModbusInit);
                    } else {
                        ModbusReq.getInstance().readInputRegisters(onRequestBackReadInputRegisters,
                                2, 0, nInputRegistersData.length);
                    }

                } else {
                    curNetDevIndex = -1;
                    mDeviceList.clear();
                    mDeviceList.add(new Device("Идет поиск устройств...", "", "", "","","", "",
                            0, 0, 0, 0, 0, 0));
                    adapter.notifyDataSetChanged();
                    deviceListInit();
                    return;
                }
            }



            mDeviceList.clear();

            // add data for list
            for (int i = 0; i < dev_array_size; i++) {
                if (dev_array[i] != null && !dev_array[i].getName().isEmpty()) {
                    mDeviceList.add(new Device(dev_array[i].getName(), dev_array[i].getId(), dev_array[i].getIp(), "OTLogic"/*wifi_inf.getSSID()*/,
                            dev_array[i].getPassword1(), dev_array[i].getPassword2(), dev_array[i].getPassword3(),dev_array[i].getAddress(),
                            dev_array[i].getSystem_status(), dev_array[i].getAlarm_status(), dev_array[i].getAlgorithm_number(), dev_array[i].getMinutes(), dev_array[i].getHours()));
                }
            }

            if (mDeviceList.isEmpty()) {
                mDeviceList.add(new Device("Идет поиск устройств...", "", "", "", "","", "",
                        0, 0, 0, 0, 0, 0));
                wait_bar.setVisibility(View.VISIBLE);
            } else {
                wait_bar.setVisibility(View.INVISIBLE);
            }

            adapter.notifyDataSetChanged();
        }
    };
}

