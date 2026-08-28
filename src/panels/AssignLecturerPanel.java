/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import javax.swing.JOptionPane;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hussain Alkhaldi
 */
public class AssignLecturerPanel extends javax.swing.JPanel {


    public AssignLecturerPanel() {
        initComponents();
        loadData();
    }
    private void loadData() {
        List<String> users = FileManager.readFile(FileManager.USERS_FILE);
        
        cmbLeader.removeAllItems();
        cmbLecturer.removeAllItems();
        
        for (String line : users) {
            String[] parts = line.split("\\|");
            if (parts.length >= 9) {
                String userID = parts[0];
                String name = parts[3];
                String type = parts[8];
                
                if (type.equalsIgnoreCase("academic leader")) {
                    cmbLeader.addItem(userID + " - " + name);
                } else if (type.equalsIgnoreCase("lecturer")) {
                    cmbLecturer.addItem(userID + " - " + name);
                }
            }
        }
    }
    
    private void assignLecturer() {
    if (cmbLeader.getSelectedItem() == null || cmbLecturer.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Please select both leader and lecturer", 
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String leaderID = ((String) cmbLeader.getSelectedItem()).split(" - ")[0];
    String lecturerID = ((String) cmbLecturer.getSelectedItem()).split(" - ")[0];
    
    List<String> assignments = FileManager.readFile(FileManager.ASSIGNMENTS_FILE);
    List<String> updated = new ArrayList<>();
    boolean lecturerExists = false;
    boolean alreadyAssigned = false;

    for (String line : assignments) {
        String[] parts = line.split("\\|");
        if (parts.length >= 2 && parts[1].equals(lecturerID)) {
            lecturerExists = true;
            if (parts[0].equals(leaderID)) {
                alreadyAssigned = true;
            }
            
            updated.add(leaderID + "|" + lecturerID);
        } else {
            updated.add(line);
        }
    }
    
    if (alreadyAssigned) {
        JOptionPane.showMessageDialog(this, 
            "This assignment already exists", 
            "Info", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    if (lecturerExists) {
        
        FileManager.writeFile(FileManager.ASSIGNMENTS_FILE, updated);
        JOptionPane.showMessageDialog(this, 
            "Lecturer assignment UPDATED successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    } else {
      
        String assignment = leaderID + "|" + lecturerID;
        if (FileManager.appendToFile(FileManager.ASSIGNMENTS_FILE, assignment)) {
            JOptionPane.showMessageDialog(this, 
                "Lecturer ASSIGNED successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to assign lecturer", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    displayAssignments();
}
    
    private void displayAssignments() {
        StringBuilder sb = new StringBuilder();
        List<String> assignments = FileManager.readFile(FileManager.ASSIGNMENTS_FILE);
        List<String> users = FileManager.readFile(FileManager.USERS_FILE);
        
        if (assignments.isEmpty()) {
            sb.append("No assignments found.");
        } else {
            for (String line : assignments) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String leaderID = parts[0];
                    String lecturerID = parts[1];
                    
                    String leaderName = getUserName(users, leaderID);
                    String lecturerName = getUserName(users, lecturerID);
                    
                    sb.append("Leader: ").append(leaderName).append(" (").append(leaderID).append(")\n");
                    sb.append("  → Lecturer: ").append(lecturerName).append(" (").append(lecturerID).append(")\n\n");
                }
            }
        }
        
        jTextArea1.setText(sb.toString());
    }
    
private void removeAssignment() {
    if (cmbLeader.getSelectedItem() == null || cmbLecturer.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, 
            "Please select both leader and lecturer", 
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String leaderID = ((String) cmbLeader.getSelectedItem()).split(" - ")[0];
    String leaderName = ((String) cmbLeader.getSelectedItem()).split(" - ")[1];
    String lecturerID = ((String) cmbLecturer.getSelectedItem()).split(" - ")[0];
    String lecturerName = ((String) cmbLecturer.getSelectedItem()).split(" - ")[1];
    
  
    List<String> assignments = FileManager.readFile(FileManager.ASSIGNMENTS_FILE);
    boolean found = false;
    String assignmentToRemove = null;
    
    for (String line : assignments) {
        String[] parts = line.split("\\|");

        if (parts.length >= 2 && parts[0].equals(leaderID) && parts[1].equals(lecturerID)) {
            found = true;
            assignmentToRemove = line;
            break;
        }
    }
    
    if (!found) {
        JOptionPane.showMessageDialog(this, 
            "No assignment found between:\n" +
            "Leader: " + leaderName + " (" + leaderID + ")\n" +
            "Lecturer: " + lecturerName + " (" + lecturerID + ")", 
            "Info", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to remove this assignment?\n\n" +
        "Leader: " + leaderName + " (" + leaderID + ")\n" +
        "Lecturer: " + lecturerName + " (" + lecturerID + ")",
        "Confirm Removal", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        if (FileManager.deleteLine(FileManager.ASSIGNMENTS_FILE, assignmentToRemove)) {
            JOptionPane.showMessageDialog(this, 
                "Assignment removed successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            displayAssignments();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to remove assignment", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private String getUserName(List<String> users, String userID) {
        for (String line : users) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && parts[0].equals(userID)) {
                return parts[3];
            }
        }
        return "Unknown";
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSelectLeader = new javax.swing.JLabel();
        cmbLeader = new javax.swing.JComboBox<>();
        lblSelectLecturer = new javax.swing.JLabel();
        cmbLecturer = new javax.swing.JComboBox<>();
        assignmentsPanel = new javax.swing.JPanel();
        txtAssignments = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        btnAssign = new javax.swing.JButton();
        btnViewAssignments = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1200, 620));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(124, 58, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(650, 80));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Assign Lecturer to Academic Leader");
        jPanel1.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 590, 40));

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(243, 244, 246));
        jPanel2.setPreferredSize(new java.awt.Dimension(650, 120));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSelectLeader.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblSelectLeader.setForeground(new java.awt.Color(55, 65, 81));
        lblSelectLeader.setText("Select Leader:");
        jPanel2.add(lblSelectLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 150, 30));

        cmbLeader.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jPanel2.add(cmbLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 390, 30));

        lblSelectLecturer.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblSelectLecturer.setForeground(new java.awt.Color(55, 65, 81));
        lblSelectLecturer.setText("Select Lecturer:");
        jPanel2.add(lblSelectLecturer, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 150, -1));

        cmbLecturer.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jPanel2.add(cmbLecturer, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 390, 30));

        assignmentsPanel.setBackground(new java.awt.Color(243, 244, 246));
        assignmentsPanel.setPreferredSize(new java.awt.Dimension(970, 260));
        assignmentsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(40);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea1.setRows(10);
        jTextArea1.setPreferredSize(new java.awt.Dimension(700, 600));
        txtAssignments.setViewportView(jTextArea1);

        assignmentsPanel.add(txtAssignments, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 710, 215));

