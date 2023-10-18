package com.codeshell.intellij.settings;

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
        return !savedSettings.getCompleteURL().equals(settingsPanel.getServerAddress())
                || savedSettings.getTabActionOption() != settingsPanel.getTabActionOption()
                || savedSettings.isSaytEnabled() != settingsPanel.getEnableSAYTCheckBox()
                || savedSettings.getCompletionMaxToken() != settingsPanel.getCompletionMaxTokens()
                || savedSettings.getChatMaxToken() != settingsPanel.getChatMaxTokens();
    }

    @Override
    public void apply() {
        CodeShellSettings savedSettings = CodeShellSettings.getInstance();
        savedSettings.setCompleteURL(settingsPanel.getServerAddress());
        savedSettings.setSaytEnabled(settingsPanel.getEnableSAYTCheckBox());
        savedSettings.setTabActionOption(settingsPanel.getTabActionOption());
        savedSettings.setCompletionMaxToken(settingsPanel.getCompletionMaxTokens());
        savedSettings.setChatMaxToken(settingsPanel.getChatMaxTokens());

        for (Project openProject : ProjectManager.getInstance().getOpenProjects()) {
            WindowManager.getInstance().getStatusBar(openProject).updateWidget(CodeShellWidget.ID);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sendUrl", CodeShellSettings.getInstance().getCompleteURL());
        jsonObject.addProperty("maxToken", CodeShellSettings.getInstance().getChatMaxToken().getDescription());
        JsonObject result = new JsonObject();
        result.addProperty("data", jsonObject.toString());
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContextFromFocus().getResultSync());
        (project.getService(CodeShellSideWindowService.class)).notifyIdeAppInstance(result);
    }

    @Override
    public void reset() {
        CodeShellSettings savedSettings = CodeShellSettings.getInstance();
        settingsPanel.setServerAddress(savedSettings.getCompleteURL());
        settingsPanel.setEnableSAYTCheckBox(savedSettings.isSaytEnabled());
        settingsPanel.setTabActionOption(savedSettings.getTabActionOption());
        settingsPanel.setCompletionMaxTokens(savedSettings.getCompletionMaxToken());
        settingsPanel.setChatMaxTokens(savedSettings.getChatMaxToken());
    }
}
