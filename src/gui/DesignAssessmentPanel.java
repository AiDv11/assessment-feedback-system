package gui;

import models.Lecturer;
import interfaces.Editor;
import java.time.LocalDate;
import utils.FileManager;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;


public class DesignAssessmentPanel extends JPanel implements Editor {
    
    private Lecturer lecturer;
    
    public DesignAssessmentPanel(Lecturer lecturer) {
        this.lecturer = lecturer;
        initComponents();
        loadData();      
        tblAssessments.setDefaultEditor(Object.class, null);
        btnDelete.setEnabled(false);

        tblAssessments.getSelectionModel().addListSelectionListener(e -> {
            btnDelete.setEnabled(tblAssessments.getSelectedRow() != -1);
        });


    }
    
   
    @Override
    public void loadData() {
               
              cmbModule.removeAllItems();

    List<String> lines = FileManager.readFile("data/modules.txt");

    for (String line : lines) {
        if (line == null || line.trim().isEmpty()) continue;

        String[] parts = line.split("\\|");
        
        if (parts.length >= 4) {
            String code = parts[0].trim();
            String credits = parts[2].trim();
            String name = parts[3].trim();

            cmbModule.addItem(code + " - " + name + " (" + credits + " credits)");
            
        }
    }
    

        if (cmbModule.getItemCount() == 0) {
            cmbModule.addItem("No modules found");
        }
    }

    
    
