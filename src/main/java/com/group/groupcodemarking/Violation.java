package com.group.groupcodemarking;

public class Violation {
    private int id;
    private String description;
    private String className;

    public Violation(int id, String name, String label) {
        this.id = id;
        this.description = name;
        this.className = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Violation{" +
                "description='" + description + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
