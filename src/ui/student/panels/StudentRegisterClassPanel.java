/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.student.panels;
import utils.FileManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author Abdu
 */
public class StudentRegisterClassPanel extends javax.swing.JPanel {
    
    private void applyGreenTheme() {

    this.setBackground(new java.awt.Color(230, 255, 230)); 


    tblClasses.setBackground(new java.awt.Color(240, 255, 240));
    tblClasses.setForeground(new java.awt.Color(0, 70, 0)); 
    tblClasses.setSelectionBackground(new java.awt.Color(144, 238, 144)); 
    tblClasses.setSelectionForeground(java.awt.Color.BLACK);
    tblClasses.setRowHeight(25);

 
    javax.swing.table.JTableHeader header = tblClasses.getTableHeader();
    header.setBackground(new java.awt.Color(34, 139, 34));
    header.setForeground(java.awt.Color.black);
    header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
}

    
    
    
    private void applyRowColoring() {
    tblClasses.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = table.getValueAt(row, 5).toString(); 

            if (!isSelected) {
                if ("Enrolled".equalsIgnoreCase(status)) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.DARK_GRAY);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } else {
          
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    });
}

    
private void updateEnrollButtonState() {
    int row = tblClasses.getSelectedRow();
    if (row < 0) {
        btnEnroll.setEnabled(false);
        return;
    }

    String status = tblClasses.getValueAt(row, 5).toString(); // Status column
    btnEnroll.setEnabled(!"Enrolled".equalsIgnoreCase(status));
}

private static final String CLASSES_FILE = "data/classes.txt";
private static final String ENROLL_FILE  = "data/enrollments.txt";

private String currentStudentId;


public void refreshRegister(String studentId) {
    this.currentStudentId = studentId;
    loadClassesIntoTable();
}

private List<String> readLinesSafe(String filePath) {
        return FileManager.readFile(filePath);
    }

private void appendLineSafe(String filePath, String line) throws Exception {
        if (!FileManager.appendToFile(filePath, line)) {
            throw new Exception("Failed to append to file: " + filePath);
        }
    }

private void loadClassesIntoTable() {
    DefaultTableModel model = (DefaultTableModel) tblClasses.getModel();
    model.setRowCount(0);

    try {
        List<String> classLines = readLinesSafe(CLASSES_FILE);
        List<String> enrollLines = readLinesSafe(ENROLL_FILE);

        Set<String> enrolled = new HashSet<>();
        for (String e : enrollLines) {
            String[] ep = e.split("\\|"); 
            if (ep.length >= 2 && ep[0].trim().equals(currentStudentId)) {
                enrolled.add(ep[1].trim());
            }
        }

        for (String c : classLines) {
            String[] cp = c.split("\\|");

            if (cp.length >= 7) {
                String classId = cp[0].trim();
                String moduleCode = cp[1].trim();
                String className = cp[2].trim();
                String moduleName = cp[4].trim();  
                String lecturerId = cp[5].trim();
                String schedule = cp[6].trim();

           
                if (moduleCode.equals("NO_MODULE")) {
                    continue;
                }

                String status = enrolled.contains(classId) ? "Enrolled" : "Available";

                model.addRow(new Object[]{
                    classId, moduleCode, moduleName, lecturerId, schedule, status
                });
            } else if (cp.length >= 5) {
                
                String classId = cp[0].trim();
                String moduleCode = cp[1].trim();
                String moduleName = cp[2].trim();
                String lecturerId = cp[3].trim();
                String schedule = cp[4].trim();

                String status = enrolled.contains(classId) ? "Enrolled" : "Available";

                model.addRow(new Object[]{
                    classId, moduleCode, moduleName, lecturerId, schedule, status
                });
            }
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Failed to load classes: " + ex.getMessage());
        ex.printStackTrace();
    }
}


public StudentRegisterClassPanel() {
    initComponents();
    applyGreenTheme();
    applyRowColoring();

    tblClasses.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Class ID", "Module Code", "Module Name",
            "Lecturer ID", "Schedule", "Status"
        }

                
) {
    public boolean isCellEditable(int row, int column) {
        return false;
    }
});

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblClasses = new javax.swing.JTable();
        btnEnroll = new javax.swing.JButton();

        tblClasses.setBackground(new java.awt.Color(255, 255, 255));
        tblClasses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblClasses);

        btnEnroll.setBackground(new java.awt.Color(0, 153, 51));
        btnEnroll.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEnroll.setForeground(new java.awt.Color(255, 255, 255));
        btnEnroll.setText("Enroll");
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(btnEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(188, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
 if (currentStudentId == null || currentStudentId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Student session not found.");
        return;
    }

    int selectedRow = tblClasses.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select a class first.");
        return;
    }

    String classId = tblClasses.getValueAt(selectedRow, 0).toString();
    String status  = tblClasses.getValueAt(selectedRow, 5).toString(); // Status column

    if ("Enrolled".equalsIgnoreCase(status)) {
        JOptionPane.showMessageDialog(
            this,
            "You are already enrolled in this class.",
            "Enrollment Info",
            JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    try {
        List<String> enrollLines = readLinesSafe(ENROLL_FILE);
        for (String line : enrollLines) {
            String[] parts = line.split("\\|"); // userId|classId
            if (parts.length >= 2
                    && parts[0].trim().equals(currentStudentId)
                    && parts[1].trim().equals(classId)) {

                JOptionPane.showMessageDialog(
                    this,
                    "You are already enrolled in this class.",
                    "Enrollment Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
        }

      
        appendLineSafe(ENROLL_FILE, currentStudentId + "|" + classId);

        JOptionPane.showMessageDialog(
            this,
            "Enrollment successful!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

    
        loadClassesIntoTable();
        tblClasses.repaint();


    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Failed to enroll: " + ex.getMessage());
    }
    }//GEN-LAST:event_btnEnrollActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnroll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClasses;
    // End of variables declaration//GEN-END:variables
}
