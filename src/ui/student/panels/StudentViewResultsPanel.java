/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.student.panels;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Abdu
 */
public class StudentViewResultsPanel extends javax.swing.JPanel {
    
    public void refreshResults(String studentId) {
    this.currentStudentId = studentId;
    loadEnrolledClassesToCombo(); 
}

    private static final String MARKS_FILE = "data/marks.txt";
    private static final String FEEDBACK_FILE = "data/feedback.txt";
    private static final String ENROLL_FILE = "data/enrollments.txt";
    private static final String GRADING_FILE = "data/grading.txt";
    private static final String CLASSES_FILE = "data/classes.txt";

    private String currentStudentId;
    
   
    

    public StudentViewResultsPanel() {
        initComponents();
        cboClass.removeAllItems();
        tblResults.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] { "Assessment", "Mark", "Feedback" }
            )
        {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });
        tblResults.setRowSelectionAllowed(true);
        tblResults.setColumnSelectionAllowed(false);
        tblResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

       
        lblAverage.setText("0.00");
        lblGrade.setText("-");
    }
    
    
    
    
    private List<String> readLinesSafe(String filePath) {
        return FileManager.readFile(filePath);
    }
    
    private void loadEnrolledClassesToCombo() {
    try {
        cboClass.removeAllItems();

        List<String> enrollLines = readLinesSafe(ENROLL_FILE);
        List<String> classLines = readLinesSafe(CLASSES_FILE);

        for (String e : enrollLines) {
            if (e == null || e.trim().isEmpty()) continue;
            
            String[] ep = e.split("\\|"); 
            if (ep.length >= 2 && ep[0].trim().equals(currentStudentId)) {

                String classId = ep[1].trim();
                String display = classId;
                String moduleCode = null;

                for (String c : classLines) {
                    if (c == null || c.trim().isEmpty()) continue;
                    
                    String[] cp = c.split("\\|");
                   
                    if (cp.length >= 6 && cp[0].trim().equals(classId)) {
                        moduleCode = cp[1].trim();
                        String className = cp[2].trim();
                        display = classId + " - " + className;
                        break;
                    } else if (cp.length >= 5 && cp[0].trim().equals(classId)) {
                        
                        moduleCode = cp[1].trim();
                        String moduleName = cp[2].trim();
                        display = classId + " - " + moduleName;
                        break;
                    }
                }

              
                if (moduleCode != null && !moduleCode.equals("NO_MODULE")) {
                    cboClass.addItem(display + " [" + moduleCode + "]");
                } else if (moduleCode != null) {
                    
                    continue;
                }
            }
        }

        if (cboClass.getItemCount() > 0) {
            cboClass.setSelectedIndex(0);
            String selected = cboClass.getSelectedItem().toString();
            if (selected.contains("[") && selected.contains("]")) {
                String moduleCode = selected.substring(selected.lastIndexOf("[") + 1, selected.lastIndexOf("]"));
                if (!moduleCode.equals("No Module Assigned")) {
                    loadResultsForClass(moduleCode);
                } else {
                    clearResultsUI();
                }
            }
        } else {
            clearResultsUI();
        }

    } catch (Exception ex) {
        System.err.println("Error loading classes: " + ex.getMessage());
        ex.printStackTrace();
        clearResultsUI();
        
    }
}

