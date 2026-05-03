package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.*;

public class AddPlanDialog extends JDialog {

    private JTextField nameField;
    private JTextField durationField;
    private JTextField priceField;

    private boolean confirmed = false;

    public AddPlanDialog(JFrame parent) {
        super(parent, "Add Plan", true);

        setSize(460, 380);
        setLocationRelativeTo(parent);
        setResizable(false);

        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(AppColors.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 28, 25, 28));

        mainPanel.add(createTitle(), BorderLayout.NORTH);
        mainPanel.add(createForm(), BorderLayout.CENTER);
        mainPanel.add(createButtons(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Add New Plan");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        return title;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(3, 1, 0, 16));
        form.setOpaque(false);

        nameField = new JTextField();
        durationField = new JTextField();
        priceField = new JTextField();

        form.add(createFieldRow("Plan Name", nameField));
        form.add(createFieldRow("Duration", durationField));
        form.add(createFieldRow("Price", priceField));

        return form;
    }

    private JPanel createFieldRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel label = createLabel(labelText);
        styleTextField(field);

        RoundedPanel inputWrapper = new RoundedPanel(18, AppColors.CARD);
        inputWrapper.setLayout(new BorderLayout());
        inputWrapper.add(field, BorderLayout.CENTER);

        row.add(label, BorderLayout.WEST);
        row.add(inputWrapper, BorderLayout.CENTER);

        return row;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(100, 42));
        label.setForeground(AppColors.TEXT_WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(220, 42));
        field.setForeground(AppColors.TEXT_WHITE);
        field.setCaretColor(AppColors.TEXT_WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    private JPanel createButtons() {
        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);

        RoundedButton cancelBtn = new RoundedButton("Cancel", 16);
        RoundedButton saveBtn = new RoundedButton("Save", 16);

        getRootPane().setDefaultButton(saveBtn);

        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        cancelBtn.addActionListener(e -> dispose());

        saveBtn.addActionListener(e -> {
            if (!validateInput()) {
                return;
            }

            confirmed = true;
            dispose();
        });

        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        return buttons;
    }

    public AddPlanDialog(JFrame parent, String name, String duration, String price) {
        this(parent);

        nameField.setText(name);
        durationField.setText(duration.replace(" days", ""));
        priceField.setText(price.replace(" DT", ""));
    }

    private boolean validateInput() {
        if (getNameValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plan name is required.");
            return false;
        }

        if (getDurationValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Duration is required.");
            return false;
        }

        if (!getDurationValue().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Duration must contain numbers only.");
            return false;
        }

        if (getPriceValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Price is required.");
            return false;
        }

        if (!getPriceValue().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Price must contain numbers only.");
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNameValue() {
        return nameField.getText().trim();
    }

    public String getDurationValue() {
        return durationField.getText().trim();
    }

    public String getPriceValue() {
        return priceField.getText().trim();
    }
}