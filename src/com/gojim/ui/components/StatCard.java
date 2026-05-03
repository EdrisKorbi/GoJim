package com.gojim.ui.components;

import com.gojim.util.AppColors;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Small dashboard card used for stats:
 * Revenue, Members, Trainers, Visits, etc.
 */
public class StatCard extends RoundedPanel {
    public StatCard(String title, String value, String subtitle) {
        super(22, AppColors.CARD);

        setLayout(new BorderLayout());
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(AppColors.TEXT_GRAY);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(AppColors.TEXT_WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(AppColors.ACCENT);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
        add(subtitleLabel, BorderLayout.SOUTH);
    }
}