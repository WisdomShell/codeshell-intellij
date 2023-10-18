package com.codeshell.intellij.actions.complete;

import com.codeshell.intellij.widget.CodeShellWidget;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class CodeGenInsertAllAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Caret caret = e.getData(CommonDataKeys.CARET);
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (!performAction(editor, caret, file)) {
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (Objects.isNull(file)) {
            return;
        }

        String[] hints = file.getUserData(CodeShellWidget.SHELL_CODER_CODE_SUGGESTION);
        e.getPresentation().setEnabledAndVisible(Objects.nonNull(hints) && hints.length > 0);
    }

    public static boolean performAction(Editor editor, Caret caret, VirtualFile file) {
        if (Objects.isNull(file)) {
            return false;
        }

        String[] hints = file.getUserData(CodeShellWidget.SHELL_CODER_CODE_SUGGESTION);
        if (Objects.isNull(hints) || (hints.length == 0)) {
            return false;
        }

        Integer position = file.getUserData(CodeShellWidget.SHELL_CODER_POSITION);
        int lastPosition = Objects.isNull(position) ? 0 : position;
        if (Objects.isNull(caret) || (caret.getOffset() != lastPosition)) {
            return false;
        }

        StringJoiner insertTextJoiner = new StringJoiner("");
        Arrays.stream(hints).forEach(insertTextJoiner::add);
        file.putUserData(CodeShellWidget.SHELL_CODER_CODE_SUGGESTION, null);

        String insertText = insertTextJoiner.toString();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), "CodeShell Insert", null, () -> {
            editor.getDocument().insertString(lastPosition, insertText);
            editor.getCaretModel().moveToOffset(lastPosition + insertText.length());
        });
        return true;
    }
}
