package com.oceantechrus.max.logicapp;

public class MainScreenParameter {
    private String system_name;
    private String name;
    private String value;
    private String units;
    private int type;
    private String system_state;
    private String system_mode;
    private Boolean alarm_flag;

    public MainScreenParameter(String system_name, String name, String value, String units, int type, String system_state, String system_mode, Boolean alarm_flag) {
        this.system_name = system_name;
        this.name = name;
        this.value = value;
        this.units = units;
        this.type = type;
        this.system_state = system_state;
        this.system_mode = system_mode;
        this.alarm_flag = alarm_flag;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSystem_state() {
        return system_state;
    }

    public void setSystem_state(String system_state) {
        this.system_state = system_state;
    }

    public String getSystem_mode() {
        return system_mode;
    }

    public void setSystem_mode(String system_mode) {
        this.system_mode = system_mode;
    }

    public Boolean getAlarm_flag() {
        return alarm_flag;
    }

    public void setAlarm_flag(Boolean alarm_flag) {
        this.alarm_flag = alarm_flag;
    }
}

