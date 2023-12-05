package com.codeshell.intellij.actions.complete;

import com.codeshell.intellij.utils.CodeGenHintRenderer;
import com.codeshell.intellij.widget.CodeShellWidget;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CodeGenEscAction extends EditorWriteActionHandler {
    protected final EditorActionHandler handler;

    public CodeGenEscAction(EditorActionHandler actionHandler) {
        handler = actionHandler;
    }

    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        cancelCodeCompletion(editor, caret, dataContext);
    }

    private void cancelCodeCompletion(Editor editor, Caret caret, DataContext dataContext) {
        VirtualFile file = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        if (Objects.isNull(file)) {
            return;
        }
        InlayModel inlayModel = editor.getInlayModel();
        inlayModel.getInlineElementsInRange(0, editor.getDocument().getTextLength()).forEach(this::disposeInlayHints);
        inlayModel.getBlockElementsInRange(0, editor.getDocument().getTextLength()).forEach(this::disposeInlayHints);
        CodeShellWidget.enableSuggestion = false;
    }

    private void disposeInlayHints(Inlay<?> inlay) {
        if (inlay.getRenderer() instanceof CodeGenHintRenderer) {
            inlay.dispose();
        }
    }
}
