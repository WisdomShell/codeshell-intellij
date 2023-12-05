package com.codeshell.intellij.actions.complete;

import com.codeshell.intellij.services.CodeShellCompleteService;
import com.codeshell.intellij.utils.CodeShellUtils;
import com.codeshell.intellij.utils.EditorUtils;
import com.codeshell.intellij.widget.CodeShellWidget;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CodeTriggerCompletionAction extends DumbAwareAction implements IntentionAction {

    @SafeFieldForPreview
    private Logger logger = Logger.getInstance(this.getClass());

    @Override
    @IntentionName
    @NotNull
    public String getText() {
        return "Trigger Completion";
    }

    @Override
    @NotNull
    @IntentionFamilyName
    public String getFamilyName() {
        return "CodeShell";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {

    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        updateInlayHints(editor);
//        Project project = e.getData(LangDataKeys.PROJECT);
//        if (Objects.isNull(project)) {
//            return;
//        }
//        ApplicationManager.getApplication().invokeLater(() -> {
//            VirtualFile vf = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
//            Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//            if (EditorUtils.isNoneTextSelected(editor)) {
//                return;
//            }
//            PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
//            JsonObject jsonObject = EditorUtils.getFileSelectionDetails(editor, psiFile, true, PrefixString.CLEAN_CODE);
//            JsonObject result = new JsonObject();
//            ToolWindowManager tool = ToolWindowManager.getInstance(project);
//            Objects.requireNonNull(tool.getToolWindow("CodeShell")).activate(() -> {
//                if(logger.isDebugEnabled()){
//                    logger.debug("******************* CleanCode Enabled CodeShell window *******************");
//                }
//            }, true, true);
//            jsonObject.addProperty("fileName", vf.getName());
//            jsonObject.addProperty("filePath", vf.getCanonicalPath());
//            result.addProperty("data", jsonObject.toString());
//            (project.getService(CodeShellSideWindowService.class)).notifyIdeAppInstance(result);
//        }, ModalityState.NON_MODAL);
    }

    private void updateInlayHints(Editor focusedEditor) {
        if (Objects.isNull(focusedEditor) || !EditorUtils.isMainEditor(focusedEditor)) {
            return;
        }
        VirtualFile file = FileDocumentManager.getInstance().getFile(focusedEditor.getDocument());
        if (Objects.isNull(file)) {
            return;
        }

        String selection = focusedEditor.getCaretModel().getCurrentCaret().getSelectedText();
        if (Objects.nonNull(selection) && !selection.isEmpty()) {
            String[] existingHints = file.getUserData(CodeShellWidget.SHELL_CODER_CODE_SUGGESTION);
            if (Objects.nonNull(existingHints) && existingHints.length > 0) {
                file.putUserData(CodeShellWidget.SHELL_CODER_CODE_SUGGESTION, null);
                file.putUserData(CodeShellWidget.SHELL_CODER_POSITION, focusedEditor.getCaretModel().getOffset());

                InlayModel inlayModel = focusedEditor.getInlayModel();
                inlayModel.getInlineElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
                inlayModel.getBlockElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
            }
            return;
        }

        Integer codeShellPos = file.getUserData(CodeShellWidget.SHELL_CODER_POSITION);
        int currentPosition = focusedEditor.getCaretModel().getOffset();

        InlayModel inlayModel = focusedEditor.getInlayModel();
        inlayModel.getInlineElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
        inlayModel.getBlockElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
        file.putUserData(CodeShellWidget.SHELL_CODER_POSITION, currentPosition);
        CodeShellCompleteService codeShell = ApplicationManager.getApplication().getService(CodeShellCompleteService.class);
        CharSequence editorContents = focusedEditor.getDocument().getCharsSequence();
        CompletableFuture<String[]> future = CompletableFuture.supplyAsync(() -> codeShell.getCodeCompletionHints(editorContents, currentPosition));
        future.thenAccept(hintList -> CodeShellUtils.addCodeSuggestion(focusedEditor, file, currentPosition, hintList));
    }
}
