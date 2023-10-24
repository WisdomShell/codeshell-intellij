package com.codeshell.intellij.handlers;

import com.intellij.openapi.diagnostic.Logger;
import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class OpenedConnection implements ResourceHandlerState {
    private InputStream inputStream;
    private final URLConnection connection;
    private boolean isConnectionOpened;

    public URLConnection connection() {
        return this.connection;
    }

    private InputStream inputStreamLazyCompute() throws IOException {
        if (!this.isConnectionOpened) {
            this.inputStream = connection().getInputStream();
            this.isConnectionOpened = true;
        }
        return this.inputStream;
    }

    private InputStream inputStream() throws IOException {
        return !this.isConnectionOpened ? inputStreamLazyCompute() : this.inputStream;
    }

    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        try {
            String url = connection().getURL().toString();
            if (url.contains(".css")) {
                cefResponse.setMimeType("text/css");
            } else if (url.contains(".js")) {
                cefResponse.setMimeType("text/javascript");
            } else if (url.contains(".html")) {
                cefResponse.setMimeType("text/html");
            } else {
                cefResponse.setMimeType(connection().getContentType());
            }
            responseLength.set(inputStream().available());
            cefResponse.setStatus(200);
        } catch (IOException e) {
            Logger.getInstance(this.getClass()).error("getResponseHeaders error", e);
            cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FILE_NOT_FOUND);
            cefResponse.setStatusText(e.getLocalizedMessage());
            cefResponse.setStatus(404);
        }
    }

    @Override
    public Boolean readResponse(byte[] paramArrayOfByte, int designedBytesToRead, IntRef bytesRead, CefCallback callback) throws IOException {
        boolean isResponseRead;
        int availableSize = inputStream().available();
        if (availableSize > 0) {
            int maxBytesToRead = Math.min(availableSize, designedBytesToRead);
            int realNumberOfReadBytes = inputStream().read(paramArrayOfByte, 0, maxBytesToRead);
            bytesRead.set(realNumberOfReadBytes);
            isResponseRead = true;
        } else {
            inputStream().close();
            isResponseRead = false;
        }
        return isResponseRead;
    }

    @Override
    public void close() throws IOException {
        inputStream().close();
    }

    public OpenedConnection(URLConnection connection) {
        this.connection = connection;
    }
}



 
 
