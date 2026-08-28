/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

/**
 *
 * @author ali66
 */
import models.Lecturer;
import models.Assessment;
import interfaces.Editor;
import utils.FileManager;
import utils.AuthManager;
import models.User;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProvideFeedbackPanel extends javax.swing.JPanel implements Editor {
    
    private Lecturer lecturer;
    private DefaultTableModel tableModel;
    
    public ProvideFeedbackPanel(Lecturer lecturer) {
        this.lecturer = lecturer;
        initComponents();
        setupTable();
        loadData();
    }
    
    private void setupTable() {
        tableModel = (DefaultTableModel) tblFeedbackHistory.getModel();
    }
    
    @Override
    public void loadData() {
        cmbStudent.removeAllItems();
        
        List<String> users = FileManager.readFile("data/users.txt");
        for (String line : users) {
            String[] parts = line.split("\\|");
            if (parts.length >= 9 && parts[8].equalsIgnoreCase("student")) {
                cmbStudent.addItem(parts[0] + " - " + parts[3]);
            }
        }
        
        if (cmbStudent.getItemCount() == 0) {
            cmbStudent.addItem("No students found");
        }
    }
    
    private void loadAssessmentsForStudent() {
        cmbAssessment.removeAllItems();
        
        if (cmbStudent.getSelectedItem() == null) return;
        if (cmbStudent.getSelectedItem().toString().equals("No students found")) return;
        
        List<String> assessments = lecturer.getMyAssessments();
        
        if (assessments.isEmpty()) {
            cmbAssessment.addItem("No assessments created");
        } else {
            for (String line : assessments) {
                Assessment assessment = Assessment.fromFileString(line);
                if (assessment != null) {
                    cmbAssessment.addItem(assessment.getAssessmentID() + " - " + 
                        assessment.getAssessmentName());
                }
            }
        }
    }
    
    private void loadMarksInfo() {
        if (cmbStudent.getSelectedItem() == null || cmbAssessment.getSelectedItem() == null) {
            return;
        }
        
        String studentID = cmbStudent.getSelectedItem().toString().split(" - ")[0];
        String assessmentID = cmbAssessment.getSelectedItem().toString().split(" - ")[0];
        
        List<String> marks = FileManager.readFile("data/marks.txt");
        boolean found = false;
        
        for (String line : marks) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5 && 
                parts[1].equals(studentID) && 
                parts[2].equals(assessmentID)) {
                lblMarksInfo.setText("Marks: " + parts[3] + " | Grade: " + parts[4]);
                found = true;
                break;
            }
        }
        
        if (!found) {
            lblMarksInfo.setText("No marks entered yet");
        }
        
        loadExistingFeedback(studentID, assessmentID);
    }
    
    private void loadExistingFeedback(String studentID, String assessmentID) {
        List<String> feedbacks = FileManager.readFile("data/feedback.txt");
        
        for (String line : feedbacks) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 &&
                parts[1].equals(studentID) &&
                parts[2].equals(assessmentID) &&
                parts[3].equals(lecturer.getUserID())) {
                txtFeedback.setText(parts[4]);
                break;
            }
        }
    }
    
    private void showFeedbackHistory() {
        tableModel.setRowCount(0);
        
        List<String> feedbacks = FileManager.readFile("data/feedback.txt");
        
        for (String line : feedbacks) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 && parts[3].equals(lecturer.getUserID())) {
                String studentName = getStudentName(parts[1]);
                String feedbackText = parts[4];
                if (feedbackText.length() > 50) {
                    feedbackText = feedbackText.substring(0, 47) + "...";
                }
                
                tableModel.addRow(new Object[]{
                    parts[0], studentName, parts[2], feedbackText, parts[5]
                });
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No feedback history", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private String getStudentName(String studentID) {
        User student = AuthManager.findUserByID(studentID);
        return student != null ? student.getName() : studentID;
    }
    
    @Override
    public void saveChanges() {
        if (cmbStudent.getSelectedItem() == null || 
            cmbStudent.getSelectedItem().toString().equals("No students found")) {
            JOptionPane.showMessageDialog(this, "Select student!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cmbAssessment.getSelectedItem() == null ||
            cmbAssessment.getSelectedItem().toString().equals("No assessments created")) {
            JOptionPane.showMessageDialog(this, "Select assessment!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String feedbackText = txtFeedback.getText().trim();
        
        if (feedbackText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter feedback!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (feedbackText.length() < 20) {
            JOptionPane.showMessageDialog(this, 
                "Feedback must be at least 20 characters!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String studentID = cmbStudent.getSelectedItem().toString().split(" - ")[0];
        String assessmentID = cmbAssessment.getSelectedItem().toString().split(" - ")[0];
        
        lecturer.provideFeedback(studentID, assessmentID, feedbackText);
        
        JOptionPane.showMessageDialog(this, "Feedback submitted!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        txtFeedback.setText("");
        lblMarksInfo.setText("Select student and assessment");
        showFeedbackHistory();
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        selectionPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblMarksInfo = new javax.swing.JLabel();
        cmbStudent = new javax.swing.JComboBox<>();
        cmbAssessment = new javax.swing.JComboBox<>();
        feedbackPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtFeedback = new javax.swing.JTextArea();
        btnPanel = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();
        btnViewHistory = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        historyPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFeedbackHistory = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        topPanel.setBackground(new java.awt.Color(29, 78, 216));

        jLabel1.setBackground(new java.awt.Color(255, 51, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Provide Feedback");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(765, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        add(topPanel, java.awt.BorderLayout.PAGE_START);

        mainPanel.setBackground(new java.awt.Color(245, 247, 250));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        selectionPanel.setBackground(new java.awt.Color(245, 247, 250));
        selectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Select Student & Assessment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        selectionPanel.setMaximumSize(new java.awt.Dimension(850, 200));
        selectionPanel.setPreferredSize(new java.awt.Dimension(850, 150));
        selectionPanel.setLayout(new java.awt.GridBagLayout());

        jLabel2.setBackground(new java.awt.Color(30, 41, 59));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Student:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        selectionPanel.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Assessment:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        selectionPanel.add(jLabel3, gridBagConstraints);

        lblMarksInfo.setForeground(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        selectionPanel.add(lblMarksInfo, gridBagConstraints);

        cmbStudent.setPreferredSize(new java.awt.Dimension(300, 30));
        cmbStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStudentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        selectionPanel.add(cmbStudent, gridBagConstraints);

        cmbAssessment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose...", "Assignment", "Quiz", "Test", "Midterm Exam", "Final Exam", "Project", "Presentation" }));
        cmbAssessment.setPreferredSize(new java.awt.Dimension(300, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        selectionPanel.add(cmbAssessment, gridBagConstraints);

        mainPanel.add(selectionPanel);

        feedbackPanel1.setBackground(new java.awt.Color(245, 247, 250));
        feedbackPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Write Feedback", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        feedbackPanel1.setMaximumSize(new java.awt.Dimension(850, 200));
        feedbackPanel1.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Enter detailed feedback (minimum 20 characters):");
        feedbackPanel1.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        txtFeedback.setBackground(new java.awt.Color(153, 153, 153));
        txtFeedback.setColumns(50);
        txtFeedback.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        txtFeedback.setForeground(new java.awt.Color(255, 255, 255));
        txtFeedback.setLineWrap(true);
        txtFeedback.setRows(6);
        txtFeedback.setWrapStyleWord(true);
        txtFeedback.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 195, 199))));
        jScrollPane1.setViewportView(txtFeedback);

        feedbackPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        mainPanel.add(feedbackPanel1);

        btnPanel.setBackground(new java.awt.Color(245, 247, 250));
        btnPanel.setMaximumSize(new java.awt.Dimension(850, 50));
        btnPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        btnSubmit.setBackground(new java.awt.Color(16, 185, 129));
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit Feedback");
        btnSubmit.setPreferredSize(new java.awt.Dimension(170, 35));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        btnPanel.add(btnSubmit);

        btnViewHistory.setBackground(new java.awt.Color(37, 99, 235));
        btnViewHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnViewHistory.setText("View Feedback History");
        btnViewHistory.setPreferredSize(new java.awt.Dimension(170, 35));
        btnViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewHistoryActionPerformed(evt);
            }
        });
        btnPanel.add(btnViewHistory);

        btnClear.setBackground(new java.awt.Color(30, 41, 59));
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.setPreferredSize(new java.awt.Dimension(170, 35));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        btnPanel.add(btnClear);

        mainPanel.add(btnPanel);

        historyPanel.setBackground(new java.awt.Color(29, 78, 216));
        historyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3), "Feedback History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        historyPanel.setLayout(new java.awt.BorderLayout());

        tblFeedbackHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID ", "Student", "Assessment", "Feedback", "Date"
            }
        ));
        tblFeedbackHistory.setRowHeight(25);
        jScrollPane2.setViewportView(tblFeedbackHistory);

        historyPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        mainPanel.add(historyPanel);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        saveChanges();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewHistoryActionPerformed
        showFeedbackHistory();
    }//GEN-LAST:event_btnViewHistoryActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtFeedback.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void cmbStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStudentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnViewHistory;
    private javax.swing.JComboBox<String> cmbAssessment;
    private javax.swing.JComboBox<String> cmbStudent;
    private javax.swing.JPanel feedbackPanel1;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMarksInfo;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel selectionPanel;
    private javax.swing.JTable tblFeedbackHistory;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextArea txtFeedback;
    // End of variables declaration//GEN-END:variables
}
