/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.GradingSystem;
import utils.FileManager;


/**
 *
 * @author Hussain Alkhaldi
 */
public class DefineGradingPanel extends javax.swing.JPanel {

    
    private static final String[] GRADE_ORDER = {
        "A+", "A", "B+", "B", "C+", "C", "C-", "D", "F+", "F", "F-"
    };

    public DefineGradingPanel() {
        initComponents();
        loadGrading();
        initActions();
    }
    
  
    private void loadGrading() {
        DefaultTableModel model = (DefaultTableModel) tblGrading.getModel();
        model.setRowCount(0);

        List<String> lines = FileManager.readFile(GradingSystem.GRADING_FILE);

        if (lines.isEmpty()) {
            return;
        }


        Map<String, GradingSystem> gradeMap = new LinkedHashMap<>();
        for (String line : lines) {
            GradingSystem gs = GradingSystem.fromFileString(line);
            if (gs != null) {
                gradeMap.put(gs.getGrade(), gs);
            }
        }


        for (String grade : GRADE_ORDER) {
            if (gradeMap.containsKey(grade)) {
                GradingSystem gs = gradeMap.get(grade);
                model.addRow(new Object[]{
                    gs.getGrade(),
                    gs.getMinMark(),
                    gs.getMaxMark()
                });
            }
        }
    }

    private void initActions() {
        btnAdd.addActionListener(e -> updateGrading());
        btnDelete.addActionListener(e -> deleteGrade());
        btnSaveDefault.addActionListener(e -> loadDefaultGrading());
    }

 
private void updateGrading() {
        java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(this);
        UpdateGradingDialog dialog = new UpdateGradingDialog(parent);
        dialog.setVisible(true);
    }


    private class UpdateGradingDialog extends javax.swing.JDialog {

        private static final int ROW_HEIGHT = 30;
        private static final int FIELD_WIDTH = 100;

        private javax.swing.JTextField[] minFields;
        private javax.swing.JTextField[] maxFields;
        private javax.swing.JLabel lblError;
        private javax.swing.JButton btnOk;

        private static final java.awt.Color ERROR_COLOR = new java.awt.Color(254, 202, 202);
        private static final java.awt.Color BORDER_ERROR_COLOR = new java.awt.Color(239, 68, 68);
        private static final java.awt.Color BORDER_DEFAULT_COLOR = new java.awt.Color(209, 213, 219);

        public UpdateGradingDialog(java.awt.Window parent) {
            super(parent, "Update Grading System", ModalityType.APPLICATION_MODAL);
            initDialogComponents();
            populateCurrentValues();
        }

        private void initDialogComponents() {
            int dialogHeight = 100 + (GRADE_ORDER.length * (ROW_HEIGHT + 8)) + 80;
            setSize(520, dialogHeight);
            setLocationRelativeTo(DefineGradingPanel.this);
            setResizable(false);
            setLayout(new java.awt.BorderLayout());

        
            javax.swing.JPanel headerPanel = new javax.swing.JPanel();
            headerPanel.setBackground(new java.awt.Color(124, 58, 237));
            headerPanel.setPreferredSize(new java.awt.Dimension(520, 55));
            headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            javax.swing.JLabel lblTitle = new javax.swing.JLabel("Update All Grades");
            lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            lblTitle.setForeground(java.awt.Color.WHITE);
            headerPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 12, 350, 30));

            add(headerPanel, java.awt.BorderLayout.NORTH);

