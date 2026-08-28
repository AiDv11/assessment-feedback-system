/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;


import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import utils.AuthManager;
import utils.FileManager;
import models.User;
import interfaces.Creatable;




/**
 *
 * @author Hussain Alkhaldi
 */
public class CreateUserPanel extends BaseFormPanel implements Creatable {

    

    public CreateUserPanel() {
        initComponents();
        setupEnterKeyNavigation();
        setupRealTimeValidation();
        populateDateComboBoxes();
    }
    
    public void createNew() {
        try {
            if (!validateInput()) {
                return;
            }
            
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String name = txtName.getText().trim();
            String gender = (String) cmbGender.getSelectedItem();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String dateOfBirth = buildDateOfBirth();
            String userType = ((String) cmbUserType.getSelectedItem()).toLowerCase();
            
            String prefix = getUserTypePrefix(userType);
            String userID = FileManager.generateNextID(FileManager.USERS_FILE, prefix);
        
            User newUser = AuthManager.createNewUser(userID, username, password, 
                name, gender, email, phone, dateOfBirth, userType);
            
            if (newUser.saveToFile()) {
                JOptionPane.showMessageDialog(this,
                    "User created successfully!\nUser ID: " + userID,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                throw new Exception("Failed to save user data to file");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error creating user: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     @Override
    protected boolean validateInput() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String dateOfBirth = buildDateOfBirth();
        
    
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || 
            email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
       
        if (AuthManager.usernameExists(username)) {
            setFieldError(txtUsername, true);
            JOptionPane.showMessageDialog(
                this,
                "Username already exists. Please choose another username.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
   
        if (!password.equals(confirmPassword)) {
            setFieldError(txtConfirmPassword, true);
            JOptionPane.showMessageDialog(
                this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
    
        if (!isValidPassword(password)) {  
            setFieldError(txtPassword, true);
            JOptionPane.showMessageDialog(
                this,
                "Password must be at least 6 characters long",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
       
        if (!isValidName(name)) {  
            setFieldError(txtName, true);
            JOptionPane.showMessageDialog(
                this,
                "Name must contain only letters and spaces",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
        
        if (!isValidEmail(email)) {  
            setFieldError(txtEmail, true);
            JOptionPane.showMessageDialog(
                this,
                "Invalid email format. Email must include @ and domain extension (e.g., .com, .org)",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
       
        if (!isValidPhone(phone)) {  
            setFieldError(txtPhone, true);
            JOptionPane.showMessageDialog(
                this,
                "Phone number must be 10-15 digits",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        
        
if (dateOfBirth.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "Please select your date of birth",
        "Validation Error", JOptionPane.WARNING_MESSAGE);
    return false;
}

try {
    String[] parts = dateOfBirth.split("-");
    int year = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);
    int day = Integer.parseInt(parts[2]);
    
    if (year < 1900 || year > java.time.LocalDate.now().getYear()) {
        throw new Exception("Invalid year");
    }
    if (month < 1 || month > 12) {
        throw new Exception("Invalid month");
    }
    if (day < 1 || day > 31) {
        throw new Exception("Invalid day");
    }
    
    java.time.LocalDate birthDate = java.time.LocalDate.of(year, month, day);
    if (birthDate.isAfter(java.time.LocalDate.now())) {
        throw new Exception("Date cannot be in the future");
    }
    
   
    int calculatedAge = java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    if (calculatedAge < 18) {
        JOptionPane.showMessageDialog(this,
            "User must be at least 18 years old. Current age: " + calculatedAge,
            "Validation Error", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
} catch (Exception e) {

    JOptionPane.showMessageDialog(this,
        "Invalid date: " + e.getMessage(),
        "Validation Error", JOptionPane.WARNING_MESSAGE);
    return false;
}
        
        return true;
    }
    
    private String getUserTypePrefix(String userType) {
        switch (userType) {
            case "admin": return "A";
            case "academic leader": return "L";
            case "lecturer": return "T";
            case "student": return "S";
            default: return "U";
        }
    }
    
private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbYear.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(0);
        cmbDay.setSelectedIndex(0);
        cmbGender.setSelectedIndex(0);
        cmbUserType.setSelectedIndex(0);
        
       
        setFieldError(txtUsername, false);
        setFieldError(txtPassword, false);
        setFieldError(txtConfirmPassword, false);
        setFieldError(txtName, false);
        setFieldError(txtEmail, false);
        setFieldError(txtPhone, false);
        populateDateComboBoxes();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        userContentPanel = new javax.swing.JPanel();
        txtPhone = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        cmbUserType = new javax.swing.JComboBox<>();
        lblUserType = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        lblDateOfBirth = new javax.swing.JLabel();
        cmbGender = new javax.swing.JComboBox<>();
        lblGender = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblFullName = new javax.swing.JLabel();
        txtConfirmPassword = new javax.swing.JPasswordField();
        lblConfirmPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbDay = new javax.swing.JComboBox<>();
        cmbMonth = new javax.swing.JComboBox<>();
        cmbYear = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(236, 240, 241));
        setPreferredSize(new java.awt.Dimension(1200, 620));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(550, 660));
        jPanel2.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(124, 58, 237));
        headerPanel.setMinimumSize(new java.awt.Dimension(950, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(1200, 80));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFormTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblFormTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblFormTitle.setText("Create New User");
        headerPanel.add(lblFormTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 490, 40));

        jPanel2.add(headerPanel, java.awt.BorderLayout.NORTH);

        userContentPanel.setBackground(new java.awt.Color(255, 255, 255));
        userContentPanel.setPreferredSize(new java.awt.Dimension(1200, 540));
        userContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPhone.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtPhone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        userContentPanel.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 280, 40));

        lblUsername.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(55, 65, 81));
        lblUsername.setText("Username:");
        userContentPanel.add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 280, 30));

        txtUsername.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        userContentPanel.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 280, 40));

        cmbUserType.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cmbUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Academic Leader", "Lecturer", "Student" }));
        cmbUserType.setSelectedIndex(-1);
        userContentPanel.add(cmbUserType, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 410, 580, 40));

        lblUserType.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblUserType.setForeground(new java.awt.Color(55, 65, 81));
        lblUserType.setText("User Type:");
        userContentPanel.add(lblUserType, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, 580, 30));

        lblPhone.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblPhone.setForeground(new java.awt.Color(55, 65, 81));
        lblPhone.setText("Phone Number:");
        userContentPanel.add(lblPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 280, 30));

        txtEmail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        userContentPanel.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 880, 40));

