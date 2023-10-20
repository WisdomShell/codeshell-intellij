package com.codeshell.intellij.enums;

public enum CompletionMaxToken {

    LOW("32"),
    MEDIUM("64"),
    HIGH("128"),
    ULTRA("256");

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
