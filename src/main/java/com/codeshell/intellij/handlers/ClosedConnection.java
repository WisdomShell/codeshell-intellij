package com.codeshell.intellij.handlers;

import org.cef.callback.CefCallback;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;

import java.io.IOException;

public class ClosedConnection implements ResourceHandlerState {
    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        cefResponse.setStatus(404);
    }

    @Override
    public Boolean readResponse(byte[] paramArrayOfByte, int designedBytesToRead, IntRef bytesRead, CefCallback callback) throws IOException {
        return Boolean.FALSE;
    }

    @Override
    public void close() throws IOException {
    }
}



 
 