private void loadResultsForClass(String moduleCode) {
    DefaultTableModel model = (DefaultTableModel) tblResults.getModel();
    model.setRowCount(0);
    
    double total = 0;
    int count = 0;

    try {
       
        List<String> assessmentLines = readLinesSafe("data/assessments.txt");
        List<String> assessmentIDs = new ArrayList<>();
        java.util.Map<String, String> assessmentNames = new java.util.HashMap<>();
        java.util.Map<String, Integer> assessmentTotalMarks = new java.util.HashMap<>();
        
        for (String line : assessmentLines) {
            if (line == null || line.trim().isEmpty()) continue;
            
            try {
                String[] parts = line.split("\\|");
                

                String assessmentId = parts[0].trim();
                String assessmentModule;
                String assessmentName;
                int totalMarks;
                
                if (parts.length >= 8 && parts[1].startsWith("U")) {
                
                    assessmentModule = parts[2].trim();
                    assessmentName = parts[3].trim();
                    totalMarks = Integer.parseInt(parts[5].trim());
                } else if (parts.length >= 8) {
                 
                    assessmentModule = parts[1].trim();
                    assessmentName = parts[2].trim();
                    totalMarks = Integer.parseInt(parts[4].trim());
                } else {
                    continue;
                }
                
            
                if (assessmentModule.equals(moduleCode)) {
                    assessmentIDs.add(assessmentId);
                    assessmentNames.put(assessmentId, assessmentName);
                    assessmentTotalMarks.put(assessmentId, totalMarks);
                }
            } catch (Exception e) {
                System.err.println("Error parsing assessment line: " + line);
                e.printStackTrace();
                continue;
            }
        }

        List<String> markLines = readLinesSafe(MARKS_FILE);
        List<String> feedbackLines = readLinesSafe(FEEDBACK_FILE);

        for (String m : markLines) {
            if (m == null || m.trim().isEmpty()) continue;
            
            try {
                String[] mp = m.split("\\|");
    
                if (mp.length >= 5 && mp[1].trim().equals(currentStudentId)) {
                    String assessmentId = mp[2].trim();
                    
                   
                    if (assessmentIDs.contains(assessmentId)) {
                        String assessmentName = assessmentNames.get(assessmentId);
                        double mark = Double.parseDouble(mp[3].trim());
                        String grade = mp[4].trim();

                        String feedbackText = "-";
                   
                        for (String f : feedbackLines) {
                            if (f == null || f.trim().isEmpty()) continue;
                            String[] fp = f.split("\\|");
                            if (fp.length >= 3 && fp[0].trim().equals(currentStudentId)
                                    && fp[1].trim().equals(assessmentId)) {
                                feedbackText = fp[2];
                                break;
                            }
                        }

                     
                        Integer totalMark = assessmentTotalMarks.get(assessmentId);
                        String markDisplay = mark + "/" + (totalMark != null ? totalMark : "?") + " (" + grade + ")";
                        
                        model.addRow(new Object[]{assessmentName, markDisplay, feedbackText});
                        total += mark;
                        count++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing mark line: " + m);
                e.printStackTrace();
                continue;
            }
        }

        if (count == 0) {
        
            clearResultsUI();
            return;
        }

        double avg = total / count;
        lblAverage.setText(String.format("%.2f", avg));
        
     
        String finalGrade = getGradeFromAverage(avg);
        lblGrade.setText(finalGrade);
        
    } catch (Exception ex) {
        System.err.println("Error loading results: " + ex.getMessage());
        ex.printStackTrace();
        clearResultsUI();
    }
}

private String getGradeFromAverage(double avg) {
    try {
        List<String> gradeLines = readLinesSafe(GRADING_FILE);

        for (String g : gradeLines) {
            if (g == null || g.trim().isEmpty()) continue;
            
            try {
                String[] gp = g.split("\\|");

                if (gp.length >= 3) {
                    String grade = gp[0].trim();
                    
               
                    double min = Double.parseDouble(gp[1].trim());
                    double max = Double.parseDouble(gp[2].trim());

                    if (avg >= min && avg <= max) {
                        return grade;
                    }
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error parsing grade boundary: " + g);
                System.err.println("Error: " + nfe.getMessage());
                continue;
            }
        }
    } catch (Exception e) {
        System.err.println("Error reading grading file: " + e.getMessage());
        e.printStackTrace();
    }
    

    if (avg >= 80) return "A";
    if (avg >= 70) return "B";
    if (avg >= 60) return "C";
    if (avg >= 50) return "D";
    return "F";
}

private void clearResultsUI() {
    DefaultTableModel model = (DefaultTableModel) tblResults.getModel();
    model.setRowCount(0);
    lblAverage.setText("0.00");
    lblGrade.setText("-");
    
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboClass = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        lblAverage = new javax.swing.JLabel();
        lblGrade = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        cboClass.setBackground(new java.awt.Color(204, 255, 204));
        cboClass.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboClass.setForeground(new java.awt.Color(0, 0, 0));
        cboClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClassActionPerformed(evt);
            }
        });

        tblResults.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Assessment", "Mark", "Feedback"
            }
        ));
        jScrollPane1.setViewportView(tblResults);

        lblAverage.setFont(new java.awt.Font("Lucida Bright", 0, 24)); // NOI18N
        lblAverage.setText("jLabel1");

        lblGrade.setFont(new java.awt.Font("Lucida Bright", 0, 24)); // NOI18N
        lblGrade.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel2.setText("Average mark:");

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel3.setText("Final Grade:");

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        jLabel4.setText("Select a module:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGrade))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAverage))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboClass, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboClass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblAverage))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblGrade))
                .addContainerGap(93, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClassActionPerformed
                                        
    if (cboClass.getSelectedItem() == null) return;
    if (currentStudentId == null || currentStudentId.isEmpty()) return;


    String selected = cboClass.getSelectedItem().toString();
    if (!selected.contains("[") || !selected.contains("]")) return;
    
    String moduleCode = selected.substring(selected.lastIndexOf("[") + 1, selected.lastIndexOf("]"));
    
    if (moduleCode.equals("No Module Assigned")) {
        clearResultsUI();
        return;
    }
    
    loadResultsForClass(moduleCode); 
       
    
    }//GEN-LAST:event_cboClassActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboClass;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAverage;
    private javax.swing.JLabel lblGrade;
    private javax.swing.JTable tblResults;
    // End of variables declaration//GEN-END:variables
}
