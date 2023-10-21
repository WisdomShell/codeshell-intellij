package com.codeshell.intellij.enums;

public enum ChatMaxToken {
    LOW("1024"),
    MEDIUM("2048"),
    HIGH("4096"),
    ULTRA("8192");

    private final String description;

    ChatMaxToken(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
