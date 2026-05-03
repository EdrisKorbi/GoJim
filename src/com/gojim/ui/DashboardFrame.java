package com.gojim.ui;

import com.gojim.ui.components.SidebarButton;
import com.gojim.ui.panels.*;
import com.gojim.util.AppColors;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    // Keep panel references so we can refresh them when needed
    private MembersPanel membersPanel;
    private DashboardPanel dashboardPanel;
    private SubscriptionsPanel subscriptionsPanel;
    private TrainersPanel trainersPanel;
    private PaymentsPanel paymentsPanel;

    public DashboardFrame() {
        setTitle("GoJim - Gym Management");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        createContentPanel();

        add(createSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates all panels once.
     * DashboardPanel receives MembersPanel so it can refresh members after adding a
     * member.
     */
    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        membersPanel = new MembersPanel();
        paymentsPanel = new PaymentsPanel();

        membersPanel.setPaymentsPanel(paymentsPanel);

        dashboardPanel = new DashboardPanel(membersPanel, paymentsPanel);

        dashboardPanel.setNavigation(
                () -> cardLayout.show(contentPanel, "Members"),
                () -> cardLayout.show(contentPanel, "Trainers"),
                () -> cardLayout.show(contentPanel, "Payments"));

        subscriptionsPanel = new SubscriptionsPanel();
        trainersPanel = new TrainersPanel();

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(membersPanel, "Members");
        contentPanel.add(subscriptionsPanel, "Subscriptions");
        contentPanel.add(trainersPanel, "Trainers");
        contentPanel.add(paymentsPanel, "Payments");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(AppColors.CARD_DARK);
        sidebar.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        sidebar.setPreferredSize(new java.awt.Dimension(230, 0));

        JLabel logo = new JLabel("<html><b>💪 GoJim</b><br><span style='font-size:10px;'>Gym Management</span></html>");
        logo.setForeground(AppColors.TEXT_WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel menu = new JPanel(new GridLayout(6, 1, 0, 8));
        menu.setOpaque(false);

        SidebarButton dashboardBtn = new SidebarButton("Dashboard");
        SidebarButton membersBtn = new SidebarButton("Members");
        SidebarButton subscriptionsBtn = new SidebarButton("Subscriptions");
        SidebarButton trainersBtn = new SidebarButton("Trainers");
        SidebarButton paymentsBtn = new SidebarButton("Payments");
        SidebarButton logoutBtn = new SidebarButton("Logout");

        dashboardBtn.addActionListener(e -> {
            // Recreate dashboard so all cards/statistics reload from DB
            contentPanel.remove(dashboardPanel);
            dashboardPanel = new DashboardPanel(membersPanel, paymentsPanel);

            dashboardPanel.setNavigation(
                    () -> cardLayout.show(contentPanel, "Members"),
                    () -> cardLayout.show(contentPanel, "Trainers"),
                    () -> cardLayout.show(contentPanel, "Payments"));

            contentPanel.add(dashboardPanel, "Dashboard", 0);

            cardLayout.show(contentPanel, "Dashboard");
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        membersBtn.addActionListener(e -> {
            membersPanel.loadMembersFromDatabase();
            cardLayout.show(contentPanel, "Members");
        });

        subscriptionsBtn.addActionListener(e -> cardLayout.show(contentPanel, "Subscriptions"));
        trainersBtn.addActionListener(e -> cardLayout.show(contentPanel, "Trainers"));
        paymentsBtn.addActionListener(e -> cardLayout.show(contentPanel, "Payments"));

        logoutBtn.addActionListener(e -> System.exit(0));

        menu.add(dashboardBtn);
        menu.add(membersBtn);
        menu.add(paymentsBtn);
        menu.add(trainersBtn);
        menu.add(subscriptionsBtn);
        menu.add(logoutBtn);

        sidebar.add(logo, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);

        return sidebar;
    }
}