        lblEmail.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(55, 65, 81));
        lblEmail.setText("Email:");
        userContentPanel.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 880, 30));

        lblDateOfBirth.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblDateOfBirth.setForeground(new java.awt.Color(55, 65, 81));
        lblDateOfBirth.setText("Date of birth:");
        lblDateOfBirth.setToolTipText("");
        lblDateOfBirth.setPreferredSize(new java.awt.Dimension(300, 20));
        userContentPanel.add(lblDateOfBirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 280, 30));

        cmbGender.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        cmbGender.setSelectedIndex(-1);
        cmbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGenderActionPerformed(evt);
            }
        });
        userContentPanel.add(cmbGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 280, 40));

        lblGender.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblGender.setForeground(new java.awt.Color(55, 65, 81));
        lblGender.setText("Gender:");
        userContentPanel.add(lblGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 280, 30));

        txtName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        userContentPanel.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 880, 40));

        lblFullName.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblFullName.setForeground(new java.awt.Color(55, 65, 81));
        lblFullName.setText("Full Name:");
        userContentPanel.add(lblFullName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 880, 30));

        txtConfirmPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtConfirmPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        userContentPanel.add(txtConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 280, 40));

        lblConfirmPassword.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblConfirmPassword.setForeground(new java.awt.Color(55, 65, 81));
        lblConfirmPassword.setText("Confirm Password:");
        userContentPanel.add(lblConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 280, 20));

        txtPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        userContentPanel.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 280, 40));

        lblPassword.setFont(new java.awt.Font("Arial", 1, 19)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(55, 65, 81));
        lblPassword.setText("Password:");
        userContentPanel.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 280, 30));

        btnCreate.setBackground(new java.awt.Color(16, 185, 129));
        btnCreate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnCreate.setForeground(new java.awt.Color(255, 255, 255));
        btnCreate.setText("Create User");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        userContentPanel.add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 470, 170, 40));

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(107, 104, 128));
        btnCancel.setText("RESET");
        btnCancel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        userContentPanel.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 120, 40));

        userContentPanel.add(cmbDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 200, 40));

        userContentPanel.add(cmbMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 200, 40));

        userContentPanel.add(cmbYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 230, 200, 40));

        jPanel2.add(userContentPanel, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

        
    
    private void populateDateComboBoxes() {
       
        int currentYear = java.time.LocalDate.now().getYear();
        for (int year = currentYear; year >= 1900; year--) {
            cmbYear.addItem(String.valueOf(year));
        }
        
        
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            cmbMonth.addItem(month);
        }
        
       
        for (int day = 1; day <= 31; day++) {
            cmbDay.addItem(String.valueOf(day));
        }
    }
    
    private String buildDateOfBirth() {
        if (cmbYear.getSelectedItem() == null || 
            cmbMonth.getSelectedItem() == null || 
            cmbDay.getSelectedItem() == null) {
            return "";
        }
        
        String year = (String) cmbYear.getSelectedItem();
        int monthIndex = cmbMonth.getSelectedIndex() + 1;
        String month = String.format("%02d", monthIndex);
        String day = String.format("%02d", Integer.parseInt((String) cmbDay.getSelectedItem()));
        
        return year + "-" + month + "-" + day;
    }
    

    



    private void setupEnterKeyNavigation() {
Component[] components = {
    txtUsername, txtPassword, txtConfirmPassword, txtName,
    cmbGender, cmbYear, cmbMonth, cmbDay, txtEmail, txtPhone, cmbUserType, btnCreate
};
        
        
        for (int i = 0; i < components.length - 1; i++) {
            final int index = i;
            Component current = components[i];
            
            if (current instanceof JTextField) {
                ((JTextField) current).addActionListener(e -> components[index + 1].requestFocus());
            } else if (current instanceof JPasswordField) {
                ((JPasswordField) current).addActionListener(e -> components[index + 1].requestFocus());
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
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateUsernameField();
            }
        });
        
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validatePasswordField();
            }
        });
        
        txtConfirmPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateConfirmPasswordField();
            }
        });
        
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

    private void validateUsernameField() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            setFieldError(txtUsername, false);
        } else if (AuthManager.usernameExists(username)) {
            setFieldError(txtUsername, true);
        } else {
            setFieldError(txtUsername, false);
        }
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
    
    private void validatePasswordField() {
        String password = new String(txtPassword.getPassword());
        if (!password.isEmpty()) {
            setFieldError(txtPassword, !isValidPassword(password)); 
        } else {
            setFieldError(txtPassword, false);
        }
    }
    
    private void validateConfirmPasswordField() {
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        if (!confirmPassword.isEmpty()) {
            setFieldError(txtConfirmPassword, !password.equals(confirmPassword));
        } else {
            setFieldError(txtConfirmPassword, false);
        }
    }
    

                                  

 
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        clearFields();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        createNew();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void cmbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGenderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCreate;
    private javax.swing.JComboBox<String> cmbDay;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbUserType;
    private javax.swing.JComboBox<String> cmbYear;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblConfirmPassword;
    private javax.swing.JLabel lblDateOfBirth;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblUserType;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPanel userContentPanel;
    // End of variables declaration//GEN-END:variables
}
