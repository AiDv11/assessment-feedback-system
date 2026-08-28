/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import interfaces.Editor;

public class EditProfilePanel extends BaseFormPanel implements Editor {
    private User user;
    

    
        @Override
    public void loadData() {
        populateFields();
    }
    
    @Override
    public void saveChanges() {
        saveProfile();
    }

    public EditProfilePanel(User user) {
        this.user = user;
        initComponents();
        populateFields();
        setupEnterKeyNavigation();
        setupRealTimeValidation();
    }
    
 
    private void setupEnterKeyNavigation() {
        Component[] components = {
            txtName, txtGender, txtEmail, txtPhone, btnSave
        };
        
        for (int i = 0; i < components.length - 1; i++) {
            final int index = i;
            Component current = components[i];
            
            if (current instanceof JTextField) {
                ((JTextField) current).addActionListener(e -> components[index + 1].requestFocus());
            } else if (current instanceof JComboBox) {
                current.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            components[index + 1].requestFocus();
                        }
                    }
                });
            } else if (current instanceof JSpinner) {
                JComponent editor = ((JSpinner) current).getEditor();
                if (editor instanceof JSpinner.DefaultEditor) {
                    ((JSpinner.DefaultEditor) editor).getTextField().addActionListener(
                        e -> components[index + 1].requestFocus()
                    );
                }
            }
        }
    }
    

    private void setupRealTimeValidation() {
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateNameField();
            }
        });
        
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateEmailField();
            }
        });
        
        txtPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validatePhoneField();
            }
        });
        

    }
    
    private void validateNameField() {
        String name = txtName.getText().trim();
        if (!name.isEmpty()) {
            setFieldError(txtName, !isValidName(name));  
        } else {
            setFieldError(txtName, false);
        }
    }
    
    private void validateEmailField() {
        String email = txtEmail.getText().trim();
        if (!email.isEmpty()) {
            setFieldError(txtEmail, !isValidEmail(email));  
        } else {
            setFieldError(txtEmail, false);
        }
    }
    
    private void validatePhoneField() {
        String phone = txtPhone.getText().trim();
        if (!phone.isEmpty()) {
            setFieldError(txtPhone, !isValidPhone(phone));  
        } else {
            setFieldError(txtPhone, false);
        }
    }
    

