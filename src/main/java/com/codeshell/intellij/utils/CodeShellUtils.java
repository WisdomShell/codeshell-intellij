package com.codeshell.intellij.utils;

import com.alibaba.fastjson2.JSONObject;
import com.codeshell.intellij.constant.PrefixString;
import com.codeshell.intellij.model.GenerateModel;
import com.codeshell.intellij.settings.CodeShellSettings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeShellUtils {
    public static String includePreText(String preText, String language, String text) {
        String sufText = "\n```" + language + "\n" + text + "\n```\n";
        return String.format(preText, language, sufText);
    }
    public static int prefixHandle(int begin, int end) {
        if (end - begin > 3000) {
            return end - 3000;
        } else {
            return begin;
        }
    }
    public static int suffixHandle(int begin, int end) {
        if (end - begin > 256) {
            return begin + 256;
        } else {
            return end;
        }
    }

    public static JsonObject pakgHttpRequestBodyForCPU(CodeShellSettings settings, String prefix, String suffix){
        JsonObject httpBody = new JsonObject();
        httpBody.addProperty("input_prefix", prefix);
        httpBody.addProperty("input_suffix", suffix);
        httpBody.addProperty("n_predict", Integer.parseInt(settings.getCompletionMaxToken().getDescription()));
        httpBody.addProperty("temperature", 0.2);
        httpBody.addProperty("repetition_penalty", 1.0);
        httpBody.addProperty("top_k", 10);
        httpBody.addProperty("top_p", 0.95);
//        httpBody.addProperty("prompt", PrefixString.REQUST_END_TAG + codeShellPrompt);
//        httpBody.addProperty("frequency_penalty", 1.2);
//        httpBody.addProperty("n_predict", Integer.parseInt(settings.getCompletionMaxToken().getDescription()));
//        httpBody.addProperty("stream", false);
//        JsonArray stopArray = new JsonArray();
//        stopArray.add("|<end>|");
//        httpBody.add("stop", stopArray);
        return httpBody;
    }

    public static JsonObject pakgHttpRequestBodyForGPU(CodeShellSettings settings, String codeShellPrompt){
        JsonObject httpBody = new JsonObject();
        httpBody.addProperty("inputs", codeShellPrompt);
        JsonObject parameters = new JsonObject();
        parameters.addProperty("max_new_tokens", Integer.parseInt(settings.getCompletionMaxToken().getDescription()));
        httpBody.add("parameters", parameters);
        return httpBody;
    }

    public static String parseHttpResponseContentForCPU(CodeShellSettings settings, String responseBody, Pattern pattern){
        String generatedText = "";
        Matcher matcher = pattern.matcher(responseBody);
        StringBuilder contentBuilder = new StringBuilder();
        while (matcher.find()) {
            String jsonString = matcher.group(1);
            JSONObject json = JSONObject.parseObject(jsonString);
            String content = json.getString("content");
            if(StringUtils.equalsAny(content, "<|endoftext|>", "")){
                continue;
            }
            contentBuilder.append(content);
        }
        String responseText = contentBuilder.toString().replace(PrefixString.RESPONSE_END_TAG, "");
        return responseText;
    }

    public static String parseHttpResponseContentForGPU(CodeShellSettings settings, String responseBody){
        String generatedText = "";
        Gson gson = new Gson();
        GenerateModel generateModel = gson.fromJson(responseBody, GenerateModel.class);
        if (StringUtils.isNotBlank(generateModel.getGenerated_text())) {
            generatedText = generateModel.getGenerated_text();
        }
        String responseText = generatedText.replace(PrefixString.RESPONSE_END_TAG, "");
        return responseText;
    }

    public static String getIDEAVersion(String whichVersion) {
        ApplicationInfo applicationInfo = ApplicationInfo.getInstance();
        String version = "";
        try {
            if (whichVersion.equalsIgnoreCase("major")) {
                version = applicationInfo.getMajorVersion();
            } else {
                version = applicationInfo.getFullVersion();
            }
        } catch (Exception e) {
            System.out.println("Error getting IDE full version" + e);
        }
        return version;
    }

}
