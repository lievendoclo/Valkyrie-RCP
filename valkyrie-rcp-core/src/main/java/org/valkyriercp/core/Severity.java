package org.valkyriercp.core;

public enum Severity {
    INFO(0, "info"),
    WARNING(50, "warning"),
    ERROR(100, "error")
    ;
    private int weight;
    private String label;

    Severity(int weight, String label) {
        this.weight = weight;
        this.label = label;
    }

    public int getWeight() {
        return weight;
    }

    public String getLabel() {
        return label;
    }
}
