package com.codeshell.intellij.enums;

public enum TabActionOption {
    ALL("All suggestions");

    private final String description;

    TabActionOption(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