    private void loadAssessmentsTable() {
        DefaultTableModel model = (DefaultTableModel) tblAssessments.getModel();
        
        model.setRowCount(0);
        List<String> lines = FileManager.readFile("data/assessments.txt");
        
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            if (parts.length < 8) continue;

            String id, lecturerId, module, name, type, marks, weightage, date;

          
            if (parts[1].startsWith("U")) {
                id = parts[0];
                lecturerId = parts[1];
                module = parts[2];
                name = parts[3];
                type = parts[4];
                marks = parts[5];
                weightage = parts[6];
                date = parts[7];
            }
            
            else {
                id = parts[0];
                module = parts[1];
                name = parts[2];
                type = parts[3];
                marks = parts[4];
                weightage = parts[5];
                lecturerId = parts[6];
                date = parts[7];
            }
            
            
            if (!lecturerId.equals(lecturer.getUserID())) {
                continue;
            }
            
         
        String moduleDisplay = module + " - " + getModuleName(module);
        model.addRow(new Object[]{ id, moduleDisplay, name, type, marks, weightage, date });

          
            
    }    
        
}



      private String getModuleName(String moduleCode) {
            List<String> modules = FileManager.readFile("data/modules.txt");
            for (String line : modules) {
                if (line == null || line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");

     
                if (parts.length >= 4 && parts[0].trim().equals(moduleCode.trim())) {
                    return parts[3].trim(); 
                }
            }
            return moduleCode;
    }

       
    private double getUsedWeightageForModule(String moduleCode) {
        double sum = 0;
        List<String> lines = FileManager.readFile("data/assessments.txt");

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 8) continue;

            String module;
            String weightageStr;
            String lecturerId;

           
            module = parts[1].trim();
            weightageStr = parts[5].trim();
            lecturerId = parts[6].trim();

            if (lecturerId.equals(lecturer.getUserID()) && module.equals(moduleCode)) {
                try { sum += Double.parseDouble(weightageStr); } catch (Exception ignored) {}
            }
        }
        return sum;
    }

   private boolean deleteAssessmentFromFile(String assessmentId) {
            String path = "data/assessments.txt";

            List<String> lines = FileManager.readFile(path);
            if (lines == null || lines.isEmpty()) return false;

            boolean removed = false;
            java.util.List<String> updated = new java.util.ArrayList<>();

            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].equals(assessmentId)) {
                    removed = true; 
                    continue;
                }
                updated.add(line);
            }

            if (removed) {
                FileManager.writeFile(path, updated); 
          
            }

            return removed;
}



    @Override
    public void saveChanges() {
   
        
        if (cmbModule.getSelectedItem() == null || 
            cmbModule.getSelectedItem().toString().equals("No modules assigned")) {
            JOptionPane.showMessageDialog(this,
                "You have no modules assigned!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        String assessmentName = txtAssessmentName.getText().trim();
        String totalMarksStr = txtTotalMarks.getText().trim();
        String weightageStr = txtWeightage.getText().trim();
        
        if (assessmentName.isEmpty() || totalMarksStr.isEmpty() || weightageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields!",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
    
        int totalMarks;
        double weightage;
        
        try {
            totalMarks = Integer.parseInt(totalMarksStr);
            weightage = Double.parseDouble(weightageStr);
            
            if (totalMarks <= 0 || weightage <= 0 || weightage > 100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers!\nTotal Marks > 0\nWeightage: 0-100",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
    
        String selectedModule = cmbModule.getSelectedItem().toString();
        String moduleCode = selectedModule.split(" - ", 2)[0];

        
        String assessmentType = cmbAssessmentType.getSelectedItem().toString();
    

        double used = getUsedWeightageForModule(moduleCode);
        if (used + weightage > 100) {
            JOptionPane.showMessageDialog(this,
                "Weightage exceeds 100% for this module.\nUsed: " + used + "%\nYou are adding: " + weightage + "%",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
            
        String dateStr = txtDueDate.getText().trim();  
            try {
                java.time.LocalDate.parse(dateStr); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid date!\nUse: YYYY-MM-DD (example: 2026-02-12)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


        
               
        LocalDate date = LocalDate.parse(dateStr); 
        boolean success = lecturer.createAssessment(moduleCode, assessmentName,
        assessmentType, totalMarks, weightage, date);


        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Assessment created successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            txtAssessmentName.setText("");
            txtTotalMarks.setText("");
            txtWeightage.setText("");
            
       
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create assessment. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
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
        java.awt.GridBagConstraints gridBagConstraints;

        topPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtAssessmentName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbAssessmentType = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtTotalMarks = new javax.swing.JTextField();
        lblWeightage = new javax.swing.JLabel();
        txtWeightage = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDueDate = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        btnCreate = new javax.swing.JButton();
        btnViewAssessments = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tablePanel = new javax.swing.JPanel();
        scrollPanel = new javax.swing.JScrollPane();
        tblAssessments = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        topPanel.setBackground(new java.awt.Color(29, 78, 216));
        topPanel.setPreferredSize(new java.awt.Dimension(300, 60));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Design Assessments");
        topPanel.add(lblTitle);

        add(topPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setBackground(new java.awt.Color(245, 247, 250));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        formPanel.setBackground(new java.awt.Color(245, 247, 250));
        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Create New Assessment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(29, 78, 216))); // NOI18N
        formPanel.setForeground(new java.awt.Color(0, 0, 0));
        formPanel.setMaximumSize(new java.awt.Dimension(2222, 340));
        formPanel.setMinimumSize(new java.awt.Dimension(844, 300));
        formPanel.setPreferredSize(new java.awt.Dimension(900, 300));
        formPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Module:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(jLabel1, gridBagConstraints);

        cmbModule.setBackground(new java.awt.Color(204, 204, 204));
        cmbModule.setForeground(new java.awt.Color(0, 0, 0));
        cmbModule.setOpaque(true);
        cmbModule.setPreferredSize(new java.awt.Dimension(250, 30));
        cmbModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbModuleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(cmbModule, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Assessment Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(jLabel2, gridBagConstraints);

        txtAssessmentName.setBackground(new java.awt.Color(204, 204, 204));
        txtAssessmentName.setForeground(new java.awt.Color(0, 0, 0));
        txtAssessmentName.setPreferredSize(new java.awt.Dimension(250, 30));
        txtAssessmentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAssessmentNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        formPanel.add(txtAssessmentName, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText(" Assessment Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(jLabel3, gridBagConstraints);

        cmbAssessmentType.setBackground(new java.awt.Color(204, 204, 204));
        cmbAssessmentType.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cmbAssessmentType.setForeground(new java.awt.Color(0, 0, 0));
        cmbAssessmentType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Assignment", "Quiz", "Test", "Midterm Exam", "Final Exam", "Project", "Presentation" }));
        cmbAssessmentType.setOpaque(true);
        cmbAssessmentType.setPreferredSize(new java.awt.Dimension(250, 30));
        cmbAssessmentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAssessmentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(cmbAssessmentType, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Total Marks:");
        jLabel4.setName("Total Marks:"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(jLabel4, gridBagConstraints);

        txtTotalMarks.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalMarks.setForeground(new java.awt.Color(0, 0, 0));
        txtTotalMarks.setPreferredSize(new java.awt.Dimension(250, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        formPanel.add(txtTotalMarks, gridBagConstraints);

        lblWeightage.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblWeightage.setForeground(new java.awt.Color(0, 0, 0));
        lblWeightage.setText("Weightage (%):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(lblWeightage, gridBagConstraints);

        txtWeightage.setBackground(new java.awt.Color(204, 204, 204));
        txtWeightage.setForeground(new java.awt.Color(0, 0, 0));
        txtWeightage.setToolTipText("");
        txtWeightage.setPreferredSize(new java.awt.Dimension(250, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        formPanel.add(txtWeightage, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Date:");
        jLabel5.setPreferredSize(new java.awt.Dimension(40, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(jLabel5, gridBagConstraints);

        txtDueDate.setBackground(new java.awt.Color(204, 204, 204));
        txtDueDate.setForeground(new java.awt.Color(0, 0, 0));
        txtDueDate.setPreferredSize(new java.awt.Dimension(250, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        formPanel.add(txtDueDate, gridBagConstraints);

        buttonPanel.setBackground(new java.awt.Color(245, 247, 250));
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 3));

        btnCreate.setBackground(new java.awt.Color(16, 185, 129));
        btnCreate.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnCreate.setForeground(new java.awt.Color(255, 255, 255));
        btnCreate.setText("Create  Asessment");
        btnCreate.setBorderPainted(false);
        btnCreate.setFocusPainted(false);
        btnCreate.setPreferredSize(new java.awt.Dimension(180, 35));
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        buttonPanel.add(btnCreate);

        btnViewAssessments.setBackground(new java.awt.Color(37, 99, 235));
        btnViewAssessments.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnViewAssessments.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAssessments.setText("View My Assessments");
        btnViewAssessments.setToolTipText("");
        btnViewAssessments.setBorderPainted(false);
        btnViewAssessments.setFocusPainted(false);
        btnViewAssessments.setPreferredSize(new java.awt.Dimension(180, 35));
        btnViewAssessments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAssessmentsActionPerformed(evt);
            }
        });
        buttonPanel.add(btnViewAssessments);

        btnClear.setBackground(new java.awt.Color(30, 41, 59));
        btnClear.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.setPreferredSize(new java.awt.Dimension(180, 35));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        buttonPanel.add(btnClear);

        btnDelete.setBackground(new java.awt.Color(239, 68, 68));
        btnDelete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(180, 35));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        buttonPanel.add(btnDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(30, 10, 10, 10);
        formPanel.add(buttonPanel, gridBagConstraints);

        mainPanel.add(formPanel);

        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 50));
        mainPanel.add(jSeparator1);

        tablePanel.setBackground(new java.awt.Color(29, 78, 216));
        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "My Assessments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        tablePanel.setMaximumSize(new java.awt.Dimension(800, 150));
        tablePanel.setPreferredSize(new java.awt.Dimension(60, 30));
        tablePanel.setLayout(new java.awt.BorderLayout());

        scrollPanel.setMaximumSize(new java.awt.Dimension(850, 200));

        tblAssessments.setBackground(new java.awt.Color(245, 247, 250));
        tblAssessments.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tblAssessments.setForeground(new java.awt.Color(0, 0, 0));
        tblAssessments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Module", "Name", "Type", "Total Marks", "Weightage %", "Date"
            }
        ));
        tblAssessments.setToolTipText("");
        tblAssessments.setGridColor(new java.awt.Color(0, 0, 0));
        tblAssessments.setRowHeight(25);
        scrollPanel.setViewportView(tblAssessments);

        tablePanel.add(scrollPanel, java.awt.BorderLayout.CENTER);

        mainPanel.add(tablePanel);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbModuleActionPerformed
        
    }//GEN-LAST:event_cmbModuleActionPerformed
        
    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        saveChanges();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnViewAssessmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAssessmentsActionPerformed
       loadAssessmentsTable();
    }//GEN-LAST:event_btnViewAssessmentsActionPerformed

    private void txtAssessmentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAssessmentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssessmentNameActionPerformed

    private void cmbAssessmentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAssessmentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAssessmentTypeActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

    txtAssessmentName.setText("");
    txtTotalMarks.setText("");
    txtWeightage.setText("");
    cmbAssessmentType.setSelectedIndex(0);

    JOptionPane.showMessageDialog(this,
        "Form cleared.",
        "Info",
        JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         int row = tblAssessments.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select an assessment from the table first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String assessmentId = tblAssessments.getValueAt(row, 0).toString(); // ID column

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete assessment: " + assessmentId + " ?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);

    if (confirm != JOptionPane.YES_OPTION) return;

    boolean deleted = deleteAssessmentFromFile(assessmentId);

    if (deleted) {
        JOptionPane.showMessageDialog(this,
            "Assessment deleted successfully!",
            "Deleted", JOptionPane.INFORMATION_MESSAGE);
        loadAssessmentsTable(); // refresh table
    } else {
        JOptionPane.showMessageDialog(this,
            "Delete failed! Could not find assessment in file.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnViewAssessments;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox<String> cmbAssessmentType;
    private javax.swing.JComboBox<String> cmbModule;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWeightage;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable tblAssessments;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtAssessmentName;
    private javax.swing.JTextField txtDueDate;
    private javax.swing.JTextField txtTotalMarks;
    private javax.swing.JTextField txtWeightage;
    // End of variables declaration//GEN-END:variables

    
}