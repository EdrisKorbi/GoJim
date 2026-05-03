package com.gojim.ui;

import com.gojim.ui.components.RoundedButton;
import com.gojim.ui.components.RoundedPanel;
import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("GoJim - Login");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(AppColors.BACKGROUND);

        RoundedPanel card = new RoundedPanel(25, AppColors.CARD);
        card.setLayout(new BorderLayout(0, 22));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        card.setPreferredSize(new Dimension(380, 300));

        card.add(createTitle(), BorderLayout.NORTH);
        card.add(createForm(), BorderLayout.CENTER);
        card.add(createButton(), BorderLayout.SOUTH);

        root.add(card);
        add(root);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("<html><b>💪 GoJim</b><br><span style='font-size:14px;'>Admin Login</span></html>");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        return title;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(2, 1, 0, 16));
        form.setOpaque(false);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        form.add(createFieldRow("Username", usernameField));
        form.add(createFieldRow("Password", passwordField));

        return form;
    }

    private JPanel createFieldRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(90, 42));
        label.setForeground(AppColors.TEXT_WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        field.setOpaque(false);
        field.setForeground(AppColors.TEXT_WHITE);
        field.setCaretColor(AppColors.TEXT_WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        RoundedPanel wrapper = new RoundedPanel(18, AppColors.BACKGROUND);
        wrapper.setLayout(new BorderLayout());
        wrapper.add(field, BorderLayout.CENTER);

        row.add(label, BorderLayout.WEST);
        row.add(wrapper, BorderLayout.CENTER);

        return row;
    }

    private RoundedButton createButton() {
        RoundedButton loginBtn = new RoundedButton("Login", 16);

        getRootPane().setDefaultButton(loginBtn);

        loginBtn.addActionListener(e -> login());

        return loginBtn;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Simple local login for now
        if (username.equals("admin") && password.equals("admin")) {
            new DashboardFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }
}