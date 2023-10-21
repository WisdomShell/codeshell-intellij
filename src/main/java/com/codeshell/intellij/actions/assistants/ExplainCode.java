package com.codeshell.intellij.actions.assistants;

import com.codeshell.intellij.constant.PrefixString;
import com.codeshell.intellij.services.CodeShellSideWindowService;
import com.codeshell.intellij.utils.EditorUtils;
import com.google.gson.JsonObject;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExplainCode extends DumbAwareAction implements IntentionAction {

    @SafeFieldForPreview
    private Logger logger = Logger.getInstance(this.getClass());

    @Override
    @IntentionName
    @NotNull
    public String getText() {
        return "Explain Code";
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
        Project project = e.getData(LangDataKeys.PROJECT);
        if (Objects.isNull(project)) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            VirtualFile vf = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
            Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
            if (EditorUtils.isNoneTextSelected(editor)) {
                return;
            }
            PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
            JsonObject jsonObject = EditorUtils.getFileSelectionDetails(editor, psiFile, true, PrefixString.EXPLAIN_CODE);
            JsonObject result = new JsonObject();
            ToolWindowManager tool = ToolWindowManager.getInstance(project);
            Objects.requireNonNull(tool.getToolWindow("CodeShell")).activate(()-> {
                if (logger.isDebugEnabled()) {
                    logger.debug("******************* ExplainCode Enabled CodeShell window *******************");
                }
            } , true, true);
            jsonObject.addProperty("fileName", vf.getName());
            jsonObject.addProperty("filePath", vf.getCanonicalPath());
            result.addProperty("data", jsonObject.toString());
            (project.getService(CodeShellSideWindowService.class)).notifyIdeAppInstance(result);
        }, ModalityState.NON_MODAL);
    }
}
