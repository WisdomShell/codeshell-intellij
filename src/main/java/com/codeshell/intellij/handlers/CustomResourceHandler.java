package com.codeshell.intellij.handlers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;


public class CustomResourceHandler implements CefResourceHandler {

    private final Logger logger = Logger.getInstance(this.getClass());

    private final Project project;

    private ResourceHandlerState resourceHandlerState;

    public CustomResourceHandler(@NotNull Project project) {
        this.project = project;
    }

    private void setResourceHandlerState(ResourceHandlerState resourceHandlerState) {
        this.resourceHandlerState = resourceHandlerState;
    }

    private ResourceHandlerState getResourceHandlerState() {
        return this.resourceHandlerState;
    }

    @Override
    public boolean processRequest(CefRequest request, CefCallback callback) {
        boolean isRequestProcessed;
        try {
            URL destURL;
            String url = request.getURL();
            String[] resourcePath = url.replace("http://codeshell/", "webview/").split(Pattern.quote("?"));
            String pathToResource = (resourcePath.length > 0) ? resourcePath[0] : "webview/index.html";
            URL newUrl = getClass().getClassLoader().getResource(pathToResource);
            if (Objects.nonNull(newUrl)) {
                if (resourcePath.length > 1) {
                    destURL = new URL(newUrl + "#/?" + resourcePath[1]);
                } else {
                    destURL = newUrl;
                }
            } else {
                destURL = getClass().getClassLoader().getResource("webview/index.html");
            }
            OpenedConnection openedConnection = new OpenedConnection((Objects.requireNonNull(destURL)).openConnection());
            setResourceHandlerState(openedConnection);
            callback.Continue();
            isRequestProcessed = true;
        } catch (IOException e) {
            logger.error("JBCefBrowser# request failed:{}", e);
            isRequestProcessed = false;
        }
        return isRequestProcessed;
    }

    @Override
    public void getResponseHeaders(CefResponse response, IntRef responseLength, StringRef redirectUrl) {
        getResourceHandlerState().getResponseHeaders(response, responseLength, redirectUrl);
    }

    @Override
    public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
        boolean isResponseRead;
        try {
            isResponseRead = getResourceHandlerState().readResponse(dataOut, bytesToRead, bytesRead, callback);
        } catch (IOException e) {
            logger.error("JBCefBrowser# readResponse failed:{}", e);
            isResponseRead = false;
        }
        return isResponseRead;
    }

    @Override
    public void cancel() {
        try {
            getResourceHandlerState().close();
            setResourceHandlerState(null);
        } catch (IOException e) {
            logger.error("JBCefBrowser# cancel failed:{}", e);
        }

    }
}





