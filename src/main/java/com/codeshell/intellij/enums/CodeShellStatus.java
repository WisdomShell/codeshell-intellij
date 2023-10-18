package com.codeshell.intellij.enums;

import java.util.stream.Stream;

public enum CodeShellStatus {
    UNKNOWN(0, "Unknown"),
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad request/token"),
    NOT_FOUND(404, "404 Not found"),
    TOO_MANY_REQUESTS(429, "Too many requests right now");

    private final int code;
    private final String displayValue;

    CodeShellStatus(int i, String s) {
        code = i;
        displayValue = s;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static CodeShellStatus getStatusByCode(int code) {
        return Stream.of(CodeShellStatus.values())
                .filter(s -> s.getCode() == code)
                .findFirst()
                .orElse(CodeShellStatus.UNKNOWN);
    }
}
