package com.codeshell.intellij.utils;

import com.codeshell.intellij.constant.PrefixString;
import com.codeshell.intellij.settings.CodeShellSettings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CodeShellUtils {
    public static String includePreText(String preText, String language, String text) {
        String sufText = "\n```" + language + "\n" + text + "\n```\n";
        return String.format(preText, language, sufText);
    }
    public static int prefixHandle(int begin, int end) {
        if (end - begin > 480) {
            return end - 480;
        } else {
            return begin;
        }
    }
    public static int suffixHandle(int begin, int end) {
        if (end - begin > 480) {
            return begin + 480;
        } else {
            return end;
        }
    }

    public static String selectUrl(CodeShellSettings settings){
        String url = "";
        url = settings.getCompleteURL();
        return url;
    }

    public static JsonObject encapsulateHttpRequestBody(CodeShellSettings settings, String codeShellPrompt){
        JsonObject httpBody = new JsonObject();
        httpBody.addProperty("prompt", PrefixString.REQUST_END_TAG + codeShellPrompt);
        httpBody.addProperty("frequency_penalty", 1.2);
        httpBody.addProperty("n_predict", Integer.parseInt(settings.getCompletionMaxToken().getDescription()));
        httpBody.addProperty("stream", false);
        JsonArray stopArray = new JsonArray();
        stopArray.add("|<end>|");
        httpBody.add("stop", stopArray);
        return httpBody;
    }

    public static String parseHttpResponseContent(CodeShellSettings settings, String responseBody){
        String responseText = "";
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        responseText = jsonObject.get("content").getAsString();
        return responseText;
    }

}
