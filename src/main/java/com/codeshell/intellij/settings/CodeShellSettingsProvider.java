package com.codeshell.intellij.settings;

import com.codeshell.intellij.enums.CodeShellURI;
import com.codeshell.intellij.services.CodeShellSideWindowService;
import com.codeshell.intellij.widget.CodeShellWidget;
import com.google.gson.JsonObject;
import com.intellij.application.options.editor.EditorOptionsProvider;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class CodeShellSettingsProvider implements EditorOptionsProvider {
    private SettingsPanel settingsPanel;

    @Override
    public @NotNull @NonNls String getId() {
        return "CodeShell.Settings";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "CodeShell";
    }

    @Override
    public @Nullable JComponent createComponent() {
        if (Objects.isNull(settingsPanel)) {
            settingsPanel = new SettingsPanel();
        }
        return settingsPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        CodeShellSettings savedSettings = CodeShellSettings.getInstance();
        return !savedSettings.getServerAddressURL().equals(settingsPanel.getServerAddressUrl())
                || savedSettings.getTabActionOption() != settingsPanel.getTabActionOption()
                || savedSettings.isSaytEnabled() != settingsPanel.getEnableSAYTCheckBox()
                || savedSettings.isCPURadioButtonEnabled() != settingsPanel.getCPUModelRadioButton()
                || savedSettings.isGPURadioButtonEnabled() != settingsPanel.getGPUModelRadioButton()
                || savedSettings.getCompletionMaxToken() != settingsPanel.getCompletionMaxTokens()
                || savedSettings.getChatMaxToken() != settingsPanel.getChatMaxTokens();
    }

    @Override
    public void apply() {
        CodeShellSettings savedSettings = CodeShellSettings.getInstance();
        savedSettings.setServerAddressURL(settingsPanel.getServerAddressUrl());
        savedSettings.setSaytEnabled(settingsPanel.getEnableSAYTCheckBox());
        savedSettings.setCPURadioButtonEnabled(settingsPanel.getCPUModelRadioButton());
        savedSettings.setGPURadioButtonEnabled(settingsPanel.getGPUModelRadioButton());
        savedSettings.setTabActionOption(settingsPanel.getTabActionOption());
        savedSettings.setCompletionMaxToken(settingsPanel.getCompletionMaxTokens());
        savedSettings.setChatMaxToken(settingsPanel.getChatMaxTokens());

        for (Project openProject : ProjectManager.getInstance().getOpenProjects()) {
            WindowManager.getInstance().getStatusBar(openProject).updateWidget(CodeShellWidget.ID);
        }

        JsonObject jsonObject = new JsonObject();
        if(CodeShellSettings.getInstance().isCPURadioButtonEnabled()){
            jsonObject.addProperty("sendUrl", CodeShellSettings.getInstance().getServerAddressURL() + CodeShellURI.CPU_CHAT.getUri());
            jsonObject.addProperty("modelType", "CPU");
        }else{
            jsonObject.addProperty("sendUrl", CodeShellSettings.getInstance().getServerAddressURL() + CodeShellURI.GPU_CHAT.getUri());
            jsonObject.addProperty("modelType", "GPU");
        }
        jsonObject.addProperty("maxToken", CodeShellSettings.getInstance().getChatMaxToken().getDescription());
        JsonObject result = new JsonObject();
        result.addProperty("data", jsonObject.toString());
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContextFromFocus().getResultSync());
        (project.getService(CodeShellSideWindowService.class)).notifyIdeAppInstance(result);
    }

    @Override
    public void reset() {
        CodeShellSettings savedSettings = CodeShellSettings.getInstance();
        settingsPanel.setServerAddressUrl(savedSettings.getServerAddressURL());
        settingsPanel.setEnableSAYTCheckBox(savedSettings.isSaytEnabled());
        settingsPanel.setTabActionOption(savedSettings.getTabActionOption());
        settingsPanel.setCPUModelRadioButton(savedSettings.isCPURadioButtonEnabled());
        settingsPanel.setGPUModelRadioButton(savedSettings.isGPURadioButtonEnabled());
        settingsPanel.setCompletionMaxTokens(savedSettings.getCompletionMaxToken());
        settingsPanel.setChatMaxTokens(savedSettings.getChatMaxToken());
    }
}
