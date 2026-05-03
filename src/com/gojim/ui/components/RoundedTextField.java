package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom JTextField with:
 * - Rounded corners
 * - Placeholder text
 */
public class RoundedTextField extends JTextField {

    private String placeholder;
    private final int radius;

    public RoundedTextField(String placeholder, int radius) {
        this.placeholder = placeholder;
        this.radius = radius;

        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));

        setForeground(AppColors.TEXT_WHITE);
        setCaretColor(AppColors.TEXT_WHITE);
        setFont(new Font("Arial", Font.PLAIN, 14));

        // Focus behavior for placeholder
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(AppColors.CARD);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: border
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(AppColors.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw placeholder only if empty and not focused
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(AppColors.TEXT_GRAY);
            g2.setFont(getFont());

            Insets insets = getInsets();
            g2.drawString(placeholder, insets.left, getHeight() / 2 + 5);

            g2.dispose();
        }
    }
}