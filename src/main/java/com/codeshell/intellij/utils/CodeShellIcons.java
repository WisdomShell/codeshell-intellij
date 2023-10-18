package com.codeshell.intellij.utils;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface CodeShellIcons {
    Icon Action = IconLoader.getIcon("/icons/actionIcon.svg", CodeShellIcons.class);
    Icon ActionDark = IconLoader.getIcon("/icons/actionIcon_dark.svg", CodeShellIcons.class);
    Icon WidgetEnabled = IconLoader.getIcon("/icons/widgetEnabled.svg", CodeShellIcons.class);
    Icon WidgetEnabledDark = IconLoader.getIcon("/icons/widgetEnabled_dark.svg", CodeShellIcons.class);
    Icon WidgetDisabled = IconLoader.getIcon("/icons/widgetDisabled.svg", CodeShellIcons.class);
    Icon WidgetDisabledDark = IconLoader.getIcon("/icons/widgetDisabled_dark.svg", CodeShellIcons.class);
    Icon WidgetError = IconLoader.getIcon("/icons/widgetError.svg", CodeShellIcons.class);
    Icon WidgetErrorDark = IconLoader.getIcon("/icons/widgetError_dark.svg", CodeShellIcons.class);
}
