package com.gojim.ui.components;

import com.gojim.util.AppColors;

import com.gojim.dao.MemberDAO;
import com.gojim.model.Member;

import java.util.List;

import javax.swing.*;
import java.awt.*;

public class AddPaymentDialog extends JDialog {

    private RoundedComboBox memberBox;
    private JTextField amountField;
    private RoundedComboBox statusBox;
    private List<Member> members;

    private boolean confirmed = false;

    public AddPaymentDialog(JFrame parent) {
        super(parent, "Add Payment", true);

        setSize(420, 330);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 14));
        panel.setBackground(AppColors.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        memberBox = new RoundedComboBox(loadMembersFromDB());
        amountField = new JTextField();
        statusBox = new RoundedComboBox(new String[] { "Paid", "Pending" });

        styleField(amountField);

        panel.add(createLabel("Member"));
        panel.add(memberBox);

        panel.add(createLabel("Amount"));
        panel.add(amountField);

        panel.add(createLabel("Status"));
        panel.add(statusBox);

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

    private boolean validateInput() {

        // ✅ Check members exist
        if (members == null || members.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members available. Please add a member first.");
            return false;
        }

        // ✅ Check selected member
        if (getSelectedMemberId() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a valid member.");
            return false;
        }

        // ✅ Amount required
        if (getAmountValue().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required.");
            return false;
        }

        // ✅ Amount must be numeric
        if (!getAmountValue().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Amount must contain numbers only.");
            return false;
        }

        // ✅ Optional: realistic amount check
        double amount = Double.parseDouble(getAmountValue());

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
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

    public String getMemberValue() {
        return memberBox.getSelectedValue();
    }

    public int getSelectedMemberId() {
        String selectedName = memberBox.getSelectedValue();

        for (Member member : members) {
            if (member.getFullName().equals(selectedName)) {
                return member.getId();
            }
        }

        return -1;
    }

    public String getAmountValue() {
        return amountField.getText().trim();
    }

    public String getStatusValue() {
        return statusBox.getSelectedValue();
    }

    private String[] loadMembersFromDB() {
        MemberDAO dao = new MemberDAO();
        members = dao.getAllMembers();

        String[] names = new String[members.size()];

        for (int i = 0; i < members.size(); i++) {
            names[i] = members.get(i).getFullName();
        }

        return names;
    }
}