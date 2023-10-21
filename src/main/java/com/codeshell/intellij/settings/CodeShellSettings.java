package com.codeshell.intellij.settings;

import com.codeshell.intellij.enums.ChatMaxToken;
import com.codeshell.intellij.enums.CompletionMaxToken;
import com.codeshell.intellij.enums.TabActionOption;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(name = "CodeShellSettings", storages = @Storage("codeshell_settings.xml"))
public class CodeShellSettings implements PersistentStateComponent<Element> {
    public static final String SETTINGS_TAG = "CodeShellSettings";
    private static final String SERVER_ADDRESS_TAG = "SERVER_ADDRESS_URL";
    private static final String SAYT_TAG = "SAYT_ENABLED";
    private static final String CPU_RADIO_BUTTON_TAG = "CPU_RADIO_BUTTON_ENABLED";
    private static final String GPU_RADIO_BUTTON_TAG = "GPU_RADIO_BUTTON_ENABLED";
    private static final String TAB_ACTION_TAG = "TAB_ACTION";
    private static final String COMPLETION_MAX_TOKENS_TAG = "COMPLETION_MAX_TOKENS";
    private static final String CHAT_MAX_TOKENS_TAG = "CHAT_MAX_TOKENS";
    private boolean saytEnabled = true;
    private boolean cpuRadioButtonEnabled = true;
    private boolean gpuRadioButtonEnabled = false;
    private String serverAddressURL = "http://127.0.0.1:8080";
    private TabActionOption tabActionOption = TabActionOption.ALL;
    private CompletionMaxToken completionMaxToken = CompletionMaxToken.MEDIUM;
    private ChatMaxToken chatMaxToken = ChatMaxToken.MEDIUM;

    private static final CodeShellSettings SHELL_CODER_SETTINGS_INSTANCE = new CodeShellSettings();

    @Override
    public @Nullable Element getState() {
        Element state = new Element(SETTINGS_TAG);
        state.setAttribute(CPU_RADIO_BUTTON_TAG, Boolean.toString(isCPURadioButtonEnabled()));
        state.setAttribute(GPU_RADIO_BUTTON_TAG, Boolean.toString(isGPURadioButtonEnabled()));
        state.setAttribute(SERVER_ADDRESS_TAG, getServerAddressURL());
        state.setAttribute(SAYT_TAG, Boolean.toString(isSaytEnabled()));
        state.setAttribute(TAB_ACTION_TAG, getTabActionOption().name());
        state.setAttribute(COMPLETION_MAX_TOKENS_TAG, getCompletionMaxToken().name());
        state.setAttribute(CHAT_MAX_TOKENS_TAG, getChatMaxToken().name());
        return state;
    }

    @Override
    public void loadState(@NotNull Element state) {
        if (Objects.nonNull(state.getAttributeValue(CPU_RADIO_BUTTON_TAG))) {
            setCPURadioButtonEnabled(Boolean.parseBoolean(state.getAttributeValue(CPU_RADIO_BUTTON_TAG)));
        }
        if (Objects.nonNull(state.getAttributeValue(GPU_RADIO_BUTTON_TAG))) {
            setGPURadioButtonEnabled(Boolean.parseBoolean(state.getAttributeValue(GPU_RADIO_BUTTON_TAG)));
        }
        if (Objects.nonNull(state.getAttributeValue(SERVER_ADDRESS_TAG))) {
            setServerAddressURL(state.getAttributeValue(SERVER_ADDRESS_TAG));
        }
        if (Objects.nonNull(state.getAttributeValue(SAYT_TAG))) {
            setSaytEnabled(Boolean.parseBoolean(state.getAttributeValue(SAYT_TAG)));
        }
        if (Objects.nonNull(state.getAttributeValue(TAB_ACTION_TAG))) {
            setTabActionOption(TabActionOption.valueOf(state.getAttributeValue(TAB_ACTION_TAG)));
        }
        if (Objects.nonNull(state.getAttributeValue(COMPLETION_MAX_TOKENS_TAG))) {
            setCompletionMaxToken(CompletionMaxToken.valueOf(state.getAttributeValue(COMPLETION_MAX_TOKENS_TAG)));
        }
        if (Objects.nonNull(state.getAttributeValue(CHAT_MAX_TOKENS_TAG))) {
            setChatMaxToken(ChatMaxToken.valueOf(state.getAttributeValue(CHAT_MAX_TOKENS_TAG)));
        }
    }

    public static CodeShellSettings getInstance() {
        if (Objects.isNull(ApplicationManager.getApplication())) {
            return SHELL_CODER_SETTINGS_INSTANCE;
        }

        CodeShellSettings service = ApplicationManager.getApplication().getService(CodeShellSettings.class);
        if (Objects.isNull(service)) {
            return SHELL_CODER_SETTINGS_INSTANCE;
        }
        return service;
    }

    public boolean isSaytEnabled() {
        return saytEnabled;
    }

    public void setSaytEnabled(boolean saytEnabled) {
        this.saytEnabled = saytEnabled;
    }

    public void toggleSaytEnabled() {
        this.saytEnabled = !this.saytEnabled;
    }

    public boolean isCPURadioButtonEnabled() {
        return cpuRadioButtonEnabled;
    }

    public void setCPURadioButtonEnabled(boolean cpuRadioButtonEnabled) {
        this.cpuRadioButtonEnabled = cpuRadioButtonEnabled;
    }

    public boolean isGPURadioButtonEnabled() {
        return gpuRadioButtonEnabled;
    }

    public void setGPURadioButtonEnabled(boolean gpuRadioButtonEnabled) {
        this.gpuRadioButtonEnabled = gpuRadioButtonEnabled;
    }

    public String getServerAddressURL() {
        return serverAddressURL;
    }

    public void setServerAddressURL(String serverAddressURL) {
        this.serverAddressURL = serverAddressURL;
    }

    public CompletionMaxToken getCompletionMaxToken() {
        return completionMaxToken;
    }

    public void setCompletionMaxToken(CompletionMaxToken completionMaxToken) {
        this.completionMaxToken = completionMaxToken;
    }

    public ChatMaxToken getChatMaxToken() {
        return chatMaxToken;
    }

    public void setChatMaxToken(ChatMaxToken chatMaxToken) {
        this.chatMaxToken = chatMaxToken;
    }

    public TabActionOption getTabActionOption() {
        return tabActionOption;
    }

    public void setTabActionOption(TabActionOption tabActionOption) {
        this.tabActionOption = tabActionOption;
    }

}
