package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Font;

/**
 * Custom sidebar button.
 * Used for navigation: Dashboard, Members, Trainers, Payments.
 */
public class SidebarButton extends JButton {
    public SidebarButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setForeground(AppColors.TEXT_WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setHorizontalAlignment(LEFT);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 18, 12, 18));
    }
}