package com.codeshell.intellij.services;

import com.codeshell.intellij.window.CodeShellSideWindow;
import com.google.gson.JsonObject;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.cef.browser.CefBrowser;
import org.jetbrains.annotations.NotNull;

@Service
public final class CodeShellSideWindowService {

    private final Project project;
    private final CodeShellSideWindow codeShellSideWindow;

    public Project getProject() {
        return this.project;
    }

    public CodeShellSideWindow getCodeShellSideWindow() {
        return this.codeShellSideWindow;
    }

    public CodeShellSideWindowService(Project project) {
        this.project = project;
        this.codeShellSideWindow = new CodeShellSideWindow(project);
    }


    public void notifyIdeAppInstance(@NotNull JsonObject result) {
        CefBrowser browser = this.getCodeShellSideWindow().jbCefBrowser().getCefBrowser();
        browser.executeJavaScript("window.postMessage(" + result + ",'*');", browser.getURL(), 0);
    }
}
