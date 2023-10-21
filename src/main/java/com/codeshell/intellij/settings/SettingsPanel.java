package com.codeshell.intellij.settings;

import com.codeshell.intellij.enums.ChatMaxToken;
import com.codeshell.intellij.enums.CompletionMaxToken;
import com.codeshell.intellij.enums.TabActionOption;
import com.intellij.ui.EnumComboBoxModel;

import javax.swing.*;

public class SettingsPanel {

    private JPanel panel;
    private JTextField serverAddressTextField;

    private JPanel modelRuntime;
    private JPanel Parameters;
    private JCheckBox enableSAYTCheckBox;
    private JPanel Settings;
    private JPanel ParamOuter;
    private JPanel TabActionPanel;
    private JComboBox<TabActionOption> tabActionComboBox;
    private JLabel tabActionLabel;
    private JComboBox<CompletionMaxToken> completionMaxTokensComboBox;
    private JComboBox<ChatMaxToken> chatMaxTokensComboBox;
    private JRadioButton useGPUModelRadioButton;
    private JRadioButton useCPUModelRadioButton;

    public SettingsPanel() {

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(useGPUModelRadioButton);
        buttonGroup.add(useCPUModelRadioButton);

        tabActionComboBox.setModel(new EnumComboBoxModel<>(TabActionOption.class));
        enableSAYTCheckBox.addActionListener(e -> {
            tabActionLabel.setEnabled(enableSAYTCheckBox.isSelected());
            tabActionComboBox.setEnabled(enableSAYTCheckBox.isSelected());
        });

        completionMaxTokensComboBox.setModel(new EnumComboBoxModel<>(CompletionMaxToken.class));
        chatMaxTokensComboBox.setModel(new EnumComboBoxModel<>(ChatMaxToken.class));

    }

    public JComponent getPanel() {
        return panel;
    }

    public boolean getCPUModelRadioButton() {
        return useCPUModelRadioButton.isSelected();
    }

    public void setCPUModelRadioButton(boolean enabledCPURadioButton) {
        useCPUModelRadioButton.setSelected(enabledCPURadioButton);
    }

    public boolean getGPUModelRadioButton() {
        return useGPUModelRadioButton.isSelected();
    }

    public void setGPUModelRadioButton(boolean enabledGPURadioButton) {
        useGPUModelRadioButton.setSelected(enabledGPURadioButton);
    }

    public String getServerAddressUrl() {
        return serverAddressTextField.getText();
    }

    public void setServerAddressUrl(String serverAddress) {
        serverAddressTextField.setText(serverAddress);
    }

    public boolean getEnableSAYTCheckBox() {
        return enableSAYTCheckBox.isSelected();
    }

    public void setEnableSAYTCheckBox(boolean enableSAYT) {
        enableSAYTCheckBox.setSelected(enableSAYT);
    }

    public TabActionOption getTabActionOption() {
        return (TabActionOption) tabActionComboBox.getModel().getSelectedItem();
    }

    public void setTabActionOption(TabActionOption option) {
        tabActionComboBox.getModel().setSelectedItem(option);
    }

    public CompletionMaxToken getCompletionMaxTokens() {
        return (CompletionMaxToken) completionMaxTokensComboBox.getModel().getSelectedItem();
    }

    public void setCompletionMaxTokens(CompletionMaxToken option) {
        completionMaxTokensComboBox.getModel().setSelectedItem(option);
    }
    public ChatMaxToken getChatMaxTokens() {
        return (ChatMaxToken) chatMaxTokensComboBox.getModel().getSelectedItem();
    }

    public void setChatMaxTokens(ChatMaxToken option) {
        chatMaxTokensComboBox.getModel().setSelectedItem(option);
    }
}