private void populateFields() {
    
    lblInfo.setText(String.format("User ID: %s  |  Username: %s  |  Age: %d", 
        user.getUserID(), user.getUsername(), user.getAge()));
    
  
    txtName.setText(user.getName());
    txtEmail.setText(user.getEmail());
    txtPhone.setText(user.getPhone());
    
    
    txtGender.setText(user.getGender());

}
    
   private void saveProfile() {
        try {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isValidName(name)) {  
                setFieldError(txtName, true);
                JOptionPane.showMessageDialog(this, "Name must contain only letters and spaces",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isValidEmail(email)) {  
                setFieldError(txtEmail, true);
                JOptionPane.showMessageDialog(this, "Invalid email format",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isValidPhone(phone)) {  
                setFieldError(txtPhone, true);
                JOptionPane.showMessageDialog(this, "Phone must be 10-15 digits",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
      
            String oldRawLine = user.findRawLine();
            String gender = txtGender.getText().trim();
            user.updateProfile(name, gender, email, phone);

            
            if (user.updateInFile(oldRawLine)) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new Exception("Failed to update profile");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        private void changePassword() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        ChangePasswordDialog dialog = new ChangePasswordDialog(parent);
        dialog.setVisible(true);
    }
            @Override
    protected boolean validateInput() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return false;
        }
        
        return isValidName(name) && isValidEmail(email) && isValidPhone(phone);
    }
    

    private class ChangePasswordDialog extends BaseDialog {
        
        
        private JPasswordField txtCurrent;
        private JPasswordField txtNew;
        private JPasswordField txtConfirm;
        private JLabel lblError;
        private JButton btnChange;
        
        public ChangePasswordDialog(Window parent) {
            super(parent, "Change Password");  
            initializeComponents(); 
            setupRealTimeValidation();
            setupEnterNavigation();
            
        }
        
        @Override
        protected void initializeComponents() {
            setSize(500, 430);
            setLocationRelativeTo(EditProfilePanel.this);
            setResizable(false);
            setLayout(new BorderLayout());
            
        
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(new Color(59, 130, 246));
            headerPanel.setPreferredSize(new Dimension(500, 60));
            headerPanel.setLayout(new AbsoluteLayout());
            
            JLabel lblTitle = new JLabel("Change Password");
            lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
            lblTitle.setForeground(Color.WHITE);
            headerPanel.add(lblTitle, new AbsoluteConstraints(30, 15, 400, 30));
            
            add(headerPanel, BorderLayout.NORTH);
        
            JPanel bodyPanel = new JPanel();
            bodyPanel.setBackground(new Color(243, 244, 246));
            bodyPanel.setLayout(new AbsoluteLayout());
            
       
            JLabel lblCurrent = new JLabel("Current Password:");
            lblCurrent.setFont(new Font("Arial", Font.BOLD, 14));
            lblCurrent.setForeground(new Color(55, 65, 81));
            bodyPanel.add(lblCurrent, new AbsoluteConstraints(30, 25, 180, 25));
            
            txtCurrent = new JPasswordField();
            txtCurrent.setFont(new Font("Arial", Font.PLAIN, 14));
            txtCurrent.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
            bodyPanel.add(txtCurrent, new AbsoluteConstraints(30, 55, 440, 38));
            
           
            JLabel lblNew = new JLabel("New Password:");
            lblNew.setFont(new Font("Arial", Font.BOLD, 14));
            lblNew.setForeground(new Color(55, 65, 81));
            bodyPanel.add(lblNew, new AbsoluteConstraints(30, 110, 180, 25));
            
            txtNew = new JPasswordField();
            txtNew.setFont(new Font("Arial", Font.PLAIN, 14));
            txtNew.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
            bodyPanel.add(txtNew, new AbsoluteConstraints(30, 140, 440, 38));
     
            JLabel lblConfirm = new JLabel("Confirm Password:");
            lblConfirm.setFont(new Font("Arial", Font.BOLD, 14));
            lblConfirm.setForeground(new Color(55, 65, 81));
            bodyPanel.add(lblConfirm, new AbsoluteConstraints(30, 195, 180, 25));
            
            txtConfirm = new JPasswordField();
            txtConfirm.setFont(new Font("Arial", Font.PLAIN, 14));
            txtConfirm.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
            bodyPanel.add(txtConfirm, new AbsoluteConstraints(30, 225, 440, 38));
            
          
            lblError = new JLabel("");
            lblError.setFont(new Font("Arial", Font.BOLD, 13));
            lblError.setForeground(new Color(239, 68, 68));
            bodyPanel.add(lblError, new AbsoluteConstraints(30, 270, 440, 25));
            
            add(bodyPanel, BorderLayout.CENTER);
            
          
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(59, 130, 246));
            buttonPanel.setPreferredSize(new Dimension(500, 55));
            buttonPanel.setLayout(new AbsoluteLayout());
            
            btnChange = new JButton("Change Password");
            btnChange.setBackground(new Color(16, 185, 129));
            btnChange.setForeground(Color.WHITE);
            btnChange.setFont(new Font("Arial", Font.BOLD, 14));
            btnChange.setFocusPainted(false);
            btnChange.setBorder(BorderFactory.createEmptyBorder());
            btnChange.addActionListener(e -> submitChange());
            buttonPanel.add(btnChange, new AbsoluteConstraints(200, 12, 180, 35));
            
            JButton btnCancel = new JButton("Cancel");
            btnCancel.setBackground(new Color(156, 163, 175));
            btnCancel.setForeground(Color.WHITE);
            btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
            btnCancel.setFocusPainted(false);
            btnCancel.setBorder(BorderFactory.createEmptyBorder());
            btnCancel.addActionListener(e -> dispose());
            buttonPanel.add(btnCancel, new AbsoluteConstraints(400, 12, 80, 35));
            
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        
        private void setupEnterNavigation() {
    JComponent[] fields = {
        txtCurrent, txtNew, txtConfirm, btnChange
    };

    for (int i = 0; i < fields.length - 1; i++) {
        JComponent current = fields[i];
        JComponent next = fields[i + 1];

        current.getInputMap(JComponent.WHEN_FOCUSED)
               .put(KeyStroke.getKeyStroke("ENTER"), "moveNext");

        current.getActionMap()
               .put("moveNext", new AbstractAction() {
                   @Override
                   public void actionPerformed(java.awt.event.ActionEvent e) {
                       next.requestFocusInWindow();
                   }
               });
    }
}

        
        private void setupRealTimeValidation() {
            txtNew.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String pass = new String(txtNew.getPassword());
                    if (pass.isEmpty()) {
                        setError(txtNew, false);
                        lblError.setText("");
                    } else if (pass.length() < 6) {
                        setError(txtNew, true);
                        lblError.setText("Password must be at least 6 characters");
                    } else {
                        setError(txtNew, false);
                        lblError.setText("");
                    }
                   
                    if (new String(txtConfirm.getPassword()).length() > 0) {
                        validateConfirm();
                    }
                }
            });
            
            txtConfirm.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    validateConfirm();
                }
            });
        }
        
        private void validateConfirm() {
            String newPass = new String(txtNew.getPassword());
            String confirm = new String(txtConfirm.getPassword());
            if (confirm.isEmpty()) {
                setError(txtConfirm, false);
                lblError.setText("");
            } else if (!confirm.equals(newPass)) {
                setError(txtConfirm, true);
                lblError.setText("Passwords do not match");
            } else {
                setError(txtConfirm, false);
                lblError.setText("");
            }
        }
        

        
                @Override
        protected void submitForm() {
            submitChange();
        }
        
        @Override
        protected boolean validateInput() {
            String current = new String(txtCurrent.getPassword());
            String newPass = new String(txtNew.getPassword());
            String confirm = new String(txtConfirm.getPassword());
            
            if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                return false;
            }
            
            if (!current.equals(EditProfilePanel.this.user.getPassword())) {
                return false;
            }
            
            if (newPass.length() < 6) {
                return false;
            }
            
            return newPass.equals(confirm);
        }
        
        private void submitChange() {
            String current = new String(txtCurrent.getPassword());
            String newPass = new String(txtNew.getPassword());
            String confirm = new String(txtConfirm.getPassword());
            
           
            if (current.isEmpty()) {
                setError(txtCurrent, true);
                lblError.setText("Please enter your current password");
                txtCurrent.requestFocus();
                return;
            }
            if (!current.equals(user.getPassword())) {
                setError(txtCurrent, true);
                lblError.setText("Current password is incorrect");
                txtCurrent.requestFocus();
                return;
            }
            setError(txtCurrent, false);
            
           
            if (newPass.isEmpty()) {
                setError(txtNew, true);
                lblError.setText("Please enter a new password");
                txtNew.requestFocus();
                return;
            }
            if (newPass.length() < 6) {
                setError(txtNew, true);
                lblError.setText("Password must be at least 6 characters");
                txtNew.requestFocus();
                return;
            }
            setError(txtNew, false);
            
          
            if (!newPass.equals(confirm)) {
                setError(txtConfirm, true);
                lblError.setText("Passwords do not match");
                txtConfirm.requestFocus();
                return;
            }
            setError(txtConfirm, false);
            lblError.setText("");
            
            
            String oldRawLine = user.findRawLine();
            user.setPassword(newPass);
            
            if (user.updateInFile(oldRawLine)) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                lblError.setText("Failed to update password. Please try again.");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnChangePassword = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        lblFullName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblGender = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtName1 = new javax.swing.JTextField();
        txtGender = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1200, 620));
        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(59, 130, 246));
        headerPanel.setPreferredSize(new java.awt.Dimension(500, 80));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Edit Profile");
        headerPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 440, 40));

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        buttonPanel.setBackground(new java.awt.Color(59, 130, 246));
        buttonPanel.setPreferredSize(new java.awt.Dimension(500, 40));
        buttonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSave.setBackground(new java.awt.Color(16, 185, 129));
        btnSave.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        buttonPanel.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 5, 130, 30));

        btnChangePassword.setBackground(new java.awt.Color(245, 158, 11));
        btnChangePassword.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnChangePassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangePassword.setText("CHANGE PASSWORD");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        buttonPanel.add(btnChangePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 5, 190, 30));

        add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        panel.setBackground(new java.awt.Color(243, 244, 246));
        panel.setPreferredSize(new java.awt.Dimension(500, 80));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInfo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(107, 114, 128));
        lblInfo.setText("User ID: _____ & Username: _____  & Age: _____");
        panel.add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, 460, 25));

        lblFullName.setBackground(new java.awt.Color(55, 65, 81));
        lblFullName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblFullName.setForeground(new java.awt.Color(55, 65, 81));
        lblFullName.setText("Full Name:");
        panel.add(lblFullName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 100, 25));

        txtName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        panel.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 700, 35));

        lblGender.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblGender.setForeground(new java.awt.Color(55, 65, 81));
        lblGender.setText("Gender:");
        panel.add(lblGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 100, 25));

        lblEmail.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(55, 65, 81));
        lblEmail.setText("Email:");
        panel.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 100, 25));

        txtEmail.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        panel.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 700, 35));

        lblPhone.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblPhone.setForeground(new java.awt.Color(55, 65, 81));
        lblPhone.setText("Phone:");
        panel.add(lblPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 100, 25));

        txtPhone.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPhone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        panel.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 700, 35));

        txtName1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        panel.add(txtName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 700, 35));

        txtGender.setEditable(false);
        panel.add(txtGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 700, 35));

        add(panel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveProfile();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        changePassword();
    }//GEN-LAST:event_btnChangePasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGender;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtName1;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
