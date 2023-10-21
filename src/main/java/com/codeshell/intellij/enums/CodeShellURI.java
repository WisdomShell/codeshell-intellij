package com.codeshell.intellij.enums;

public enum CodeShellURI {

    CPU_COMPLETE("/infill"),
    CPU_CHAT("/completion"),
    GPU_COMPLETE("/completion"),
    GPU_CHAT("/assistants");

    private final String uri;

    CodeShellURI(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

}
