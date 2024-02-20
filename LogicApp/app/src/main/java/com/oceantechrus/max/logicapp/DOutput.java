package com.oceantechrus.max.logicapp;

public class DOutput {
    private String name;
    private int state;
    private boolean hand_mode;
    private int hand_mode_state;
    private boolean inversion;

    public DOutput(String name, int state, boolean hand_mode, int hand_mode_state, boolean inversion) {
        this.name = name;
        this.state = state;
        this.hand_mode = hand_mode;
        this.hand_mode_state = hand_mode_state;
        this.inversion = inversion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isHand_mode() {
        return hand_mode;
    }

    public void setHand_mode(boolean hand_mode) {
        this.hand_mode = hand_mode;
    }

    public int getHand_mode_state() {
        return hand_mode_state;
    }

    public void setHand_mode_state(int hand_mode_state) {
        this.hand_mode_state = hand_mode_state;
    }

    public boolean isInversion() {
        return inversion;
    }

    public void setInversion(boolean inversion) {
        this.inversion = inversion;
    }
}
