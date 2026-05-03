package com.gojim.ui.panels;

import com.gojim.ui.components.AddTrainerDialog;
import com.gojim.ui.components.RoundedButton;
import com.gojim.ui.components.RoundedPanel;
import com.gojim.ui.components.RoundedTextField;
import com.gojim.util.AppColors;

import com.gojim.dao.TrainerDAO;
import com.gojim.model.Trainer;

import javax.swing.table.TableRowSorter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

public class TrainersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private TrainerDAO trainerDAO = new TrainerDAO();

    private TableRowSorter<DefaultTableModel> tableSorter;

    public TrainersPanel() {
        setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Trainers");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        RoundedTextField searchField = new RoundedTextField("Search trainer...", 20);
        searchField.setPreferredSize(new Dimension(200, 42));

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();

                if (text.isEmpty()) {
                    tableSorter.setRowFilter(null);
                } else {
                    tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }
        });

        RoundedButton addBtn = new RoundedButton("+ Add Trainer", 18);
        RoundedButton editBtn = new RoundedButton("Edit", 18);
        RoundedButton deleteBtn = new RoundedButton("Delete", 18);

        addBtn.addActionListener(e -> {
            AddTrainerDialog dialog = new AddTrainerDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this));

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                boolean success = trainerDAO.addTrainer(
                        dialog.getNameValue(),
                        dialog.getSpecialtyValue(),
                        dialog.getExperienceValue());

                if (success) {
                    loadTrainersFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not add trainer.");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a trainer to edit.");
                return;
            }

            // 🔥 THIS LINE WAS MISSING
            int modelRow = table.convertRowIndexToModel(selectedRow);

            int trainerId = (int) model.getValueAt(modelRow, 0);

            AddTrainerDialog dialog = new AddTrainerDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    model.getValueAt(modelRow, 1).toString(),
                    model.getValueAt(modelRow, 2).toString(),
                    model.getValueAt(modelRow, 3).toString());

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                boolean success = trainerDAO.updateTrainer(
                        trainerId,
                        dialog.getNameValue(),
                        dialog.getSpecialtyValue(),
                        dialog.getExperienceValue());

                if (success) {
                    loadTrainersFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not update trainer.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a trainer to delete.");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            int trainerId = (int) model.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this trainer?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = trainerDAO.deleteTrainer(trainerId);

                if (success) {
                    loadTrainersFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete trainer.");
                }
            }
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(searchField);
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

        String[] cols = { "ID", "Name", "Specialty", "Experience" };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        tableSorter = new TableRowSorter<>(model);
        table.setRowSorter(tableSorter);

        loadTrainersFromDatabase();

        // ===== HEADER STYLE =====
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
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

        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);

        // ===== TABLE STYLE =====
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

        // ===== ROW RENDERER =====
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

        // ===== SCROLL FIX =====
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppColors.CARD);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        // ===== TITLE =====
        JLabel title = new JLabel("All Trainers");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadTrainersFromDatabase() {
        model.setRowCount(0);

        for (Trainer trainer : trainerDAO.getAllTrainers()) {
            model.addRow(new Object[] {
                    trainer.getId(),
                    trainer.getFullName(),
                    trainer.getSpecialty(),
                    trainer.getExperience()
            });
        }
    }
}