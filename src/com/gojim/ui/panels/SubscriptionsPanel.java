package com.gojim.ui.panels;

import com.gojim.ui.components.AddPlanDialog;
import com.gojim.ui.components.RoundedButton;
import com.gojim.ui.components.RoundedPanel;
import com.gojim.util.AppColors;

import com.gojim.dao.SubscriptionDAO;
import com.gojim.model.Subscription;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SubscriptionsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

    public SubscriptionsPanel() {
        setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Subscriptions");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        RoundedButton addBtn = new RoundedButton("+ Add Plan", 18);
        RoundedButton editBtn = new RoundedButton("Edit", 18);
        RoundedButton deleteBtn = new RoundedButton("Delete", 18);

        addBtn.addActionListener(e -> {
            AddPlanDialog dialog = new AddPlanDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this));

            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                boolean success = subscriptionDAO.addSubscription(
                        dialog.getNameValue(),
                        Integer.parseInt(dialog.getDurationValue()),
                        Double.parseDouble(dialog.getPriceValue()));

                if (success) {
                    loadSubscriptionsFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not add subscription.");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a plan to edit.");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            int planId = (int) model.getValueAt(modelRow, 0);

            AddPlanDialog dialog = new AddPlanDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    model.getValueAt(modelRow, 1).toString(),
                    model.getValueAt(modelRow, 2).toString(),
                    model.getValueAt(modelRow, 3).toString());

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                boolean success = subscriptionDAO.updateSubscription(
                        planId,
                        dialog.getNameValue(),
                        Integer.parseInt(dialog.getDurationValue()),
                        Double.parseDouble(dialog.getPriceValue()));

                if (success) {
                    loadSubscriptionsFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not update plan.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a plan to delete.");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            int planId = (int) model.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this plan?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = subscriptionDAO.deleteSubscription(planId);

                if (success) {
                    loadSubscriptionsFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Could not delete plan. It may be used by existing members.");
                }
            }
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(addBtn);
        right.add(editBtn);
        right.add(deleteBtn);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private RoundedPanel createTable() {
        RoundedPanel panel = new RoundedPanel(25, AppColors.CARD);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] cols = { "ID", "Plan Name", "Duration", "Price" };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        loadSubscriptionsFromDatabase();

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel label = new JLabel(value.toString());
                label.setForeground(AppColors.TEXT_GRAY);
                label.setBackground(AppColors.CARD);
                label.setFont(new Font("Arial", Font.BOLD, 13));
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

                return label;
            }
        });

        tableHeader.setBorder(BorderFactory.createEmptyBorder());
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 45));
        tableHeader.setReorderingAllowed(false);

        table.setBackground(AppColors.CARD);
        table.setForeground(AppColors.TEXT_WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(AppColors.BORDER);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(45, 45, 50));
        table.setSelectionForeground(AppColors.ACCENT);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setFont(new Font("Arial", Font.PLAIN, 14));

                if (isSelected) {
                    cell.setBackground(new Color(45, 45, 50));
                    cell.setForeground(AppColors.ACCENT);
                } else {
                    cell.setBackground(row % 2 == 0 ? AppColors.CARD : new Color(18, 18, 24));
                    cell.setForeground(AppColors.TEXT_WHITE);
                }

                return cell;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppColors.CARD);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JLabel sectionTitle = new JLabel("Plans");
        sectionTitle.setForeground(AppColors.TEXT_WHITE);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(sectionTitle, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadSubscriptionsFromDatabase() {
        model.setRowCount(0);

        for (Subscription subscription : subscriptionDAO.getAllSubscriptions()) {
            model.addRow(new Object[] {
                    subscription.getId(),
                    subscription.getPlanName(),
                    subscription.getDurationDays() + " days",
                    subscription.getPrice() + " DT"
            });
        }
    }
}