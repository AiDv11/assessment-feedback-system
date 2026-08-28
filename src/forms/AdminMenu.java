/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forms;

import panels.CreateUserPanel;
import panels.ViewUsersPanel;
import panels.UpdateUserPanel; 
import panels.AssignLecturerPanel;
import panels.CreateClassPanel;
import panels.EditProfilePanel;
import panels.DeleteUserPanel; 
import panels.DefineGradingPanel;
import models.AdminStaff;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Hussain Alkhaldi
 */
public class AdminMenu extends javax.swing.JFrame {
    private AdminStaff admin;
    
    
    public AdminMenu(AdminStaff admin) {
        this.admin = admin;
        initComponents();
        
        this.setLocationRelativeTo(null);

        CreateUserPanel cup = new CreateUserPanel();
        contentPanel.add(cup, "createuserpanel");
        
        ViewUsersPanel vup = new ViewUsersPanel();
        contentPanel.add(vup, "viewuserspanel");
        
        UpdateUserPanel uup = new UpdateUserPanel();
        contentPanel.add(uup, "updateuserpanel");
        
        EditProfilePanel epp = new EditProfilePanel(admin);
        contentPanel.add(epp, "editprofilepanel");
        
        DeleteUserPanel dup = new DeleteUserPanel(admin);
        contentPanel.add(dup, "deleteuserpanel");
        
        DefineGradingPanel dgp = new DefineGradingPanel();
        contentPanel.add(dgp, "definegradingpanel");
        
        AssignLecturerPanel alp = new AssignLecturerPanel();
        contentPanel.add(alp, "assignlecturerpanel");
        
        CreateClassPanel ccp = new CreateClassPanel();
        contentPanel.add(ccp, "createclasspanel");
        
        
    }
    
  
    

  
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            admin.logout();
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

        topPanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        sidebarPanel = new javax.swing.JPanel();
        btnCreateUser = new javax.swing.JButton();
        btnUpdateUser = new javax.swing.JButton();
        btnDeleteUser = new javax.swing.JButton();
        btnViewUsers = new javax.swing.JButton();
        btnAssignLecturer = new javax.swing.JButton();
        btnDefineGrading = new javax.swing.JButton();
        btnCreateClass = new javax.swing.JButton();
        btnEditProfile = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setResizable(false);

        topPanel.setBackground(new java.awt.Color(44, 62, 80));
        topPanel.setAlignmentX(0.0F);
        topPanel.setAlignmentY(0.0F);
        topPanel.setPreferredSize(new java.awt.Dimension(1200, 80));
        topPanel.setLayout(new java.awt.BorderLayout());

        lblWelcome.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, Administrator");
        topPanel.add(lblWelcome, java.awt.BorderLayout.WEST);

