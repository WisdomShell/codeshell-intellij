package com.codeshell.intellij.actions.assistants;


import com.codeshell.intellij.services.CodeShellSideWindowService;
import com.codeshell.intellij.window.CodeShellSideWindow;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenDevTools extends DumbAwareAction {
    public OpenDevTools() {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            CodeShellSideWindow codeShellSideWindow = Objects.requireNonNull(e.getProject()).getService(CodeShellSideWindowService.class).getCodeShellSideWindow();
            codeShellSideWindow.jbCefBrowser().openDevtools();
        } catch (Exception exception) {
            Logger.getInstance(this.getClass()).error("openDevtools exception", exception);
        }

    }
}