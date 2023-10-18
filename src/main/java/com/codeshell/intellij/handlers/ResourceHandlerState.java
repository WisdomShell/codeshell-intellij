package com.codeshell.intellij.handlers;

import org.cef.callback.CefCallback;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;

import java.io.IOException;

public interface ResourceHandlerState {
    void getResponseHeaders(CefResponse paramCefResponse, IntRef paramIntRef, StringRef paramStringRef);

    Boolean readResponse(byte[] paramArrayOfByte, int paramInt, IntRef paramIntRef, CefCallback paramCefCallback) throws IOException;

    void close() throws IOException;
}



 
 
