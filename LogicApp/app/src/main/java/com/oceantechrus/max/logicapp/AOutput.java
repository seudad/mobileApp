package com.oceantechrus.max.logicapp;

public class AOutput {
    private String name;
    private int value;
    private boolean hand_mode;
    private int hand_mode_value;
    private boolean inversion;
    private int min;
    private int max;

    public AOutput(String name, int value, boolean hand_mode, int hand_mode_value, boolean inversion, int min, int max) {
        this.name = name;
        this.value = value;
        this.hand_mode = hand_mode;
        this.hand_mode_value = hand_mode_value;
        this.inversion = inversion;
        this.min = min;
        this.max = max;
    }

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

    public boolean isInversion() {
        return inversion;
    }

    public void setInversion(boolean inversion) {
        this.inversion = inversion;
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
}
