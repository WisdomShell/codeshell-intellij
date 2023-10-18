package com.codeshell.intellij.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CodeGenHintRenderer implements EditorCustomElementRenderer {
    private final String myText;
    private Font myFont;

    public CodeGenHintRenderer(String text, Font font) {
        myText = text;
        myFont = font;
    }

    public CodeGenHintRenderer(String text) {
        myText = text;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        Editor editor = inlay.getEditor();
        Font font = editor.getColorsScheme().getFont(EditorFontType.PLAIN);
        myFont = new Font(font.getName(), Font.ITALIC, font.getSize());
        return editor.getContentComponent().getFontMetrics(font).stringWidth(myText);
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        g.setColor(JBColor.GRAY);
        g.setFont(myFont);
        g.drawString(myText, targetRegion.x, targetRegion.y + targetRegion.height - (int) Math.ceil((double) g.getFontMetrics().getFont().getSize() / 2) + 1);
    }

}