        btnLogout.setBackground(new java.awt.Color(231, 76, 60));
        btnLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        topPanel.add(btnLogout, java.awt.BorderLayout.EAST);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        sidebarPanel.setBackground(new java.awt.Color(52, 73, 94));
        sidebarPanel.setAlignmentX(0.0F);
        sidebarPanel.setAlignmentY(0.0F);
        sidebarPanel.setPreferredSize(new java.awt.Dimension(250, 620));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCreateUser.setBackground(new java.awt.Color(52, 73, 94));
        btnCreateUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCreateUser.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateUser.setText("Create User");
        btnCreateUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCreateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateUserActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnCreateUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 250, 60));

        btnUpdateUser.setBackground(new java.awt.Color(52, 73, 94));
        btnUpdateUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnUpdateUser.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateUser.setText("Update User");
        btnUpdateUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateUserActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnUpdateUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 85, 250, 60));

        btnDeleteUser.setBackground(new java.awt.Color(52, 73, 94));
        btnDeleteUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeleteUser.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteUser.setText("Delete User");
        btnDeleteUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteUserActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnDeleteUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 250, 60));

        btnViewUsers.setBackground(new java.awt.Color(52, 73, 94));
        btnViewUsers.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnViewUsers.setForeground(new java.awt.Color(255, 255, 255));
        btnViewUsers.setText("View All Users");
        btnViewUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnViewUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewUsersActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnViewUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 215, 250, 60));

        btnAssignLecturer.setBackground(new java.awt.Color(52, 73, 94));
        btnAssignLecturer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAssignLecturer.setForeground(new java.awt.Color(255, 255, 255));
        btnAssignLecturer.setText("Assign Lecturer");
        btnAssignLecturer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAssignLecturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignLecturerActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnAssignLecturer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 250, 60));

        btnDefineGrading.setBackground(new java.awt.Color(52, 73, 94));
        btnDefineGrading.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDefineGrading.setForeground(new java.awt.Color(255, 255, 255));
        btnDefineGrading.setText("Define Grading");
        btnDefineGrading.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDefineGrading.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefineGradingActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnDefineGrading, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 345, 250, 60));

        btnCreateClass.setBackground(new java.awt.Color(52, 73, 94));
        btnCreateClass.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCreateClass.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateClass.setText("Create Class");
        btnCreateClass.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCreateClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateClassActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnCreateClass, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 250, 60));

        btnEditProfile.setBackground(new java.awt.Color(52, 73, 94));
        btnEditProfile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEditProfile.setForeground(new java.awt.Color(255, 255, 255));
        btnEditProfile.setText("Edit Profile");
        btnEditProfile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProfileActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnEditProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 475, 250, 60));

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.WEST);

        contentPanel.setBackground(new java.awt.Color(236, 240, 241));
        contentPanel.setPreferredSize(new java.awt.Dimension(950, 620));
        contentPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateUserActionPerformed
        // TODO add your handling code here:
        CardLayout cup = (CardLayout) contentPanel.getLayout();
        cup.show(contentPanel, "createuserpanel");
    }//GEN-LAST:event_btnCreateUserActionPerformed

    private void btnUpdateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateUserActionPerformed
        // TODO add your handling code here:
        CardLayout uup = (CardLayout) contentPanel.getLayout();
        uup.show(contentPanel, "updateuserpanel");
    }//GEN-LAST:event_btnUpdateUserActionPerformed

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteUserActionPerformed
        // TODO add your handling code here:
        CardLayout dup = (CardLayout) contentPanel.getLayout();
        dup.show(contentPanel, "deleteuserpanel");
    }//GEN-LAST:event_btnDeleteUserActionPerformed

    private void btnViewUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewUsersActionPerformed
        CardLayout vup = (CardLayout) contentPanel.getLayout();
        vup.show(contentPanel, "viewuserspanel");
    }//GEN-LAST:event_btnViewUsersActionPerformed

    private void btnAssignLecturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignLecturerActionPerformed
        // TODO add your handling code here:
        CardLayout alp = (CardLayout) contentPanel.getLayout();
        alp.show(contentPanel, "assignlecturerpanel");
    }//GEN-LAST:event_btnAssignLecturerActionPerformed

    private void btnDefineGradingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefineGradingActionPerformed
        // TODO add your handling code here:
        CardLayout dgp = (CardLayout) contentPanel.getLayout();
        dgp.show(contentPanel, "definegradingpanel");
    }//GEN-LAST:event_btnDefineGradingActionPerformed

    private void btnCreateClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateClassActionPerformed
        // TODO add your handling code here:
        CardLayout ccp = (CardLayout) contentPanel.getLayout();
        ccp.show(contentPanel, "createclasspanel");
        
    }//GEN-LAST:event_btnCreateClassActionPerformed

    private void btnEditProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProfileActionPerformed
        // TODO add your handling code here:
        CardLayout vup = (CardLayout) contentPanel.getLayout();
        vup.show(contentPanel, "editprofilepanel");
    }//GEN-LAST:event_btnEditProfileActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_btnLogoutActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAssignLecturer;
    private javax.swing.JButton btnCreateClass;
    private javax.swing.JButton btnCreateUser;
    private javax.swing.JButton btnDefineGrading;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnEditProfile;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnUpdateUser;
    private javax.swing.JButton btnViewUsers;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
