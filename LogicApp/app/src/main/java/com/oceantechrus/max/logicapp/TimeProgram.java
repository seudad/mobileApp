package com.oceantechrus.max.logicapp;

public class TimeProgram {
    private int sec_on;
    private int min_on;
    private int hour_on;
    private int sec_off;
    private int min_off;
    private int hour_off;
    private int weekdays;
    private boolean enabled;

    public TimeProgram(int sec_on, int min_on, int hour_on, int sec_off, int min_off, int hour_off, int weekdays, boolean enabled) {
        this.sec_on = sec_on;
        this.min_on = min_on;
        this.hour_on = hour_on;
        this.sec_off = sec_off;
        this.min_off = min_off;
        this.hour_off = hour_off;
        this.weekdays = weekdays;
        this.enabled = enabled;
    }

    public int getSec_on() {
        return sec_on;
    }

    public void setSec_on(int sec_on) {
        this.sec_on = sec_on;
    }

    public int getMin_on() {
        return min_on;
    }

    public void setMin_on(int min_on) {
        this.min_on = min_on;
    }

    public int getHour_on() {
        return hour_on;
    }

    public void setHour_on(int hour_on) {
        this.hour_on = hour_on;
    }

    public int getSec_off() {
        return sec_off;
    }

    public void setSec_off(int sec_off) {
        this.sec_off = sec_off;
    }

    public int getMin_off() {
        return min_off;
    }

    public void setMin_off(int min_off) {
        this.min_off = min_off;
    }

    public int getHour_off() {
        return hour_off;
    }

    public void setHour_off(int hour_off) {
        this.hour_off = hour_off;
    }

    public int getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(int weekdays) {
        this.weekdays = weekdays;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
