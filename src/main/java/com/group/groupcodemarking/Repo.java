package com.group.groupcodemarking;

public class Repo {
    private String value;
    private String label;

    public Repo(String name, String label) {
        this.value = name;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
