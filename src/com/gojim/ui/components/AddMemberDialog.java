package com.gojim.ui.components;

import com.gojim.util.AppColors;

import com.gojim.dao.SubscriptionDAO;
import com.gojim.model.Subscription;

import java.util.List;

import javax.swing.*;
import java.awt.*;

public class AddMemberDialog extends JDialog {

    private JTextField nameField;
    private JTextField ageField;
    private JTextField phoneField;
    private RoundedComboBox planBox;
    private boolean confirmed = false;

    private List<Subscription> subscriptions;

    public AddMemberDialog(JFrame parent, String name, String age, String phone, String plan) {
        this(parent);

        nameField.setText(name);
        ageField.setText(age);
        phoneField.setText(phone.replace("+216 ", ""));
        planBox.setSelectedValue(plan);
    }

    public AddMemberDialog(JFrame parent) {
        super(parent, "Add Member", true);

        setSize(460, 430);
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
        JLabel title = new JLabel("Add New Member");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        return title;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 14));
        form.setOpaque(false);

        nameField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();
        planBox = new RoundedComboBox(loadPlansFromDB());

        form.add(createFieldRow("Full Name", nameField));
        form.add(createFieldRow("Age", ageField));
        form.add(createFieldRow("Phone", phoneField));
        form.add(createCustomRow("Plan", planBox));
        return form;
    }

    private JPanel createCustomRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel label = createLabel(labelText);

        component.setPreferredSize(new Dimension(220, 42));

        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);

        return row;
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
        label.setPreferredSize(new Dimension(100, 40));
        label.setForeground(AppColors.TEXT_WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(220, 42));

        field.setOpaque(false); // important for custom painting
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
                return; // keep dialog open
            }

            confirmed = true;
            dispose(); // close only when valid
        });

        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        return buttons;
    }

    private boolean validateInput() {
        String name = getNameValue();
        String ageText = getAgeValue();
        String phoneText = getPhoneValue();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name is required.");
            return false;
        }

        if (ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Age is required.");
            return false;
        }

        if (!ageText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Age must contain numbers only.");
            return false;
        }

        int age = Integer.parseInt(ageText);

        if (age <= 18 || age > 100) {
            JOptionPane.showMessageDialog(this, "Age must be over 18.");
            return false;
        }

        if (phoneText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number is required.");
            return false;
        }

        if (!phoneText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Phone number must contain numbers only.");
            return false;
        }

        if (phoneText.length() != 8) {
            JOptionPane.showMessageDialog(this, "Phone number must contain exactly 8 digits.");
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

    public String getAgeValue() {
        return ageField.getText().trim();
    }

    public String getPhoneValue() {
        return phoneField.getText().trim();
    }

    public String getPlanValue() {
        return planBox.getSelectedValue();
    }

    private String[] loadPlansFromDB() {
        SubscriptionDAO dao = new SubscriptionDAO();
        subscriptions = dao.getAllSubscriptions();

        String[] plans = new String[subscriptions.size()];

        for (int i = 0; i < subscriptions.size(); i++) {
            plans[i] = subscriptions.get(i).getPlanName();
        }

        return plans;
    }

    public int getSelectedSubscriptionId() {
        String selectedPlan = planBox.getSelectedValue();

        for (Subscription subscription : subscriptions) {
            if (subscription.getPlanName().equals(selectedPlan)) {
                return subscription.getId();
            }
        }

        return -1;
    }
}