package com.gojim.ui.panels;

import com.gojim.ui.components.*;
import com.gojim.util.AppColors;

import com.gojim.dao.MemberDAO;
import com.gojim.model.Member;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class MembersPanel extends JPanel {

    private JTable membersTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    private MemberDAO memberDAO = new MemberDAO();

    private PaymentsPanel paymentsPanel;

    public MembersPanel() {
        setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createTableCard(), BorderLayout.CENTER);
    }

    public void setPaymentsPanel(PaymentsPanel paymentsPanel) {
        this.paymentsPanel = paymentsPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 20));
        header.setOpaque(false);

        JLabel title = new JLabel("Members Management");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        JPanel actions = new JPanel(new BorderLayout(12, 0));
        actions.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundedTextField searchField = new RoundedTextField("Search member...", 25);
        searchField.setPreferredSize(new Dimension(200, 42));
        searchField.setForeground(AppColors.TEXT_GRAY);
        searchField.setBackground(AppColors.CARD);
        searchField.setCaretColor(AppColors.TEXT_WHITE);
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();

                if (text.isEmpty()) {
                    tableSorter.setRowFilter(null);
                } else {
                    tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
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

        RoundedButton addButton = new RoundedButton("+ Add Member", 28);
        RoundedButton deleteButton = new RoundedButton("Delete", 18);
        RoundedButton editButton = new RoundedButton("Edit", 18);

        addButton.addActionListener(e -> {
            AddMemberDialog dialog = new AddMemberDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this));

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {

                boolean success = memberDAO.addMember(
                        dialog.getNameValue(),
                        Integer.parseInt(dialog.getAgeValue()),
                        "+216 " + dialog.getPhoneValue(),
                        dialog.getSelectedSubscriptionId());

                if (success) {
                    loadMembersFromDatabase();
                    if (paymentsPanel != null) {
                        paymentsPanel.loadPaymentsFromDatabase();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Could not add member.");
                }
            }
        });

        addButton.setFont(new Font("Arial", Font.BOLD, 14));

        editButton.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to edit.");
                return;
            }

            int modelRow = membersTable.convertRowIndexToModel(selectedRow);
            int memberId = (int) tableModel.getValueAt(modelRow, 0);

            AddMemberDialog dialog = new AddMemberDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    tableModel.getValueAt(modelRow, 1).toString(),
                    tableModel.getValueAt(modelRow, 2).toString(),
                    tableModel.getValueAt(modelRow, 3).toString(),
                    tableModel.getValueAt(modelRow, 4).toString());

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                boolean success = memberDAO.updateMember(
                        memberId,
                        dialog.getNameValue(),
                        Integer.parseInt(dialog.getAgeValue()),
                        "+216 " + dialog.getPhoneValue(),
                        dialog.getSelectedSubscriptionId());

                if (success) {
                    loadMembersFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not update member.");
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to delete.");
                return;
            }

            int modelRow = membersTable.convertRowIndexToModel(selectedRow);
            int memberId = (int) tableModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this member?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = memberDAO.deleteMember(memberId);

                if (success) {
                    loadMembersFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete member.");
                }
            }
        });

        left.add(searchField, BorderLayout.CENTER);
        right.add(addButton);
        right.add(editButton);
        right.add(deleteButton);

        actions.add(left, BorderLayout.CENTER);
        actions.add(right, BorderLayout.EAST);

        header.add(title, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        return header;
    }

    /**
     * Creates the main table card.
     */
    private RoundedPanel createTableCard() {
        RoundedPanel card = new RoundedPanel(25, AppColors.CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel sectionTitle = new JLabel("All Members");
        sectionTitle.setForeground(AppColors.TEXT_WHITE);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 22));

        createMembersTable();

        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppColors.CARD);

        card.add(sectionTitle, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void createMembersTable() {
        String[] columns = {
                "ID",
                "Member Name",
                "Age",
                "Phone",
                "Plan",
                "Expired Date",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadMembersFromDatabase();

        membersTable = new JTable(tableModel);

        tableSorter = new TableRowSorter<>(tableModel);
        membersTable.setRowSorter(tableSorter);

        membersTable.setShowVerticalLines(false);
        membersTable.setShowHorizontalLines(true);
        membersTable.setGridColor(AppColors.BORDER);
        membersTable.setIntercellSpacing(new java.awt.Dimension(0, 1));

        // Main table style
        membersTable.setBackground(AppColors.CARD);
        membersTable.setForeground(AppColors.TEXT_WHITE);
        membersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        membersTable.setRowHeight(48);

        // Selection style
        membersTable.setSelectionBackground(new java.awt.Color(45, 45, 50));
        membersTable.setSelectionForeground(AppColors.ACCENT);

        // Header style
        JTableHeader header = membersTable.getTableHeader();
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
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

                // padding
                label.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

                return label;
            }
        });

        // remove default border look
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 45));

        // Custom row rendering for alternating row colors + status coloring
        membersTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                java.awt.Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setFont(new Font("Arial", Font.PLAIN, 14));

                if (isSelected) {
                    cell.setBackground(new java.awt.Color(45, 45, 50));
                    cell.setForeground(AppColors.ACCENT);
                } else {
                    if (row % 2 == 0) {
                        cell.setBackground(AppColors.CARD);
                    } else {
                        cell.setBackground(new java.awt.Color(18, 18, 24));
                    }

                    // Status column
                    if (column == 6) {
                        String status = value.toString();

                        if (status.equalsIgnoreCase("Active")) {
                            cell.setForeground(AppColors.ACCENT);
                        } else {
                            cell.setForeground(new java.awt.Color(255, 90, 90));
                        }
                    } else {
                        cell.setForeground(AppColors.TEXT_WHITE);
                    }
                }

                return cell;
            }
        });
    }

    public void loadMembersFromDatabase() {
        tableModel.setRowCount(0);

        for (Member member : memberDAO.getAllMembers()) {
            tableModel.addRow(new Object[] {
                    member.getId(),
                    member.getFullName(),
                    member.getAge(),
                    member.getPhone(),
                    member.getSubscriptionName(),
                    member.getEndDate(),
                    member.getStatus()
            });
        }
    }
}