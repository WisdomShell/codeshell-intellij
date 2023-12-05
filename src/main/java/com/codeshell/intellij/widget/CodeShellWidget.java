package com.codeshell.intellij.widget;

import com.codeshell.intellij.enums.CodeShellStatus;
import com.codeshell.intellij.services.CodeShellCompleteService;
import com.codeshell.intellij.settings.CodeShellSettings;
import com.codeshell.intellij.utils.CodeGenHintRenderer;
import com.codeshell.intellij.utils.CodeShellIcons;
import com.codeshell.intellij.utils.CodeShellUtils;
import com.codeshell.intellij.utils.EditorUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CodeShellWidget extends EditorBasedWidget
        implements StatusBarWidget.Multiframe, StatusBarWidget.IconPresentation,
        CaretListener, SelectionListener, BulkAwareDocumentListener.Simple, PropertyChangeListener {
    public static final String ID = "CodeShellWidget";

    public static final Key<String[]> SHELL_CODER_CODE_SUGGESTION = new Key<>("CodeShell Code Suggestion");
    public static final Key<Integer> SHELL_CODER_POSITION = new Key<>("CodeShell Position");
    public static boolean enableSuggestion = false;
    protected CodeShellWidget(@NotNull Project project) {
        super(project);
    }

    @Override
    public @NonNls @NotNull String ID() {
        return ID;
    }

    @Override
    public StatusBarWidget copy() {
        return new CodeShellWidget(getProject());
    }

    @Override
    public @Nullable Icon getIcon() {
        CodeShellCompleteService codeShell = ApplicationManager.getApplication().getService(CodeShellCompleteService.class);
        CodeShellStatus status = CodeShellStatus.getStatusByCode(codeShell.getStatus());
        if (status == CodeShellStatus.OK) {
            return CodeShellSettings.getInstance().isSaytEnabled() ? CodeShellIcons.WidgetEnabled : CodeShellIcons.WidgetDisabled;
        } else {
            return CodeShellIcons.WidgetError;
        }
    }

    @Override
    public WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public @Nullable @NlsContexts.Tooltip String getTooltipText() {
        StringBuilder toolTipText = new StringBuilder("CodeShell");
        if (CodeShellSettings.getInstance().isSaytEnabled()) {
            toolTipText.append(" enabled");
        } else {
            toolTipText.append(" disabled");
        }

        CodeShellCompleteService codeShell = ApplicationManager.getApplication().getService(CodeShellCompleteService.class);
        int statusCode = codeShell.getStatus();
        CodeShellStatus status = CodeShellStatus.getStatusByCode(statusCode);
        switch (status) {
            case OK:
                if (CodeShellSettings.getInstance().isSaytEnabled()) {
                    toolTipText.append(" (Click to disable)");
                } else {
                    toolTipText.append(" (Click to enable)");
                }
                break;
            case UNKNOWN:
                toolTipText.append(" (http error ");
                toolTipText.append(statusCode);
                toolTipText.append(")");
                break;
            default:
                toolTipText.append(" (");
                toolTipText.append(status.getDisplayValue());
                toolTipText.append(")");
        }

        return toolTipText.toString();
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return mouseEvent -> {
            CodeShellSettings.getInstance().toggleSaytEnabled();
            if (Objects.nonNull(myStatusBar)) {
                myStatusBar.updateWidget(ID);
            }
        };
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        super.install(statusBar);
        EditorEventMulticaster multicaster = EditorFactory.getInstance().getEventMulticaster();
        multicaster.addCaretListener(this, this);
        multicaster.addSelectionListener(this, this);
        multicaster.addDocumentListener(this, this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", this);
        Disposer.register(this,
                () -> KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("focusOwner",
                        this)
        );
    }

    private Editor getFocusOwnerEditor() {
        Component component = getFocusOwnerComponent();
        Editor editor = component instanceof EditorComponentImpl ? ((EditorComponentImpl) component).getEditor() : getEditor();
        return Objects.nonNull(editor) && !editor.isDisposed() && EditorUtils.isMainEditor(editor) ? editor : null;
    }

    private Component getFocusOwnerComponent() {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (Objects.isNull(focusOwner)) {
            IdeFocusManager focusManager = IdeFocusManager.getInstance(getProject());
            Window frame = focusManager.getLastFocusedIdeWindow();
            if (Objects.nonNull(frame)) {
                focusOwner = focusManager.getLastFocusedFor(frame);
            }
        }
        return focusOwner;
    }

    private boolean isFocusedEditor(Editor editor) {
        Component focusOwner = getFocusOwnerComponent();
        return focusOwner == editor.getContentComponent();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateInlayHints(getFocusOwnerEditor());
    }

    @Override
    public void selectionChanged(SelectionEvent event) {
        updateInlayHints(event.getEditor());
    }

    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
        updateInlayHints(event.getEditor());
    }

    @Override
    public void caretAdded(@NotNull CaretEvent event) {
        updateInlayHints(event.getEditor());
    }

    @Override
    public void caretRemoved(@NotNull CaretEvent event) {
        updateInlayHints(event.getEditor());
    }

    @Override
    public void afterDocumentChange(@NotNull Document document) {
        enableSuggestion = true;
        if (ApplicationManager.getApplication().isDispatchThread()) {
            EditorFactory.getInstance().editors(document)
                    .filter(this::isFocusedEditor)
                    .findFirst()
                    .ifPresent(this::updateInlayHints);
        }
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
            String[] existingHints = file.getUserData(SHELL_CODER_CODE_SUGGESTION);
            if (Objects.nonNull(existingHints) && existingHints.length > 0) {
                file.putUserData(SHELL_CODER_CODE_SUGGESTION, null);
                file.putUserData(SHELL_CODER_POSITION, focusedEditor.getCaretModel().getOffset());

                InlayModel inlayModel = focusedEditor.getInlayModel();
                inlayModel.getInlineElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
                inlayModel.getBlockElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
            }
            return;
        }

        Integer codeShellPos = file.getUserData(SHELL_CODER_POSITION);
        int lastPosition = (Objects.isNull(codeShellPos)) ? 0 : codeShellPos;
        int currentPosition = focusedEditor.getCaretModel().getOffset();

        if (lastPosition == currentPosition) return;

        InlayModel inlayModel = focusedEditor.getInlayModel();
        if (currentPosition > lastPosition) {
            String[] existingHints = file.getUserData(SHELL_CODER_CODE_SUGGESTION);
            if (Objects.nonNull(existingHints) && existingHints.length > 0) {
                String inlineHint = existingHints[0];
                String modifiedText = focusedEditor.getDocument().getCharsSequence().subSequence(lastPosition, currentPosition).toString();
                if (modifiedText.startsWith("\n")) {
                    modifiedText = modifiedText.replace(" ", "");
                }
                if (inlineHint.startsWith(modifiedText)) {
                    inlineHint = inlineHint.substring(modifiedText.length());
                    enableSuggestion = false;
                    if (inlineHint.length() > 0) {
                        inlayModel.getInlineElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
                        inlayModel.addInlineElement(currentPosition, true, new CodeGenHintRenderer(inlineHint));
                        existingHints[0] = inlineHint;

                        file.putUserData(SHELL_CODER_CODE_SUGGESTION, existingHints);
                        file.putUserData(SHELL_CODER_POSITION, currentPosition);
                        return;
                    } else if (existingHints.length > 1) {
                        existingHints = Arrays.copyOfRange(existingHints, 1, existingHints.length);
                        CodeShellUtils.addCodeSuggestion(focusedEditor, file, currentPosition, existingHints);
                        return;
                    } else {
                        file.putUserData(SHELL_CODER_CODE_SUGGESTION, null);
                    }
                }
            }
        }

        inlayModel.getInlineElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);
        inlayModel.getBlockElementsInRange(0, focusedEditor.getDocument().getTextLength()).forEach(CodeShellUtils::disposeInlayHints);

        file.putUserData(SHELL_CODER_POSITION, currentPosition);
        if(!enableSuggestion || currentPosition < lastPosition){
            enableSuggestion = false;
            return;
        }
        CodeShellCompleteService codeShell = ApplicationManager.getApplication().getService(CodeShellCompleteService.class);
        CharSequence editorContents = focusedEditor.getDocument().getCharsSequence();
        CompletableFuture<String[]> future = CompletableFuture.supplyAsync(() -> codeShell.getCodeCompletionHints(editorContents, currentPosition));
        future.thenAccept(hintList -> CodeShellUtils.addCodeSuggestion(focusedEditor, file, currentPosition, hintList));
    }

}