            javax.swing.JPanel bodyPanel = new javax.swing.JPanel();
            bodyPanel.setBackground(new java.awt.Color(243, 244, 246));
            bodyPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

          
            javax.swing.JLabel lblGradeCol = new javax.swing.JLabel("Grade");
            lblGradeCol.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            lblGradeCol.setForeground(new java.awt.Color(55, 65, 81));
            bodyPanel.add(lblGradeCol, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 10, 70, 22));

            javax.swing.JLabel lblMinCol = new javax.swing.JLabel("Min Mark");
            lblMinCol.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            lblMinCol.setForeground(new java.awt.Color(55, 65, 81));
            bodyPanel.add(lblMinCol, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 22));

            javax.swing.JLabel lblMaxCol = new javax.swing.JLabel("Max Mark");
            lblMaxCol.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            lblMaxCol.setForeground(new java.awt.Color(55, 65, 81));
            bodyPanel.add(lblMaxCol, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 100, 22));

            
            javax.swing.JSeparator sep = new javax.swing.JSeparator();
            bodyPanel.add(sep, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 33, 480, 2));

         
            minFields = new javax.swing.JTextField[GRADE_ORDER.length];
            maxFields = new javax.swing.JTextField[GRADE_ORDER.length];

            for (int i = 0; i < GRADE_ORDER.length; i++) {
                int y = 42 + (i * (ROW_HEIGHT + 8));

                javax.swing.JLabel lblGrade = new javax.swing.JLabel(GRADE_ORDER[i]);
                lblGrade.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
                lblGrade.setForeground(new java.awt.Color(55, 65, 81));
                bodyPanel.add(lblGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, y + 4, 70, 24));

           
                minFields[i] = new javax.swing.JTextField();
                minFields[i].setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
                minFields[i].setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
                minFields[i].setHorizontalAlignment(javax.swing.JTextField.CENTER);
                bodyPanel.add(minFields[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(120, y, FIELD_WIDTH, ROW_HEIGHT));

                
                javax.swing.JLabel lblDash = new javax.swing.JLabel("—");
                lblDash.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
                lblDash.setForeground(new java.awt.Color(107, 114, 128));
                bodyPanel.add(lblDash, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, y + 4, 20, 22));

                
                maxFields[i] = new javax.swing.JTextField();
                maxFields[i].setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
                maxFields[i].setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
                maxFields[i].setHorizontalAlignment(javax.swing.JTextField.CENTER);
                bodyPanel.add(maxFields[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(250, y, FIELD_WIDTH, ROW_HEIGHT));

              
                final int idx = i;
                minFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyReleased(java.awt.event.KeyEvent e) {
                        clearFieldErrors();
                        lblError.setText("");
                    }
                });
                maxFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyReleased(java.awt.event.KeyEvent e) {
                        clearFieldErrors();
                        lblError.setText("");
                    }
                });
            }

          
            int errorY = 42 + (GRADE_ORDER.length * (ROW_HEIGHT + 8)) + 5;
            lblError = new javax.swing.JLabel("");
            lblError.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
            lblError.setForeground(new java.awt.Color(239, 68, 68));
            bodyPanel.add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, errorY, 480, 22));

            add(bodyPanel, java.awt.BorderLayout.CENTER);

       
            javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
            buttonPanel.setBackground(new java.awt.Color(124, 58, 237));
            buttonPanel.setPreferredSize(new java.awt.Dimension(520, 50));
            buttonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            btnOk = new javax.swing.JButton("OK - Save Grades");
            btnOk.setBackground(new java.awt.Color(16, 185, 129));
            btnOk.setForeground(java.awt.Color.WHITE);
            btnOk.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            btnOk.setFocusPainted(false);
            btnOk.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            btnOk.addActionListener(e -> submitGrades());
            buttonPanel.add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 170, 32));

            javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
            btnCancel.setBackground(new java.awt.Color(156, 163, 175));
            btnCancel.setForeground(java.awt.Color.WHITE);
            btnCancel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            btnCancel.setFocusPainted(false);
            btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            btnCancel.addActionListener(e -> dispose());
            buttonPanel.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 10, 100, 32));

            add(buttonPanel, java.awt.BorderLayout.SOUTH);
        }

        private void populateCurrentValues() {

            Map<String, GradingSystem> gradeMap = new LinkedHashMap<>();
            for (String line : FileManager.readFile(GradingSystem.GRADING_FILE)) {
                GradingSystem gs = GradingSystem.fromFileString(line);
                if (gs != null) {
                    gradeMap.put(gs.getGrade(), gs);
                }
            }

            for (int i = 0; i < GRADE_ORDER.length; i++) {
                GradingSystem gs = gradeMap.get(GRADE_ORDER[i]);
                if (gs != null) {
                    minFields[i].setText(String.valueOf((int) gs.getMinMark()));
                    maxFields[i].setText(String.valueOf((int) gs.getMaxMark()));
                } else {
                    minFields[i].setText("");
                    maxFields[i].setText("");
                }
            }
        }

        private void clearFieldErrors() {
            for (int i = 0; i < GRADE_ORDER.length; i++) {
                minFields[i].setBackground(java.awt.Color.WHITE);
                minFields[i].setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
                maxFields[i].setBackground(java.awt.Color.WHITE);
                maxFields[i].setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
            }
        }

        private void setFieldError(javax.swing.JTextField field) {
            field.setBackground(ERROR_COLOR);
            field.setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_ERROR_COLOR, 2));
        }

        private void submitGrades() {
            clearFieldErrors();
            lblError.setText("");

            double[] mins = new double[GRADE_ORDER.length];
            double[] maxs = new double[GRADE_ORDER.length];

       
            for (int i = 0; i < GRADE_ORDER.length; i++) {
                String minStr = minFields[i].getText().trim();
                String maxStr = maxFields[i].getText().trim();

                if (minStr.isEmpty() || maxStr.isEmpty()) {
                    setFieldError(minStr.isEmpty() ? minFields[i] : maxFields[i]);
                    lblError.setText("All grades must have Min and Max values.");
                    return;
                }

                try {
                    mins[i] = Double.parseDouble(minStr);
                    maxs[i] = Double.parseDouble(maxStr);
                } catch (NumberFormatException e) {
                    setFieldError(minFields[i]);
                    setFieldError(maxFields[i]);
                    lblError.setText(GRADE_ORDER[i] + ": Enter valid numbers only.");
                    return;
                }
            }

            for (int i = 0; i < GRADE_ORDER.length; i++) {
                if (mins[i] < 0 || mins[i] > 100 || maxs[i] < 0 || maxs[i] > 100) {
                    setFieldError(minFields[i]);
                    setFieldError(maxFields[i]);
                    lblError.setText(GRADE_ORDER[i] + ": Min and Max must be between 0 and 100.");
                    return;
                }
                if (mins[i] > maxs[i]) {
                    setFieldError(minFields[i]);
                    setFieldError(maxFields[i]);
                    lblError.setText(GRADE_ORDER[i] + ": Min (" + (int)mins[i] + ") cannot be greater than Max (" + (int)maxs[i] + ").");
                    return;
                }
            }

            for (int i = 1; i < GRADE_ORDER.length; i++) {
                if (maxs[i] + 1 != mins[i - 1]) {
                    setFieldError(maxFields[i]);
                    setFieldError(minFields[i - 1]);
                    lblError.setText(GRADE_ORDER[i] + " Max (" + (int)maxs[i] + ") + 1 must equal "
                            + GRADE_ORDER[i - 1] + " Min (" + (int)mins[i - 1] + "). No gaps or overlaps allowed.");
                    return;
                }
            }

           
            if (maxs[0] != 100) {
                setFieldError(maxFields[0]);
                lblError.setText(GRADE_ORDER[0] + " Max must be 100.");
                return;
            }
            if (mins[GRADE_ORDER.length - 1] != 0) {
                setFieldError(minFields[GRADE_ORDER.length - 1]);
                lblError.setText(GRADE_ORDER[GRADE_ORDER.length - 1] + " Min must be 0.");
                return;
            }

  
            List<String> output = new ArrayList<>();
            for (int i = 0; i < GRADE_ORDER.length; i++) {
                GradingSystem gs = new GradingSystem(GRADE_ORDER[i], mins[i], maxs[i]);
                output.add(gs.toFileString());
            }

            FileManager.writeFile(GradingSystem.GRADING_FILE, output);
            loadGrading(); 

            JOptionPane.showMessageDialog(this,
                    "All grades updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private void deleteGrade() {
        int row = tblGrading.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a grade first");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblGrading.getModel();

        String grade = model.getValueAt(row, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete grade " + grade + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm != JOptionPane.YES_OPTION) return;

        double min = Double.parseDouble(model.getValueAt(row, 1).toString());
        double max = Double.parseDouble(model.getValueAt(row, 2).toString());

        GradingSystem gs = new GradingSystem(grade, min, max);

        FileManager.deleteLine(
            GradingSystem.GRADING_FILE,
            gs.toFileString()
        );

        loadGrading();
        JOptionPane.showMessageDialog(this, "Grade deleted successfully");
    }

    private void loadDefaultGrading() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "This will reset all grades to default APU grading system.\n" +
            "Are you sure?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

    
        FileManager.writeFile(
            GradingSystem.GRADING_FILE,
            new ArrayList<>()
        );

      
        String[] grades = {
            "A+", "A", "B+", "B", "C+", "C", "C-", "D", "F+", "F", "F-"
        };

        double[][] ranges = {
            {80, 100},  // A+
            {75, 79},   // A
            {70, 74},   // B+
            {65, 69},   // B
            {60, 64},   // C+
            {55, 59},   // C
            {50, 54},   // C-
            {40, 49},   // D
            {30, 39},   // F+
            {20, 29},   // F
            {0, 19}     // F-
        };

        for (int i = 0; i < grades.length; i++) {
            GradingSystem gs = new GradingSystem(
                grades[i],
                ranges[i][0],
                ranges[i][1]
            );

            FileManager.appendToFile(
                GradingSystem.GRADING_FILE,
                gs.toFileString()
            );
        }

        loadGrading();
        JOptionPane.showMessageDialog(
            this, 
            "Default APU grading system loaded successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
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
        infoPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGrading = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSaveDefault = new javax.swing.JButton();

        setBackground(new java.awt.Color(236, 240, 241));
        setPreferredSize(new java.awt.Dimension(1200, 620));
        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(124, 58, 237));
        headerPanel.setPreferredSize(new java.awt.Dimension(700, 80));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("APU Grading System");
        headerPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 640, 40));

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        infoPanel.setBackground(new java.awt.Color(254, 243, 199));
        infoPanel.setPreferredSize(new java.awt.Dimension(700, 50));
        infoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblGrading.setAutoCreateRowSorter(true);
        tblGrading.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Grade", "Min Mark", "Max Mark"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGrading.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblGrading.setPreferredSize(new java.awt.Dimension(660, 330));
        tblGrading.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGrading.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblGrading);

        infoPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 88, 969, 525));

        lblInfo.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(146, 64, 14));
        lblInfo.setText("Standard APU Grading: A+ (80-100), A (75-79), B+ (70-74), B (65-69), C+ (60-64), C (55-59), C- (50-54), D (40-49), F+ (30-39), F (20-29), F- (0-19)");
        infoPanel.add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 830, 20));

        add(infoPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBackground(new java.awt.Color(243, 244, 246));
        buttonPanel.setPreferredSize(new java.awt.Dimension(700, 50));
        buttonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdd.setBackground(new java.awt.Color(16, 185, 129));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("UPDATE GRADE");
        buttonPanel.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 130, 30));

        btnDelete.setBackground(new java.awt.Color(220, 38, 38));
        btnDelete.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("DELETE");
        buttonPanel.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 100, 30));

        btnSaveDefault.setBackground(new java.awt.Color(59, 130, 246));
        btnSaveDefault.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        btnSaveDefault.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveDefault.setText("LOAD DEFAULT");
        btnSaveDefault.setToolTipText("");
        buttonPanel.add(btnSaveDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 130, 30));

        add(buttonPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSaveDefault;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblGrading;
    // End of variables declaration//GEN-END:variables
}
