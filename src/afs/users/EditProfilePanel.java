package afs.users;

import models.AcademicLeader;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class EditProfilePanel extends javax.swing.JPanel {
    
    private AcademicLeader leader;
    private AcademicLeaderMenu menu;

    public EditProfilePanel(AcademicLeader leader, AcademicLeaderMenu menu) {
        this.leader = leader;
        this.menu = menu;
        initComponents();
        
  
        setupInputValidation();
        
        loadProfile();
    }

    /**
     * Setup input validation filters for text fields
     */
    private void setupInputValidation() {
        
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (string != null && string.matches("[a-zA-Z ]*")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep(); 
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null && text.matches("[a-zA-Z ]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep(); 
                }
            }
        });

    
        ((AbstractDocument) txtPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (string != null && string.matches("[0-9+\\-() ]*")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep(); 
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null && text.matches("[0-9+\\-() ]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep(); 
                }
            }
        });
    }

    private void loadProfile() {
 
        lblDisplayName.setText(leader.getName());
        lblDisplayEmail.setText(" Email: " + leader.getEmail());
        lblDisplayPhone.setText(" Phone: " + leader.getPhone());
        lblDisplayUserID.setText(" ID: " + leader.getUserID());
        lblDisplayUsername.setText(" Username: " + leader.getUsername());
        

        txtName.setText(leader.getName());
        txtEmail.setText(leader.getEmail());
        txtPhone.setText(leader.getPhone());
    }
    
    
    private void saveProfile() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        
   
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this,
                "Name can only contain letters and spaces!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        if (!phone.matches("[0-9+\\-() ]+")) {
            JOptionPane.showMessageDialog(this,
                "Phone number can only contain numbers and +, -, (), spaces!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.matches(".*\\d+.*")) {
            JOptionPane.showMessageDialog(this,
                "Phone number must contain at least one digit!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String oldLine = leader.findRawLine();
        if (oldLine == null) {
            JOptionPane.showMessageDialog(this,
                "Error: Could not find user record!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update leader object
        leader.setName(name);
        leader.setEmail(email);
        leader.setPhone(phone);
        
        if (leader.updateInFile(oldLine)) {
            JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh display
            loadProfile();
            
            if (menu != null) {
                menu.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to update profile!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void changePassword() {
        String oldPassword = new String(txtOldPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all password fields!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!leader.getPassword().equals(oldPassword)) {
            JOptionPane.showMessageDialog(this,
                "Old password is incorrect!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "New passwords do not match!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPassword.equals(oldPassword)) {
            JOptionPane.showMessageDialog(this,
                "New password must be different from old password!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String oldLine = leader.findRawLine();
        if (oldLine == null) {
            JOptionPane.showMessageDialog(this,
                "Error: Could not find user record!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        leader.setPassword(newPassword);
        
        if (leader.updateInFile(oldLine)) {
            JOptionPane.showMessageDialog(this,
                "Password changed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            txtOldPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to change password!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        centerPanel = new javax.swing.JPanel();
        currentProfilePanel = new javax.swing.JPanel();
        lblDisplayRole = new javax.swing.JLabel();
        lblDisplayName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        infoPanel = new javax.swing.JPanel();
        lblDisplayEmail = new javax.swing.JLabel();
        lblDisplayPhone = new javax.swing.JLabel();
        lblDisplayUserID = new javax.swing.JLabel();
        lblDisplayUsername = new javax.swing.JLabel();
        editFormPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        formContentPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtOldPassword = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        txtNewPassword = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        txtConfirmPassword = new javax.swing.JPasswordField();
        btnChangePassword = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        titlePanel.setBackground(new java.awt.Color(255, 170, 85));
        titlePanel.setPreferredSize(new java.awt.Dimension(866, 60));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Edit Profile");
        titlePanel.add(lblTitle);

        add(titlePanel, java.awt.BorderLayout.PAGE_START);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setLayout(new java.awt.BorderLayout());

        centerPanel.setBackground(new java.awt.Color(255, 245, 200));
        centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        currentProfilePanel.setBackground(new java.awt.Color(204, 204, 204));
        currentProfilePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        currentProfilePanel.setLayout(new javax.swing.BoxLayout(currentProfilePanel, javax.swing.BoxLayout.Y_AXIS));

        lblDisplayRole.setFont(new java.awt.Font("Arial", 2, 16)); // NOI18N
        lblDisplayRole.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayRole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDisplayRole.setText("Academic Leader");
        lblDisplayRole.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));
        currentProfilePanel.add(lblDisplayRole);

        lblDisplayName.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblDisplayName.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDisplayName.setText("(loading...)");
        lblDisplayName.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 20, 10, 20));
        currentProfilePanel.add(lblDisplayName);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        currentProfilePanel.add(jSeparator1);

        infoPanel.setBackground(new java.awt.Color(204, 204, 204));
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        infoPanel.setLayout(new java.awt.GridLayout(4, 1, 0, 20));

        lblDisplayEmail.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDisplayEmail.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayEmail.setText("Email: ");
        infoPanel.add(lblDisplayEmail);

        lblDisplayPhone.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDisplayPhone.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayPhone.setText("Phone: ");
        infoPanel.add(lblDisplayPhone);

        lblDisplayUserID.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDisplayUserID.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayUserID.setText("ID: ");
        infoPanel.add(lblDisplayUserID);

        lblDisplayUsername.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDisplayUsername.setForeground(new java.awt.Color(0, 0, 0));
        lblDisplayUsername.setText("Username: ");
        infoPanel.add(lblDisplayUsername);

        currentProfilePanel.add(infoPanel);

        centerPanel.add(currentProfilePanel);

        editFormPanel.setBackground(new java.awt.Color(255, 255, 255));
        editFormPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        editFormPanel.setLayout(new javax.swing.BoxLayout(editFormPanel, javax.swing.BoxLayout.Y_AXIS));

        formContentPanel.setBackground(new java.awt.Color(204, 204, 204));
        formContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formContentPanel.setLayout(new javax.swing.BoxLayout(formContentPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("EDIT PROFILE");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 20, 10));
        formContentPanel.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Name:");
        formContentPanel.add(jLabel2);

        txtName.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtName.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtName);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Email:");
        formContentPanel.add(jLabel3);

        txtEmail.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtEmail.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtEmail);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Phone:");
        formContentPanel.add(jLabel4);

        txtPhone.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtPhone.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtPhone);

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        formContentPanel.add(jSeparator2);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Change Password");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 20, 10));
        formContentPanel.add(jLabel5);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Old Password:");
        formContentPanel.add(jLabel6);

        txtOldPassword.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtOldPassword.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtOldPassword);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("New Passsword:");
        formContentPanel.add(jLabel7);

        txtNewPassword.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtNewPassword.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtNewPassword);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Confirm Password:");
        formContentPanel.add(jLabel8);

        txtConfirmPassword.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtConfirmPassword.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        formContentPanel.add(txtConfirmPassword);

        btnChangePassword.setBackground(new java.awt.Color(255, 245, 200));
        btnChangePassword.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnChangePassword.setForeground(new java.awt.Color(0, 0, 0));
        btnChangePassword.setText("Change Password");
        btnChangePassword.setMaximumSize(new java.awt.Dimension(200, 35));
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        formContentPanel.add(btnChangePassword);

        scrollPane.setViewportView(formContentPanel);

        editFormPanel.add(scrollPane);

        centerPanel.add(editFormPanel);

        formPanel.add(centerPanel, java.awt.BorderLayout.CENTER);

        add(formPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBackground(new java.awt.Color(255, 245, 200));
        buttonPanel.setPreferredSize(new java.awt.Dimension(10, 70));

        btnSave.setBackground(new java.awt.Color(255, 245, 200));
        btnSave.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(0, 0, 0));
        btnSave.setText("Save Changes");
        btnSave.setPreferredSize(new java.awt.Dimension(180, 40));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        buttonPanel.add(btnSave);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveProfile();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        changePassword();
    }//GEN-LAST:event_btnChangePasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel currentProfilePanel;
    private javax.swing.JPanel editFormPanel;
    private javax.swing.JPanel formContentPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDisplayEmail;
    private javax.swing.JLabel lblDisplayName;
    private javax.swing.JLabel lblDisplayPhone;
    private javax.swing.JLabel lblDisplayRole;
    private javax.swing.JLabel lblDisplayUserID;
    private javax.swing.JLabel lblDisplayUsername;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtNewPassword;
    private javax.swing.JPasswordField txtOldPassword;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
