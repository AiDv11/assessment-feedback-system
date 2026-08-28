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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class KeyInMarksPanel extends javax.swing.JPanel implements Editor {
    
    private Lecturer lecturer;
    private String selectedAssessmentID;
    private int assessmentTotalMarks;
    private DefaultTableModel tableModel;
    
    public KeyInMarksPanel(Lecturer lecturer) {
        this.lecturer = lecturer;
        initComponents();
        setupTable();
        loadData();
        
   
         tblStudents.getSelectionModel().addListSelectionListener(e -> {
            btnClear.setEnabled(tblStudents.getSelectedRow() != -1);
        });
       
        cmbAssessment.addActionListener(e -> onAssessmentSelected());
    }
    
 private void setupTable() {

    tblStudents.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] { "Student ID", "Name", "Marks Obtained", "Grade", "Status" }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2; 
        }
    });

    tableModel = (DefaultTableModel) tblStudents.getModel();
}



    
    @Override
    public void loadData() {
        cmbAssessment.removeAllItems();
        List<String> assessments = lecturer.getMyAssessments();
        
        if (assessments.isEmpty()) {
            cmbAssessment.addItem("No assessments created");
        } else {
            for (String line : assessments) {
                Assessment assessment = Assessment.fromFileString(line);
                if (assessment != null) {
                    cmbAssessment.addItem(assessment.getAssessmentID() + " - " + 
                        assessment.getAssessmentName() + " (" + 
                        assessment.getTotalMarks() + " marks)");
                }
            }
        }
    }
    
   private void onAssessmentSelected() {
    if (cmbAssessment.getSelectedItem() == null) return;

    String selected = cmbAssessment.getSelectedItem().toString();
    if (selected.equals("No assessments created")) return;

   
    selectedAssessmentID = selected.split(" - ", 2)[0].trim();

   
    List<String> assessments = lecturer.getMyAssessments();
    for (String line : assessments) {
        Assessment assessment = Assessment.fromFileString(line);
        if (assessment != null && assessment.getAssessmentID().equals(selectedAssessmentID)) {
            assessmentTotalMarks = assessment.getTotalMarks();
            lblAssessmentInfo.setText(
                "Assessment: " + assessment.getAssessmentName() +
                " | Type: " + assessment.getAssessmentType() +
                " | Total Marks: " + assessmentTotalMarks
            );
            lblMaxMarks.setText("/" + assessmentTotalMarks); 
            break;
        }
    }


    loadStudentCombo();
    loadStudents();
    
}

   
   
       private void loadStudents() {
    tableModel.setRowCount(0);

    List<String> users = FileManager.readFile("data/users.txt");
    List<String> existingMarks = FileManager.readFile("data/marks.txt");

    for (String line : users) {
        if (line == null || line.trim().isEmpty()) continue;

        String[] parts = line.split("\\|");
        if (parts.length < 2) continue;

        String role = parts[parts.length - 1].trim();
        if (!role.equalsIgnoreCase("student")) continue;

        String studentID = parts[0].trim();
        String studentName = (parts.length > 3) ? parts[3].trim() : studentID;

        String mark = null;
        String grade = null;

        for (String markLine : existingMarks) {
            if (markLine == null || markLine.trim().isEmpty()) continue;

            String[] mp = markLine.split("\\|");
            if (mp.length < 5) continue;

            String mStudent = mp[1].trim();  
            String mAssess  = mp[2].trim();  

            if (mStudent.equals(studentID) && mAssess.equals(selectedAssessmentID)) {
                mark = mp[3].trim();         
                grade = mp[4].trim();      
            }
        }

        if (mark != null) {
            tableModel.addRow(new Object[]{ studentID, studentName, mark, grade, "Entered" });
        }
    }
}





        private void loadStudentCombo() {
        cmbStudentSelect.removeAllItems();

        List<String> users = FileManager.readFile("data/users.txt");
        for (String line : users) {
            if (line == null || line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 2) continue;

            String role = parts[parts.length - 1].trim();
            if (!role.equalsIgnoreCase("student")) continue;

            String studentID = parts[0].trim();
            String studentName = (parts.length > 3) ? parts[3].trim() : studentID;

            cmbStudentSelect.addItem(studentID + " - " + studentName);
        }

        if (cmbStudentSelect.getItemCount() == 0) {
            cmbStudentSelect.addItem("No students found");
        }
    }
    
        
                
        @Override
        public void saveChanges() {
        
        if (selectedAssessmentID == null) {
            if (tblStudents.isEditing()) {
            tblStudents.getCellEditor().stopCellEditing();
            }

            JOptionPane.showMessageDialog(this, "Select assessment first!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

                int saved = 0;
             
        String studentPick = (cmbStudentSelect.getSelectedItem() == null) ? "" : cmbStudentSelect.getSelectedItem().toString();

        if (!studentPick.isEmpty() && !studentPick.equals("No students found")) {

            String studentID = studentPick.split(" - ", 2)[0].trim();
            String marksText = txtMarks.getText().trim();

            if (!marksText.isEmpty()) {
                try {
                    double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > assessmentTotalMarks) {
                JOptionPane.showMessageDialog(this,
                        "Marks must be between 0 and " + assessmentTotalMarks,
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String grade = calculateGrade(marks);

            boolean ok = lecturer.enterMarks(studentID, selectedAssessmentID, marks, grade); 

            if (ok) {
           
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).toString().equals(studentID)) {
                        tableModel.setValueAt(marks, i, 2);
                        tableModel.setValueAt(grade, i, 3);
                        tableModel.setValueAt("Entered", i, 4);
                        break;
                    }
                }
                loadStudents();   
                clearForm();

                JOptionPane.showMessageDialog(this,
                        "Saved marks for " + studentID,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                return; 
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save marks (enterMarks returned false)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

          } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Marks must be a number!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
               return;
        }
         }
        }

        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String studentID = tableModel.getValueAt(i, 0).toString();
            Object marksObj = tableModel.getValueAt(i, 2);
            
            if (marksObj == null || marksObj.toString().trim().isEmpty()) continue;
            
            try {
                double marks = Double.parseDouble(marksObj.toString());
                
                if (marks < 0 || marks > assessmentTotalMarks) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid marks for " + studentID + "!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                if (lecturer.enterMarks(studentID, selectedAssessmentID, marks)) {
                    saved++;
                    String grade = calculateGrade(marks);
                    tableModel.setValueAt(grade, i, 3);
                    tableModel.setValueAt("Entered", i, 4);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid marks for " + studentID, 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
      
        if (saved > 0) {
            JOptionPane.showMessageDialog(this, "Saved " + saved + " marks!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
  }
    
    private String calculateGrade(double marks) {
        double percentage = (marks / assessmentTotalMarks) * 100;
        
        if (percentage >= 85) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 75) return "A-";
        if (percentage >= 70) return "B+";
        if (percentage >= 65) return "B";
        if (percentage >= 60) return "B-";
        if (percentage >= 55) return "C+";
        if (percentage >= 50) return "C";
        if (percentage >= 45) return "D";
        return "F";
    }
    
    
    
        private void refreshData() {
    onAssessmentSelected(); 
        }

    private void clearForm() {
        txtMarks.setText("");
    if (cmbStudentSelect.getItemCount() > 0) cmbStudentSelect.setSelectedIndex(0);
        }

    private void saveSingleMarkFromForm() {

    if (selectedAssessmentID == null) {
        JOptionPane.showMessageDialog(this, "Select an assessment first!",
                "Error", JOptionPane.ERROR_MESSAGE);
        loadStudents(); 
        return;
    }

    if (cmbStudentSelect.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Select a student first!",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String studentItem = cmbStudentSelect.getSelectedItem().toString();
    String studentID = studentItem.split(" - ", 2)[0].trim();

    String marksStr = txtMarks.getText().trim();
    if (marksStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter marks first!",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double marks;
    try {
        marks = Double.parseDouble(marksStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Marks must be a number!",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (marks < 0 || marks > assessmentTotalMarks) {
        JOptionPane.showMessageDialog(this,
                "Marks must be between 0 and " + assessmentTotalMarks,
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String grade = calculateGrade(marks);

    boolean ok = lecturer.enterMarks(studentID, selectedAssessmentID, marks, grade);
    if (!ok) {
        JOptionPane.showMessageDialog(this, "Failed to save marks!",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        if (tableModel.getValueAt(i, 0).toString().equals(studentID)) {
            tableModel.setValueAt(marks, i, 2);
            tableModel.setValueAt(grade, i, 3);
            tableModel.setValueAt("Entered", i, 4);
            break;
        }
    }

    JOptionPane.showMessageDialog(this, "Marks saved for " + studentID + " (" + grade + ")",
            "Success", JOptionPane.INFORMATION_MESSAGE);

    clearForm();
}

  
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        inputPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbStudentSelect = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        idkpanel = new javax.swing.JPanel();
        txtMarks = new javax.swing.JTextField();
        lblMaxMarks = new javax.swing.JLabel();
        cmbAssessment = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lblAssessmentInfo = new javax.swing.JLabel();
        tablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        btnPanel = new javax.swing.JPanel();
        btnSaveMarks = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(29, 78, 216));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Key-in Assessment Marks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 932, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        mainPanel.setBackground(new java.awt.Color(245, 247, 250));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        inputPanel.setBackground(new java.awt.Color(245, 247, 250));
        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 128, 185), 2), "Enter Student Marks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(29, 78, 216))); // NOI18N
        inputPanel.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Student:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        inputPanel.add(jLabel2, gridBagConstraints);

        cmbStudentSelect.setBackground(new java.awt.Color(204, 204, 204));
        cmbStudentSelect.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        cmbStudentSelect.setForeground(new java.awt.Color(0, 0, 0));
        cmbStudentSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cmbStudentSelect.setPreferredSize(new java.awt.Dimension(400, 35));
        cmbStudentSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStudentSelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        inputPanel.add(cmbStudentSelect, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Marks:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        inputPanel.add(jLabel3, gridBagConstraints);

        idkpanel.setBackground(new java.awt.Color(245, 247, 250));
        idkpanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        txtMarks.setBackground(new java.awt.Color(204, 204, 204));
        txtMarks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtMarks.setForeground(new java.awt.Color(0, 0, 0));
        txtMarks.setPreferredSize(new java.awt.Dimension(100, 35));
        idkpanel.add(txtMarks);

        lblMaxMarks.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblMaxMarks.setForeground(new java.awt.Color(0, 0, 0));
        lblMaxMarks.setText("/100");
        idkpanel.add(lblMaxMarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        inputPanel.add(idkpanel, gridBagConstraints);

        cmbAssessment.setBackground(new java.awt.Color(204, 204, 204));
        cmbAssessment.setPreferredSize(new java.awt.Dimension(400, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        inputPanel.add(cmbAssessment, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(29, 78, 216));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Assessment: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        inputPanel.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        inputPanel.add(lblAssessmentInfo, gridBagConstraints);

        mainPanel.add(inputPanel);

        tablePanel.setBackground(new java.awt.Color(29, 78, 216));
        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Students", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        tablePanel.setPreferredSize(new java.awt.Dimension(130, 130));
        tablePanel.setLayout(new java.awt.BorderLayout());

        tblStudents.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblStudents.setForeground(new java.awt.Color(0, 0, 0));
        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student ID", " Name", "Marks Obtained", "Grade", "Status"
            }
        ));
        tblStudents.setName(""); // NOI18N
        jScrollPane1.setViewportView(tblStudents);

        tablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        mainPanel.add(tablePanel);

        btnPanel.setBackground(new java.awt.Color(245, 247, 250));
        btnPanel.setPreferredSize(new java.awt.Dimension(850, 50));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        btnPanel.setLayout(flowLayout1);

        btnSaveMarks.setBackground(new java.awt.Color(16, 185, 129));
        btnSaveMarks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveMarks.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveMarks.setText("Save Marks");
        btnSaveMarks.setPreferredSize(new java.awt.Dimension(150, 35));
        btnSaveMarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMarksActionPerformed(evt);
            }
        });
        btnPanel.add(btnSaveMarks);

        btnRefresh.setBackground(new java.awt.Color(37, 99, 235));
        btnRefresh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.setPreferredSize(new java.awt.Dimension(150, 35));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        btnPanel.add(btnRefresh);

        btnClear.setBackground(new java.awt.Color(30, 41, 59));
        btnClear.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.setPreferredSize(new java.awt.Dimension(150, 35));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        btnPanel.add(btnClear);

        mainPanel.add(btnPanel);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveMarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMarksActionPerformed
       saveSingleMarkFromForm();


   
    }//GEN-LAST:event_btnSaveMarksActionPerformed

    private void cmbStudentSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStudentSelectActionPerformed
//        loadStudentCombo();
    }//GEN-LAST:event_cmbStudentSelectActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshData();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSaveMarks;
    private javax.swing.JComboBox<String> cmbAssessment;
    private javax.swing.JComboBox<String> cmbStudentSelect;
    private javax.swing.JPanel idkpanel;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAssessmentInfo;
    private javax.swing.JLabel lblMaxMarks;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtMarks;
    // End of variables declaration//GEN-END:variables
}
