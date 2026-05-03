package com.gojim.ui.panels;

import com.gojim.dao.MemberDAO;
import com.gojim.dao.PaymentDAO;
import com.gojim.dao.TrainerDAO;
import com.gojim.model.Member;
import com.gojim.ui.components.AddMemberDialog;
import com.gojim.ui.components.RoundedButton;
import com.gojim.ui.components.RoundedPanel;
import com.gojim.ui.components.StatCard;
import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class DashboardPanel extends JPanel {

    private final MemberDAO memberDAO = new MemberDAO();
    private final TrainerDAO trainerDAO = new TrainerDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    private MembersPanel membersPanel;
    private PaymentsPanel paymentsPanel;

    private Runnable openMembersPanel;
    private Runnable openTrainersPanel;
    private Runnable openPaymentsPanel;

    public DashboardPanel(MembersPanel membersPanel, PaymentsPanel paymentsPanel) {
        this.membersPanel = membersPanel;
        this.paymentsPanel = paymentsPanel;

        setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    public void setNavigation(
            Runnable openMembersPanel,
            Runnable openTrainersPanel,
            Runnable openPaymentsPanel) {
        this.openMembersPanel = openMembersPanel;
        this.openTrainersPanel = openTrainersPanel;
        this.openPaymentsPanel = openPaymentsPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("<html>Manage your<br>Fitness business</html>");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 34));

        RoundedButton addButton = new RoundedButton("+ New Member", 30);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

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
                    refreshDashboard();

                    if (membersPanel != null) {
                        membersPanel.loadMembersFromDatabase();
                    }

                    if (paymentsPanel != null) {
                        paymentsPanel.loadPaymentsFromDatabase();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Could not add member.");
                }
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new GridLayout(2, 2, 20, 20));
        content.setOpaque(false);

        content.add(createStatsGrid());
        content.add(createRevenueCard());
        content.add(createCapacityCard());
        content.add(createMembersPreviewCard());

        return content;
    }

    private JPanel createStatsGrid() {
        JPanel stats = new JPanel(new GridLayout(2, 2, 15, 15));
        stats.setOpaque(false);

        int totalMembers = memberDAO.getTotalMembers();
        int activeMembers = memberDAO.getActiveMembersCount();
        int totalTrainers = trainerDAO.getTotalTrainers();
        double revenue = paymentDAO.getTotalRevenue();

        StatCard revenueCard = new StatCard("Revenue", revenue + " DT", "Paid payments");
        StatCard membersCard = new StatCard("Members", String.valueOf(totalMembers), activeMembers + " active");
        StatCard visitedCard = new StatCard("Visited", String.valueOf(paymentDAO.getTodayPaidVisits()),
                "Today paid visits");
        StatCard trainersCard = new StatCard("Trainers", String.valueOf(totalTrainers), "Active trainers");

        // 👇 Make clickable
        revenueCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        membersCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        trainersCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        revenueCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (openPaymentsPanel != null)
                    openPaymentsPanel.run();
            }
        });

        membersCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (openMembersPanel != null)
                    openMembersPanel.run();
            }
        });

        trainersCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (openTrainersPanel != null)
                    openTrainersPanel.run();
            }
        });

        stats.add(revenueCard);
        stats.add(membersCard);
        stats.add(visitedCard);
        stats.add(trainersCard);

        return stats;
    }

    private RoundedPanel createRevenueCard() {
        RoundedPanel panel = new RoundedPanel(25, AppColors.CARD);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        double paid = paymentDAO.getTotalRevenue();
        double pending = paymentDAO.getPendingRevenue();

        JLabel title = new JLabel("Revenue Analytics");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel value = new JLabel(
                "<html>Paid Revenue: " + paid + " DT<br>Pending Revenue: " + pending + " DT</html>");
        value.setForeground(AppColors.TEXT_GRAY);
        value.setHorizontalAlignment(JLabel.CENTER);
        value.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(title, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);

        return panel;
    }

    private RoundedPanel createCapacityCard() {
        RoundedPanel panel = new RoundedPanel(25, AppColors.CARD);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int maxCapacity = 100;
        int activeMembers = memberDAO.getActiveMembersCount();
        int capacity = (activeMembers * 100) / maxCapacity;

        JLabel title = new JLabel("Gym Capacity");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel value = new JLabel("Space Status: " + capacity + "%");
        value.setForeground(AppColors.ACCENT);
        value.setFont(new Font("Arial", Font.BOLD, 22));
        value.setHorizontalAlignment(JLabel.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);

        return panel;
    }

    private RoundedPanel createMembersPreviewCard() {
        RoundedPanel panel = new RoundedPanel(25, AppColors.CARD);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Recent Members");
        title.setForeground(AppColors.TEXT_WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        StringBuilder html = new StringBuilder("<html>");

        int count = 0;
        for (Member member : memberDAO.getAllMembers()) {
            if (count == 5) {
                break;
            }

            html.append(member.getFullName())
                    .append(" — ")
                    .append(member.getStatus())
                    .append("<br>");

            count++;
        }

        html.append("</html>");

        JLabel preview = new JLabel(html.toString());
        preview.setForeground(AppColors.TEXT_GRAY);
        preview.setFont(new Font("Arial", Font.PLAIN, 15));

        panel.add(title, BorderLayout.NORTH);
        panel.add(preview, BorderLayout.CENTER);

        return panel;
    }

    private void refreshDashboard() {
        removeAll();

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}