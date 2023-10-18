package com.codeshell.intellij.services;

import com.codeshell.intellij.constant.PrefixString;
import com.codeshell.intellij.settings.CodeShellSettings;
import com.codeshell.intellij.utils.CodeShellUtils;
import com.codeshell.intellij.widget.CodeShellWidget;
import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CodeShellCompleteService {

    private static final String PREFIX_TAG = "<fim_prefix>";
    private static final String SUFFIX_TAG = "<fim_suffix>";
    private static final String MIDDLE_TAG = "<fim_middle>";
    private static long lastRequestTime = 0;
    private static boolean httpRequestFinFlag = true;
    private int statusCode = 200;

    public String[] getCodeCompletionHints(CharSequence editorContents, int cursorPosition) {
        CodeShellSettings settings = CodeShellSettings.getInstance();
        String contents = editorContents.toString();
        if (!httpRequestFinFlag || !settings.isSaytEnabled() || contents.length() == 0) {
            return null;
        }

        if (contents.contains(PREFIX_TAG) || contents.contains(SUFFIX_TAG) || contents.contains(MIDDLE_TAG) || contents.contains(PrefixString.RESPONSE_END_TAG)) {
            return null;
        }

        String prefix = contents.substring(CodeShellUtils.prefixHandle(0, cursorPosition), cursorPosition);
        String suffix = contents.substring(cursorPosition, CodeShellUtils.suffixHandle(cursorPosition, editorContents.length()));
        String codeShellPrompt = generateFIMPrompt(prefix, suffix);
        HttpPost httpPost = buildApiPost(settings, codeShellPrompt);
        String generatedText = getApiResponse(settings, httpPost, codeShellPrompt);

        String[] suggestionList = null;
        if (generatedText.contains(MIDDLE_TAG)) {
            String[] parts = generatedText.split(MIDDLE_TAG);
            if (parts.length > 0) {
                suggestionList = StringUtils.splitPreserveAllTokens(parts[1], "\n");
                if (suggestionList.length == 1 && suggestionList[0].trim().isEmpty()) {
                    return null;
                }
                if (suggestionList.length > 1) {
                    for (int i = 0; i < suggestionList.length; i++) {
                        StringBuilder sb = new StringBuilder(suggestionList[i]);
                        sb.append("\n");
                        suggestionList[i] = sb.toString();
                    }
                }
            }
        }
        return suggestionList;
    }

    private String generateFIMPrompt(String prefix, String suffix) {
        return SUFFIX_TAG + suffix + PREFIX_TAG + prefix + MIDDLE_TAG;
    }

    private HttpPost buildApiPost(CodeShellSettings settings, String codeShellPrompt) {
        String apiURL = CodeShellUtils.selectUrl(settings);
        HttpPost httpPost = new HttpPost(apiURL);
        JsonObject httpBody = CodeShellUtils.encapsulateHttpRequestBody(settings, codeShellPrompt);
        StringEntity requestEntity = new StringEntity(httpBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);
        return httpPost;
    }

    private String getApiResponse(CodeShellSettings settings, HttpPost httpPost, String codeShellPrompt) {
        String responseText = "";
        httpRequestFinFlag = false;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(httpPost);
            int oldStatusCode = statusCode;
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != oldStatusCode) {
                for (Project openProject : ProjectManager.getInstance().getOpenProjects()) {
                    WindowManager.getInstance().getStatusBar(openProject).updateWidget(CodeShellWidget.ID);
                }
            }
            if (statusCode != 200) {
                return responseText;
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            responseText = CodeShellUtils.parseHttpResponseContent(settings, responseBody);
            httpClient.close();
        } catch (IOException e) {
        } finally {
            httpRequestFinFlag = true;
        }

        return codeShellPrompt + responseText;
    }

    public int getStatus() {
        return statusCode;
    }


}
