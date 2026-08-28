/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package afs.users;

import models.AcademicLeader;
import java.awt.CardLayout;

import javax.swing.*;
import afs.users.ManageModulesPanel;
import forms.LoginForm;



public class AcademicLeaderMenu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AcademicLeaderMenu.class.getName());

    
private AcademicLeader leader;
private CardLayout cardLayout;

private static final String DASHBOARD = "Dashboard";
private static final String MANAGE_MODULES = "ManageModules";
private static final String EDIT_PROFILE = "EditProfile";
private static final String REPORTS = "Reports";

    
    
    
public AcademicLeaderMenu(AcademicLeader leader) {
    this.leader = leader;

    initComponents(); // NetBeans generated

    setupUI();
//    setupPanels();

//        ManageModulesPanel mmp = new ManageModulesPanel(leader, this);
        contentPanel.add(new ManageModulesPanel(leader, this), MANAGE_MODULES);
        contentPanel.add(new EditProfilePanel(leader, this), EDIT_PROFILE);
        contentPanel.add(new ReportsPanel(leader, this), REPORTS);

    showPanel(DASHBOARD);
}



private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            leader.logout();
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            });
        }
    }




    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        sidebarPanel = new javax.swing.JPanel();
        sidebarPanel1 = new javax.swing.JPanel();
        btnManageModules = new javax.swing.JButton();
        btnEditProfile = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Academic Leader System");
        setSize(new java.awt.Dimension(1100, 700));

        sidebarPanel.setBackground(new java.awt.Color(255, 245, 200));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(250, 700));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidebarPanel1.setBackground(new java.awt.Color(255, 245, 200));
        sidebarPanel1.setPreferredSize(new java.awt.Dimension(250, 620));
        sidebarPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnManageModules.setBackground(new java.awt.Color(255, 245, 200));
        btnManageModules.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnManageModules.setForeground(new java.awt.Color(0, 0, 0));
        btnManageModules.setText("Manage Modules");
        btnManageModules.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnManageModules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageModulesActionPerformed(evt);
            }
        });
        sidebarPanel1.add(btnManageModules, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 60));

        btnEditProfile.setBackground(new java.awt.Color(255, 245, 200));
        btnEditProfile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditProfile.setForeground(new java.awt.Color(0, 0, 0));
        btnEditProfile.setText("Edit Profile");
        btnEditProfile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProfileActionPerformed(evt);
            }
        });
        sidebarPanel1.add(btnEditProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 250, 60));

        btnReports.setBackground(new java.awt.Color(255, 245, 200));
        btnReports.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnReports.setForeground(new java.awt.Color(0, 0, 0));
        btnReports.setText("Reports");
        btnReports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });
        sidebarPanel1.add(btnReports, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 250, 60));

        sidebarPanel.add(sidebarPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 350));

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.WEST);

        jPanel3.setBackground(new java.awt.Color(255, 170, 85));
        jPanel3.setForeground(new java.awt.Color(15, 76, 92));
        jPanel3.setPreferredSize(new java.awt.Dimension(1200, 80));
        jPanel3.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("Welcome, Academic Leader");
        jPanel3.add(lblHeader, java.awt.BorderLayout.WEST);

        btnLogout.setBackground(new java.awt.Color(231, 76, 60));
        btnLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new java.awt.Dimension(87, 25));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel3.add(btnLogout, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnManageModulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageModulesActionPerformed
        // TODO add your handling code here:
         showPanel(MANAGE_MODULES);
    }//GEN-LAST:event_btnManageModulesActionPerformed

    private void btnEditProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProfileActionPerformed
        // TODO add your handling code here:
       showPanel(EDIT_PROFILE);
    }//GEN-LAST:event_btnEditProfileActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        // TODO add your handling code here:
        showPanel(REPORTS);
    }//GEN-LAST:event_btnReportsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
 logout();


    }//GEN-LAST:event_btnLogoutActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditProfile;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnManageModules;
    private javax.swing.JButton btnReports;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel sidebarPanel1;
    // End of variables declaration//GEN-END:variables



 private void setupUI() {
    if (lblHeader != null) {
        lblHeader.setText("Welcome, " + leader.getName());
    }

    if (!(contentPanel.getLayout() instanceof CardLayout)) {
        contentPanel.setLayout(new CardLayout());
    }

    cardLayout = (CardLayout) contentPanel.getLayout();

    setLocationRelativeTo(null);
    setTitle("Academic Leader System - " + leader.getName());
}


public void showPanel(String panelName) {
    if (cardLayout != null) {
        cardLayout.show(contentPanel, panelName);
    }
}

public void showDashboard() {
    showPanel(DASHBOARD);
}
}
                






