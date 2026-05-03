package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.*;

/**
 * Custom rounded dropdown.
 * We do NOT use JComboBox because default Swing dropdowns are hard to style.
 * Instead, we create our own floating dropdown using JLayeredPane.
 */
public class RoundedComboBox extends RoundedPanel {

    private String selectedValue;
    private final JLabel valueLabel;
    private JWindow dropdownWindow;

    public RoundedComboBox(String[] items) {
        super(18, AppColors.CARD);

        selectedValue = items[0];

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(220, 42));

        valueLabel = new JLabel(selectedValue);
        valueLabel.setForeground(AppColors.TEXT_WHITE);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel arrow = new JLabel("▼");
        arrow.setForeground(AppColors.TEXT_GRAY);
        arrow.setFont(new Font("Arial", Font.PLAIN, 11));

        add(valueLabel, BorderLayout.CENTER);
        add(arrow, BorderLayout.EAST);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleDropdown(items);
            }
        });
    }

    /**
     * Shows or hides the custom dropdown.
     */
    /**
     * Shows or hides the custom dropdown.
     * We use JWindow so the dropdown is not clipped by the dialog.
     */
    private void toggleDropdown(String[] items) {
        if (dropdownWindow != null && dropdownWindow.isVisible()) {
            dropdownWindow.dispose();
            dropdownWindow = null;
            return;
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        dropdownWindow = new JWindow(parentWindow);
        dropdownWindow.setBackground(new Color(0, 0, 0, 0));

        int width = getWidth();
        int height = items.length * 44 + 12;

        RoundedPanel menuPanel = new RoundedPanel(18, AppColors.CARD_DARK);
        menuPanel.setLayout(new GridLayout(items.length, 1));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        for (String item : items) {
            menuPanel.add(createOption(item));
        }

        dropdownWindow.add(menuPanel);
        dropdownWindow.setSize(width, height);

        Point screenPoint = getLocationOnScreen();

        // Always opens downward
        dropdownWindow.setLocation(screenPoint.x, screenPoint.y + getHeight() + 6);

        dropdownWindow.setVisible(true);
    }

    public void setSelectedValue(String value) {
        selectedValue = value;
        valueLabel.setText(value);
    }

    /**
     * Creates one dropdown option.
     */
    private JLabel createOption(String item) {
        JLabel option = new JLabel(item);
        option.setOpaque(true);
        option.setBackground(AppColors.CARD_DARK);
        option.setForeground(AppColors.TEXT_WHITE);
        option.setFont(new Font("Arial", Font.PLAIN, 14));
        option.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        option.setCursor(new Cursor(Cursor.HAND_CURSOR));

        option.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                option.setBackground(AppColors.ACCENT);
                option.setForeground(AppColors.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                option.setBackground(AppColors.CARD_DARK);
                option.setForeground(AppColors.TEXT_WHITE);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                setSelectedValue(item);

                if (dropdownWindow != null) {
                    dropdownWindow.dispose();
                    dropdownWindow = null;
                }
            }
        });

        return option;
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}