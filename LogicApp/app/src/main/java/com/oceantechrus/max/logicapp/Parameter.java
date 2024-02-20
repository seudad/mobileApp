package com.oceantechrus.max.logicapp;

public class Parameter {
    private String name;
    private String value;

    // constructor
    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // setter, getter

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