        jPanel2.add(assignmentsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 980, 250));

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(243, 244, 246));
        jPanel3.setPreferredSize(new java.awt.Dimension(650, 60));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAssign.setBackground(new java.awt.Color(16, 185, 129));
        btnAssign.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnAssign.setForeground(new java.awt.Color(255, 255, 255));
        btnAssign.setText("ASSIGN / UPDATE");
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });
        jPanel3.add(btnAssign, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 210, 30));

        btnViewAssignments.setBackground(new java.awt.Color(59, 130, 246));
        btnViewAssignments.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnViewAssignments.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAssignments.setText("REFRESH");
        btnViewAssignments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAssignmentsActionPerformed(evt);
            }
        });
        jPanel3.add(btnViewAssignments, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 200, 30));

        btnRemove.setBackground(new java.awt.Color(239, 68, 68));
        btnRemove.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setText("REMOVE ASSIGNMENT");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jPanel3.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 200, 30));

        add(jPanel3, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignActionPerformed
        // TODO add your handling code here:
        assignLecturer();
    }//GEN-LAST:event_btnAssignActionPerformed

    private void btnViewAssignmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAssignmentsActionPerformed
        // TODO add your handling code here:
        displayAssignments();
    }//GEN-LAST:event_btnViewAssignmentsActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeAssignment();
    }//GEN-LAST:event_btnRemoveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel assignmentsPanel;
    private javax.swing.JButton btnAssign;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnViewAssignments;
    private javax.swing.JComboBox<String> cmbLeader;
    private javax.swing.JComboBox<String> cmbLecturer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblSelectLeader;
    private javax.swing.JLabel lblSelectLecturer;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane txtAssignments;
    // End of variables declaration//GEN-END:variables
}
