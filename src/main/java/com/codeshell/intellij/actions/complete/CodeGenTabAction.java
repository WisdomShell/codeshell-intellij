package com.codeshell.intellij.actions.complete;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeGenTabAction extends EditorWriteActionHandler {
    protected final EditorActionHandler handler;

    public CodeGenTabAction(EditorActionHandler actionHandler) {
        handler = actionHandler;
    }

    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        if (!insertCodeSuggestion(editor, caret, dataContext)) {
            handler.execute(editor, caret, dataContext);
        }
    }

    private boolean insertCodeSuggestion(Editor editor, Caret caret, DataContext dataContext) {
        VirtualFile file = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        return CodeGenInsertAllAction.performAction(editor, caret, file);
    }
}
