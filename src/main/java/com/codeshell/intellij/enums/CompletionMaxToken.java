package com.codeshell.intellij.enums;

public enum CompletionMaxToken {

    LOW("128"),
    MEDIUM("512"),
    HIGH("1024"),
    ULTRA("2048");

    private final String description;

    CompletionMaxToken(String description) {
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
