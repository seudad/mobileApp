package com.oceantechrus.max.logicapp;

public class UInput {
    private String name;
    private int value;
    private boolean hand_mode;
    private int hand_mode_value;
    private int offset;
    private int offsetR;
    private int min;
    private int max;
    private int sensor;

    public UInput(String name, int value, boolean hand_mode, int hand_mode_value, int offset, int offsetR, int min, int max, int sensor) {
        this.name = name;
        this.value = value;
        this.hand_mode = hand_mode;
        this.hand_mode_value = hand_mode_value;
        this.offset = offset;
        this.offsetR = offsetR;
        this.min = min;
        this.max = max;
        this.sensor = sensor;
    }

    // setter, getter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isHand_mode() {
        return hand_mode;
    }

    public void setHand_mode(boolean hand_mode) {
        this.hand_mode = hand_mode;
    }

    public int getHand_mode_value() {
        return hand_mode_value;
    }

    public void setHand_mode_value(int hand_mode_value) {
        this.hand_mode_value = hand_mode_value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffsetR() {
        return offsetR;
    }

    public void setOffsetR(int offsetR) {
        this.offsetR = offsetR;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }
}
