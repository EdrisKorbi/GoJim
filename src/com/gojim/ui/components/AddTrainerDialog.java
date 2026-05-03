package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.*;

public class AddTrainerDialog extends JDialog {

    private JTextField nameField;
    private JTextField specialtyField;
    private JTextField experienceField;

    private boolean confirmed = false;

    public AddTrainerDialog(JFrame parent) {
        super(parent, "Add Trainer", true);

        setSize(420, 330);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 14));
        panel.setBackground(AppColors.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        nameField = new JTextField();
        specialtyField = new JTextField();
        experienceField = new JTextField();

        styleField(nameField);
        styleField(specialtyField);
        styleField(experienceField);

        panel.add(createLabel("Full Name"));
        panel.add(nameField);

        panel.add(createLabel("Specialty"));
        panel.add(specialtyField);

        panel.add(createLabel("Experience"));
        panel.add(experienceField);

        RoundedButton cancelBtn = new RoundedButton("Cancel", 16);
        RoundedButton saveBtn = new RoundedButton("Save", 16);

        getRootPane().setDefaultButton(saveBtn);

        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        cancelBtn.addActionListener(e -> dispose());

        saveBtn.addActionListener(e -> {
            if (!validateInput())
                return;

            confirmed = true;
            dispose();
        });

        panel.add(cancelBtn);
        panel.add(saveBtn);

        add(panel);
    }

    public AddTrainerDialog(JFrame parent, String name, String specialty, String experience) {
        this(parent);

        nameField.setText(name);
        specialtyField.setText(specialty);
        experienceField.setText(experience);
    }

    private boolean validateInput() {
        if (getNameValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trainer name is required.");
            return false;
        }

        if (getSpecialtyValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Specialty is required.");
            return false;
        }

        if (getExperienceValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Experience is required.");
            return false;
        }

        return true;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppColors.TEXT_WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleField(JTextField field) {
        field.setBackground(AppColors.CARD);
        field.setForeground(AppColors.TEXT_WHITE);
        field.setCaretColor(AppColors.TEXT_WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNameValue() {
        return nameField.getText().trim();
    }

    public String getSpecialtyValue() {
        return specialtyField.getText().trim();
    }

    public String getExperienceValue() {
        return experienceField.getText().trim();
    }
}