package com.codeshell.intellij.utils;

import com.codeshell.intellij.enums.CodeShellURI;
import com.codeshell.intellij.settings.CodeShellSettings;
import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EditorUtils {

    public static JsonObject getFileSelectionDetails(Editor editor, @NotNull PsiFile psiFile, String preText) {
        return getFileSelectionDetails(editor, psiFile, false, preText);
    }

    public static boolean isNoneTextSelected(Editor editor) {
        return Objects.isNull(editor) || StringUtils.isEmpty(editor.getCaretModel().getCurrentCaret().getSelectedText());
    }

    public static JsonObject getFileSelectionDetails(Editor editor, @NotNull PsiFile psiFile, boolean isCodeNote, String preText) {
        if (Objects.isNull(editor)) {
            return getEmptyRange();
        } else {
            Document document = editor.getDocument();
            int startOffset;
            int endOffset;
            String editorText;

            if (editor.getSelectionModel().hasSelection()) {
                startOffset = editor.getSelectionModel().getSelectionStart();
                endOffset = editor.getSelectionModel().getSelectionEnd();
                editorText = editor.getSelectionModel().getSelectedText();
            } else {
                LogicalPosition logicalPos = editor.getCaretModel().getCurrentCaret().getLogicalPosition();
                int line = logicalPos.line;
                startOffset = editor.logicalPositionToOffset(new LogicalPosition(line, 0));
                endOffset = editor.logicalPositionToOffset(new LogicalPosition(line, Integer.MAX_VALUE));
                editorText = lspPosition(document, endOffset).get("text").toString();
                if (isCodeNote) {
                    editor.getDocument();
                    for (int linenumber = 0; linenumber < editor.getDocument().getLineCount(); ++linenumber) {
                        startOffset = editor.logicalPositionToOffset(new LogicalPosition(linenumber, 0));
                        endOffset = editor.logicalPositionToOffset(new LogicalPosition(linenumber, Integer.MAX_VALUE));
                        String tempText = lspPosition(document, endOffset).get("text").toString();
                        if (Objects.nonNull(tempText) && !"\"\"".equals(tempText.trim()) && !tempText.trim().isEmpty()) {
                            editorText = tempText;
                            break;
                        }
                    }
                }
            }
            JsonObject range = new JsonObject();
            range.add("start", lspPosition(document, startOffset));
            range.add("end", lspPosition(document, endOffset));
            range.addProperty("selectedText", includePreText(preText, psiFile.getLanguage().getID(), editorText));
            JsonObject sendText = new JsonObject();
            sendText.addProperty("inputs", includePreText(preText, psiFile.getLanguage().getID(), editorText));
            JsonObject parameters = new JsonObject();
            parameters.addProperty("max_new_tokens", CodeShellSettings.getInstance().getChatMaxToken().getDescription());
            sendText.add("parameters", parameters);

            JsonObject returnObj = new JsonObject();
            returnObj.add("range", range);
            if(CodeShellSettings.getInstance().isCPURadioButtonEnabled()){
                returnObj.addProperty("sendUrl", CodeShellSettings.getInstance().getServerAddressURL() + CodeShellURI.CPU_CHAT.getUri());
                returnObj.addProperty("modelType", "CPU");
            }else{
                returnObj.addProperty("sendUrl", CodeShellSettings.getInstance().getServerAddressURL() + CodeShellURI.GPU_CHAT.getUri());
                returnObj.addProperty("modelType", "GPU");
            }
            returnObj.addProperty("maxToken", CodeShellSettings.getInstance().getChatMaxToken().getDescription());
            returnObj.add("sendText", sendText);
            return returnObj;
        }
    }

    public static JsonObject lspPosition(Document document, int offset) {
        int line = document.getLineNumber(offset);
        int lineStart = document.getLineStartOffset(line);
        String lineTextBeforeOffset = document.getText(TextRange.create(lineStart, offset));
        int column = lineTextBeforeOffset.length();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("line", line);
        jsonObject.addProperty("column", column);
        jsonObject.addProperty("text", lineTextBeforeOffset);
        return jsonObject;
    }

    public static JsonObject getEmptyRange() {
        JsonObject jsonObject = new JsonObject();
        JsonObject range = new JsonObject();
        JsonObject lineCol = new JsonObject();
        lineCol.addProperty("line", "0");
        lineCol.addProperty("column", "0");
        lineCol.addProperty("text", "0");
        range.add("start", lineCol);
        range.add("end", lineCol);
        jsonObject.add("range", range);
        jsonObject.addProperty("selectedText", "");
        return jsonObject;
    }

    private static String includePreText(String preText, String language, String text) {
        String sufText = "\n```" + language + "\n" + text + "\n```\n";
        return String.format(preText, language, sufText);
    }

    public static boolean isMainEditor(Editor editor) {
        return editor.getEditorKind() == EditorKind.MAIN_EDITOR || ApplicationManager.getApplication().isUnitTestMode();
    }

}
