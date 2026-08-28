/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;
import models.Lecturer;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import javax.swing.SwingUtilities;

/**
 *
 * @author ali66
 */
public class LecturerDashboard extends javax.swing.JFrame {
    private Lecturer currentLecturer;
       public LecturerDashboard(Lecturer lecturer) {
        this.currentLecturer = lecturer;
        initComponents();
        lblWelcome.setText("Welcome, " + lecturer.getName());
        
    contentPanel.add(new EditProfilePanel(currentLecturer), "EditProfilePanel");
    contentPanel.add(new DesignAssessmentPanel(currentLecturer), "DesignAssessmentPanel");
    contentPanel.add(new KeyInMarksPanel(currentLecturer), "KeyInMarksPanel");
    contentPanel.add(new ProvideFeedbackPanel(currentLecturer), "ProvideFeedbackPanel");

 
    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "DesignAssessmentPanel");
    
  
    }
       
       
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            currentLecturer.logout();
            dispose();
            SwingUtilities.invokeLater(() -> {
                forms.LoginForm loginForm = new forms.LoginForm();
                loginForm.setVisible(true);
            });
        }
    }
    
    public Lecturer getCurrentLecturer() {
        return currentLecturer;
    }
    
    public void refreshDashboard(Lecturer updatedLecturer) {
        this.currentLecturer = updatedLecturer;
        lblWelcome.setText("Welcome, " + updatedLecturer.getName());
    }
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        sidebarPanel = new javax.swing.JPanel();
        btneditprofile = new javax.swing.JButton();
        btndesginassessments = new javax.swing.JButton();
        btnkeyinmarks = new javax.swing.JButton();
        btnprovidefeedback = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        topPanel.setBackground(new java.awt.Color(0, 0, 102));
        topPanel.setAlignmentX(0.0F);
        topPanel.setAlignmentY(0.0F);
        topPanel.setPreferredSize(new java.awt.Dimension(1200, 80));
        topPanel.setLayout(new java.awt.BorderLayout());

        lblWelcome.setBackground(new java.awt.Color(0, 0, 153));
        lblWelcome.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, Lectuerer");
        topPanel.add(lblWelcome, java.awt.BorderLayout.CENTER);

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

        contentPanel.setBackground(new java.awt.Color(245, 247, 250));
        contentPanel.setPreferredSize(new java.awt.Dimension(950, 620));
        contentPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        sidebarPanel.setBackground(new java.awt.Color(29, 78, 216));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(250, 620));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btneditprofile.setBackground(new java.awt.Color(15, 23, 42));
        btneditprofile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btneditprofile.setForeground(new java.awt.Color(255, 255, 255));
        btneditprofile.setText("Edit Profile");
        btneditprofile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btneditprofile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditprofileActionPerformed(evt);
            }
        });
        sidebarPanel.add(btneditprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 250, 60));

        btndesginassessments.setBackground(new java.awt.Color(15, 23, 42));
        btndesginassessments.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btndesginassessments.setForeground(new java.awt.Color(255, 255, 255));
        btndesginassessments.setText("Desgin Assessments");
        btndesginassessments.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btndesginassessments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndesginassessmentsActionPerformed(evt);
            }
        });
        sidebarPanel.add(btndesginassessments, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));

        btnkeyinmarks.setBackground(new java.awt.Color(15, 23, 42));
        btnkeyinmarks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnkeyinmarks.setForeground(new java.awt.Color(255, 255, 255));
        btnkeyinmarks.setText("Key in marks");
        btnkeyinmarks.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnkeyinmarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeyinmarksActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnkeyinmarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 250, 60));

        btnprovidefeedback.setBackground(new java.awt.Color(15, 23, 42));
        btnprovidefeedback.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnprovidefeedback.setForeground(new java.awt.Color(255, 255, 255));
        btnprovidefeedback.setText("Provide feedback");
        btnprovidefeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnprovidefeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprovidefeedbackActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnprovidefeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 250, 60));

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        logout();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btneditprofileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditprofileActionPerformed
        CardLayout cup = (CardLayout) contentPanel.getLayout();
        cup.show(contentPanel, "EditProfilePanel");
    }//GEN-LAST:event_btneditprofileActionPerformed

    private void btndesginassessmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndesginassessmentsActionPerformed
        // TODO add your handling code here:
        CardLayout uup = (CardLayout) contentPanel.getLayout();
        uup.show(contentPanel, "DesignAssessmentPanel");
    }//GEN-LAST:event_btndesginassessmentsActionPerformed

    private void btnkeyinmarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeyinmarksActionPerformed
        // TODO add your handling code here:
        CardLayout dup = (CardLayout) contentPanel.getLayout();
        dup.show(contentPanel, "KeyInMarksPanel");
    }//GEN-LAST:event_btnkeyinmarksActionPerformed

    private void btnprovidefeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprovidefeedbackActionPerformed
        CardLayout vup = (CardLayout) contentPanel.getLayout();
        vup.show(contentPanel, "ProvideFeedbackPanel");
    }//GEN-LAST:event_btnprovidefeedbackActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btndesginassessments;
    private javax.swing.JButton btneditprofile;
    private javax.swing.JButton btnkeyinmarks;
    private javax.swing.JButton btnprovidefeedback;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables

}