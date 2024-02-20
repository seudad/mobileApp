package com.oceantechrus.max.logicapp;

public class Device {
    private String name;
    private String id;
    private String ip;
    private String ssid;
    private String password1;
    private String password2;
    private String password3;
    private int system_status;
    private int alarm_status;
    private int address;
    private int algorithm_number;
    private int minutes;
    private int hours;

    public Device(String name, String id, String ip, String ssid, String password1, String password2, String password3,
                  int address, int system_status, int alarm_status, int algorithm_number, int minutes, int hours) {
        this.name = name;
        this.id = id;
        this.ip = ip;
        this.ssid = ssid;
        this.password1 = password1;
        this.password2 = password2;
        this.password3 = password3;
        this.address = address;
        this.system_status = system_status;
        this.alarm_status = alarm_status;
        this.algorithm_number = algorithm_number;
        this.minutes = minutes;
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword3() {
        return password3;
    }

    public void setPassword3(String password3) {
        this.password3 = password3;
    }

    public int getSystem_status() {
        return system_status;
    }

    public int getAlgorithm_number() {
        return algorithm_number;
    }

    public void setSystem_status(int system_status) {
        this.system_status = system_status;
    }

    public int getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(int alarm_status) {
        this.alarm_status = alarm_status;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setAlgorithm_number(int algorithm_number) {
        this.algorithm_number = algorithm_number;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
