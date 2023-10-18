package com.codeshell.intellij.handlers;

import com.intellij.openapi.project.Project;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;

public class CustomSchemeHandlerFactory implements CefSchemeHandlerFactory {
    private final Project project;

    public CustomSchemeHandlerFactory(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public CefResourceHandler create(CefBrowser browser, CefFrame frame, String schemeName, CefRequest request) {
        return new CustomResourceHandler(this.project);
    }
}
