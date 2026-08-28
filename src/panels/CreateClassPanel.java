/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import models.ClassGroup;
import models.Module;
import utils.FileManager;
import interfaces.Creatable;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author Hussain Alkhaldi
 */
public class CreateClassPanel extends javax.swing.JPanel implements Creatable {

   
    public CreateClassPanel() {
        initComponents();
        loadModules();
        addNoModuleCheckbox();
    }
    
    private void loadModules() {
        cmbModule.removeAllItems();
        List<String> modules = FileManager.readFile(Module.MODULE_FILE);
        
        if (modules.isEmpty()) {
            cmbModule.addItem("No modules available - Create modules first");
        } else {
            for (String line : modules) {
                Module mod = Module.fromFileString(line);
                if (mod != null) {
                    cmbModule.addItem(mod.getModuleID() + " - " + mod.getModuleName());
                }
            }
        }
    }
    
    
    public void createNew() {
    try {
        String className = txtClassName.getText().trim();
        
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a class name",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String moduleID = null;
        String moduleCode = "TBA";
        String moduleName = "To Be Assigned";
        boolean hasModule = true;
        
        
        Component[] components = panel1.getComponents();
        for (Component comp : components) {
            if (comp instanceof javax.swing.JCheckBox) {
                javax.swing.JCheckBox chk = (javax.swing.JCheckBox) comp;
                if (chk.isSelected()) {
                    moduleID = "NO_MODULE";
                    hasModule = false;
                    break;
                }
            }
        }
        
       
        if (hasModule) {
            if (cmbModule.getSelectedItem() == null || 
                ((String) cmbModule.getSelectedItem()).contains("No modules")) {
                JOptionPane.showMessageDialog(this, 
                    "Please create modules first or check 'No Module'",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedModule = (String) cmbModule.getSelectedItem();
            moduleID = selectedModule.split(" - ")[0];
            
         
            List<String> modules = FileManager.readFile(Module.MODULE_FILE);
            for (String line : modules) {
                Module mod = Module.fromFileString(line);
                if (mod != null && mod.getModuleID().equals(moduleID)) {
                    moduleCode = mod.getModuleCode();
                    moduleName = mod.getModuleName();
                    break;
                }
            }
        }
        
        String classID = FileManager.generateNextID(ClassGroup.CLASS_FILE, "C");
        ClassGroup classGroup = new ClassGroup(classID, moduleID, className, 
                                               moduleCode, moduleName, "L001", "TBA");
        
        if (FileManager.appendToFile(ClassGroup.CLASS_FILE, classGroup.toFileString())) {
            String message = hasModule 
                ? "Class created successfully!\nClass ID: " + classID
                : "Class created successfully!\nClass ID: " + classID + "\nNote: No module assigned yet.";
                
            JOptionPane.showMessageDialog(this, message,
                "Success", JOptionPane.INFORMATION_MESSAGE);
            txtClassName.setText("");
            
       
            for (Component comp : panel1.getComponents()) {
                if (comp instanceof javax.swing.JCheckBox) {
                    ((javax.swing.JCheckBox) comp).setSelected(false);
                    cmbModule.setEnabled(true);
                }
            }
        } else {
            throw new Exception("Failed to save class data");
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error creating class: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updateClass() {
        java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(this);
        UpdateClassDialog dialog = new UpdateClassDialog(parent);
        dialog.setVisible(true);
    }
    private void renameClass() {
    java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(this);
    RenameClassDialog dialog = new RenameClassDialog(parent);
    dialog.setVisible(true);
}
    private void deleteClass() {
    java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(this);
    DeleteClassDialog dialog = new DeleteClassDialog(parent);
    dialog.setVisible(true);
}

    
    private class UpdateClassDialog extends javax.swing.JDialog {

        private javax.swing.JComboBox<String> cmbSelectClass;
        private javax.swing.JComboBox<String> cmbNewModule;
        private javax.swing.JLabel lblError;
        private javax.swing.JButton btnUpdate;

        private static final java.awt.Color BORDER_DEFAULT_COLOR = new java.awt.Color(209, 213, 219);
        private static final java.awt.Color BORDER_ERROR_COLOR  = new java.awt.Color(239, 68, 68);
        private static final java.awt.Color ERROR_COLOR          = new java.awt.Color(254, 202, 202);

        public UpdateClassDialog(java.awt.Window parent) {
            super(parent, "Update Class Module", ModalityType.APPLICATION_MODAL);
            initDialogComponents();
            loadClasses();
            loadModulesForDialog();
        }

        private void initDialogComponents() {
            setSize(500, 400);
            setLocationRelativeTo(CreateClassPanel.this);
            setResizable(false);
            setLayout(new java.awt.BorderLayout());

        
            javax.swing.JPanel headerPanel = new javax.swing.JPanel();
            headerPanel.setBackground(new java.awt.Color(16, 185, 129));
            headerPanel.setPreferredSize(new java.awt.Dimension(500, 55));
            headerPanel.setLayout(new AbsoluteLayout());

            javax.swing.JLabel lblTitle = new javax.swing.JLabel("Update Class");
            lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            lblTitle.setForeground(java.awt.Color.WHITE);
            headerPanel.add(lblTitle, new AbsoluteConstraints(25, 12, 350, 30));

            add(headerPanel, java.awt.BorderLayout.NORTH);

           
            javax.swing.JPanel bodyPanel = new javax.swing.JPanel();
            bodyPanel.setBackground(new java.awt.Color(243, 244, 246));
            bodyPanel.setLayout(new AbsoluteLayout());

            
            javax.swing.JLabel lblSelectClass = new javax.swing.JLabel("Select Class:");
            lblSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            lblSelectClass.setForeground(new java.awt.Color(55, 65, 81));
            bodyPanel.add(lblSelectClass, new AbsoluteConstraints(30, 20, 420, 25));

            cmbSelectClass = new javax.swing.JComboBox<>();
            cmbSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
            bodyPanel.add(cmbSelectClass, new AbsoluteConstraints(30, 48, 420, 35));

            
            javax.swing.JLabel lblCurrentModule = new javax.swing.JLabel("Current Module:");
            lblCurrentModule.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 12));
            lblCurrentModule.setForeground(new java.awt.Color(107, 114, 128));
            bodyPanel.add(lblCurrentModule, new AbsoluteConstraints(30, 88, 420, 22));

            
            cmbSelectClass.addActionListener(e -> {
                String selected = (String) cmbSelectClass.getSelectedItem();
                if (selected != null && selected.contains(" — ")) {
                    String currentMod = selected.split(" — ").length > 1 ? selected.split(" — ")[1] : "";
                    lblCurrentModule.setText("Current Module: " + currentMod);
                } else {
                    lblCurrentModule.setText("Current Module: —");
                }
                lblError.setText("");
            });

           
            javax.swing.JLabel lblNewModule = new javax.swing.JLabel("Change To:");
            lblNewModule.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            lblNewModule.setForeground(new java.awt.Color(55, 65, 81));
            bodyPanel.add(lblNewModule, new AbsoluteConstraints(30, 125, 420, 25));

            cmbNewModule = new javax.swing.JComboBox<>();
            cmbNewModule.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
            bodyPanel.add(cmbNewModule, new AbsoluteConstraints(30, 153, 420, 35));

          
            lblError = new javax.swing.JLabel("");
            lblError.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
            lblError.setForeground(new java.awt.Color(239, 68, 68));
            bodyPanel.add(lblError, new AbsoluteConstraints(30, 198, 420, 22));

            add(bodyPanel, java.awt.BorderLayout.CENTER);

       
            javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
            buttonPanel.setBackground(new java.awt.Color(16, 185, 129));
            buttonPanel.setPreferredSize(new java.awt.Dimension(500, 50));
            buttonPanel.setLayout(new AbsoluteLayout());

            btnUpdate = new javax.swing.JButton("Update");
            btnUpdate.setBackground(new java.awt.Color(59, 130, 246));
            btnUpdate.setForeground(java.awt.Color.WHITE);
            btnUpdate.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            btnUpdate.setFocusPainted(false);
            btnUpdate.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            btnUpdate.addActionListener(e -> submitUpdate());
            buttonPanel.add(btnUpdate, new AbsoluteConstraints(170, 10, 140, 32));

            javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
            btnCancel.setBackground(new java.awt.Color(156, 163, 175));
            btnCancel.setForeground(java.awt.Color.WHITE);
            btnCancel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            btnCancel.setFocusPainted(false);
            btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            btnCancel.addActionListener(e -> dispose());
            buttonPanel.add(btnCancel, new AbsoluteConstraints(335, 10, 100, 32));

            add(buttonPanel, java.awt.BorderLayout.SOUTH);
        }

private void loadClasses() {
    cmbSelectClass.removeAllItems();
    List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);

    if (classes.isEmpty()) {
        cmbSelectClass.addItem("No classes available");
        return;
    }

    
    java.util.Map<String, String> moduleNames = new java.util.LinkedHashMap<>();
    for (String line : FileManager.readFile(Module.MODULE_FILE)) {
        Module mod = Module.fromFileString(line);
        if (mod != null) {
            moduleNames.put(mod.getModuleID(), mod.getModuleName());
        }
    }

    for (String line : classes) {
        ClassGroup cg = ClassGroup.fromFileString(line);
        if (cg != null) {
            String modName;
            if (cg.getModuleID().equals("NO_MODULE")) {
                modName = "No Module";
            } else {
                modName = moduleNames.getOrDefault(cg.getModuleID(), cg.getModuleID());
            }
           
            cmbSelectClass.addItem(cg.getClassID() + " - " + cg.getClassName() + " — " + modName);
        }
    }
}

private void loadModulesForDialog() {
    cmbNewModule.removeAllItems();
    

    cmbNewModule.addItem("NO_MODULE - Remove Module");
    
    List<String> modules = FileManager.readFile(Module.MODULE_FILE);

    if (modules.isEmpty()) {
    
        return;
    }

    for (String line : modules) {
        Module mod = Module.fromFileString(line);
        if (mod != null) {
            cmbNewModule.addItem(mod.getModuleID() + " - " + mod.getModuleName());
        }
    }
}

        private void submitUpdate() {
    lblError.setText("");

    String selectedClass = (String) cmbSelectClass.getSelectedItem();
    String selectedModule = (String) cmbNewModule.getSelectedItem();

    if (selectedClass == null || selectedClass.equals("No classes available")) {
        lblError.setText("No classes to update.");
        return;
    }
    if (selectedModule == null || selectedModule.equals("No modules available")) {
        lblError.setText("No modules available. Create a module first.");
        return;
    }


    String classID = selectedClass.split(" - ")[0].trim();

    
    String newModuleID = selectedModule.split(" - ")[0].trim();

   
    List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);
    String oldLine = null;
    ClassGroup targetClass = null;

    for (String line : classes) {
        ClassGroup cg = ClassGroup.fromFileString(line);
        if (cg != null && cg.getClassID().equals(classID)) {
            oldLine = line;
            targetClass = cg;
            break;
        }
    }

    if (oldLine == null || targetClass == null) {
        lblError.setText("Class not found in file.");
        return;
    }

    
    if (targetClass.getModuleID().equals(newModuleID)) {
        JOptionPane.showMessageDialog(this,
            "This class is already assigned to this module.\nPlease select a different module.",
            "Already Assigned", JOptionPane.WARNING_MESSAGE);
        lblError.setText("Class is already on this module.");
        return;
    }

  
    String newModuleCode = "TBA";
    String newModuleName = "To Be Assigned";
    
    if (!newModuleID.equals("NO_MODULE")) {
        List<String> modules = FileManager.readFile(Module.MODULE_FILE);
        for (String modLine : modules) {
            Module mod = Module.fromFileString(modLine);
            if (mod != null && mod.getModuleID().equals(newModuleID)) {
                newModuleCode = mod.getModuleCode();
                newModuleName = mod.getModuleName();
                break;
            }
        }
    }

   
    ClassGroup updated = new ClassGroup(
        targetClass.getClassID(), 
        newModuleID, 
        targetClass.getClassName(),
        newModuleCode,
        newModuleName,
        targetClass.getLecturerID(),
        targetClass.getSchedule()
    );
    FileManager.updateLine(ClassGroup.CLASS_FILE, oldLine, updated.toFileString());

    JOptionPane.showMessageDialog(this,
            "Class " + classID + " updated to " + selectedModule + " successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    dispose();
}
    }
   
private class RenameClassDialog extends javax.swing.JDialog {

    private javax.swing.JComboBox<String> cmbSelectClass;
    private javax.swing.JTextField txtNewName;
    private javax.swing.JLabel lblError;

    public RenameClassDialog(java.awt.Window parent) {
        super(parent, "Rename Class", ModalityType.APPLICATION_MODAL);
        initDialogComponents();
        loadClasses();
    }

    private void initDialogComponents() {
        setSize(500, 400);
        setLocationRelativeTo(CreateClassPanel.this);
        setResizable(false);
        setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 158, 11));
        headerPanel.setPreferredSize(new java.awt.Dimension(500, 55));
        headerPanel.setLayout(new AbsoluteLayout());

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("Rename Class");
        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        lblTitle.setForeground(java.awt.Color.WHITE);
        headerPanel.add(lblTitle, new AbsoluteConstraints(25, 12, 350, 30));

        add(headerPanel, java.awt.BorderLayout.NORTH);

        
        javax.swing.JPanel bodyPanel = new javax.swing.JPanel();
        bodyPanel.setBackground(new java.awt.Color(243, 244, 246));
        bodyPanel.setLayout(new AbsoluteLayout());

      
        javax.swing.JLabel lblSelectClass = new javax.swing.JLabel("Select Class:");
        lblSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblSelectClass.setForeground(new java.awt.Color(55, 65, 81));
        bodyPanel.add(lblSelectClass, new AbsoluteConstraints(30, 20, 420, 25));

        cmbSelectClass = new javax.swing.JComboBox<>();
        cmbSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        bodyPanel.add(cmbSelectClass, new AbsoluteConstraints(30, 48, 420, 35));

      
        javax.swing.JLabel lblNewName = new javax.swing.JLabel("New Name:");
        lblNewName.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblNewName.setForeground(new java.awt.Color(55, 65, 81));
        bodyPanel.add(lblNewName, new AbsoluteConstraints(30, 100, 420, 25));

        txtNewName = new javax.swing.JTextField();
        txtNewName.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        txtNewName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        bodyPanel.add(txtNewName, new AbsoluteConstraints(30, 128, 420, 35));

       
        lblError = new javax.swing.JLabel("");
        lblError.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        lblError.setForeground(new java.awt.Color(239, 68, 68));
        bodyPanel.add(lblError, new AbsoluteConstraints(30, 173, 420, 22));

        add(bodyPanel, java.awt.BorderLayout.CENTER);

      
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.setBackground(new java.awt.Color(245, 158, 11));
        buttonPanel.setPreferredSize(new java.awt.Dimension(500, 50));
        buttonPanel.setLayout(new AbsoluteLayout());

        javax.swing.JButton btnRename = new javax.swing.JButton("Rename");
        btnRename.setBackground(new java.awt.Color(59, 130, 246));
        btnRename.setForeground(java.awt.Color.WHITE);
        btnRename.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnRename.setFocusPainted(false);
        btnRename.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnRename.addActionListener(e -> submitRename());
        buttonPanel.add(btnRename, new AbsoluteConstraints(170, 10, 140, 32));

        javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
        btnCancel.setBackground(new java.awt.Color(156, 163, 175));
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnCancel, new AbsoluteConstraints(335, 10, 100, 32));

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }

    private void loadClasses() {
        cmbSelectClass.removeAllItems();
        List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);

        if (classes.isEmpty()) {
            cmbSelectClass.addItem("No classes available");
            return;
        }

        for (String line : classes) {
            ClassGroup cg = ClassGroup.fromFileString(line);
            if (cg != null) {
                cmbSelectClass.addItem(cg.getClassID() + " - " + cg.getClassName());
            }
        }
    }

    private void submitRename() {
        lblError.setText("");

        String selectedClass = (String) cmbSelectClass.getSelectedItem();
        String newName = txtNewName.getText().trim();

        if (selectedClass == null || selectedClass.equals("No classes available")) {
            lblError.setText("No classes to rename.");
            return;
        }

        if (newName.isEmpty()) {
            lblError.setText("Please enter a new name.");
            return;
        }

        String classID = selectedClass.split(" - ")[0].trim();

       
        List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);
        String oldLine = null;
        ClassGroup targetClass = null;

        for (String line : classes) {
            ClassGroup cg = ClassGroup.fromFileString(line);
            if (cg != null && cg.getClassID().equals(classID)) {
                oldLine = line;
                targetClass = cg;
                break;
            }
        }

        if (oldLine == null || targetClass == null) {
            lblError.setText("Class not found.");
            return;
        }

   
        ClassGroup updated = new ClassGroup(
            targetClass.getClassID(), 
            targetClass.getModuleID(), 
            newName,
            targetClass.getModuleCode(),
            targetClass.getModuleName(),
            targetClass.getLecturerID(),
            targetClass.getSchedule()
        );
        FileManager.updateLine(ClassGroup.CLASS_FILE, oldLine, updated.toFileString());

        JOptionPane.showMessageDialog(this,
                "Class renamed successfully!\n" + classID + " is now: " + newName,
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}

   
private class DeleteClassDialog extends javax.swing.JDialog {

    private javax.swing.JComboBox<String> cmbSelectClass;
    private javax.swing.JLabel lblWarning;

    public DeleteClassDialog(java.awt.Window parent) {
        super(parent, "Delete Class", ModalityType.APPLICATION_MODAL);
        initDialogComponents();
        loadClasses();
    }

    private void initDialogComponents() {
        setSize(500, 400);
        setLocationRelativeTo(CreateClassPanel.this);
        setResizable(false);
        setLayout(new java.awt.BorderLayout());

       
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(239, 68, 68));
        headerPanel.setPreferredSize(new java.awt.Dimension(500, 55));
        headerPanel.setLayout(new AbsoluteLayout());

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("Delete Class");
        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        lblTitle.setForeground(java.awt.Color.WHITE);
        headerPanel.add(lblTitle, new AbsoluteConstraints(25, 12, 350, 30));

        add(headerPanel, java.awt.BorderLayout.NORTH);

     
        javax.swing.JPanel bodyPanel = new javax.swing.JPanel();
        bodyPanel.setBackground(new java.awt.Color(243, 244, 246));
        bodyPanel.setLayout(new AbsoluteLayout());

     
        javax.swing.JLabel lblSelectClass = new javax.swing.JLabel("Select Class to Delete:");
        lblSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblSelectClass.setForeground(new java.awt.Color(55, 65, 81));
        bodyPanel.add(lblSelectClass, new AbsoluteConstraints(30, 20, 420, 25));

        cmbSelectClass = new javax.swing.JComboBox<>();
        cmbSelectClass.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        bodyPanel.add(cmbSelectClass, new AbsoluteConstraints(30, 48, 420, 35));


        lblWarning = new javax.swing.JLabel("⚠ This action cannot be undone!");
        lblWarning.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        lblWarning.setForeground(new java.awt.Color(239, 68, 68));
        bodyPanel.add(lblWarning, new AbsoluteConstraints(30, 95, 420, 25));

        add(bodyPanel, java.awt.BorderLayout.CENTER);

       
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.setBackground(new java.awt.Color(239, 68, 68));
        buttonPanel.setPreferredSize(new java.awt.Dimension(500, 50));
        buttonPanel.setLayout(new AbsoluteLayout());

        javax.swing.JButton btnDelete = new javax.swing.JButton("Delete");
        btnDelete.setBackground(new java.awt.Color(220, 38, 38));
        btnDelete.setForeground(java.awt.Color.WHITE);
        btnDelete.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnDelete.addActionListener(e -> submitDelete());
        buttonPanel.add(btnDelete, new AbsoluteConstraints(170, 10, 140, 32));

        javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
        btnCancel.setBackground(new java.awt.Color(156, 163, 175));
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnCancel, new AbsoluteConstraints(335, 10, 100, 32));

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }

    private void loadClasses() {
        cmbSelectClass.removeAllItems();
        List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);

        if (classes.isEmpty()) {
            cmbSelectClass.addItem("No classes available");
            return;
        }

        for (String line : classes) {
            ClassGroup cg = ClassGroup.fromFileString(line);
            if (cg != null) {
                cmbSelectClass.addItem(cg.getClassID() + " - " + cg.getClassName());
            }
        }
    }

    private void submitDelete() {
        String selectedClass = (String) cmbSelectClass.getSelectedItem();

        if (selectedClass == null || selectedClass.equals("No classes available")) {
            JOptionPane.showMessageDialog(this, "No classes to delete.", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String classID = selectedClass.split(" - ")[0].trim();

        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this class?\n\n" + selectedClass + 
                "\n\nThis action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

     
        List<String> classes = FileManager.readFile(ClassGroup.CLASS_FILE);
        for (String line : classes) {
            ClassGroup cg = ClassGroup.fromFileString(line);
            if (cg != null && cg.getClassID().equals(classID)) {
                if (FileManager.deleteLine(ClassGroup.CLASS_FILE, line)) {
                    JOptionPane.showMessageDialog(this,
                            "Class deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Failed to delete class.", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        panel1 = new javax.swing.JPanel();
        lblSelectModule = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox<>();
        lblClassName = new javax.swing.JLabel();
        txtClassName = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        btnCreate = new javax.swing.JButton();
        btnUpdateClass = new javax.swing.JButton();
        btnRenameClass = new javax.swing.JButton();
        btnDeleteClass = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1200, 620));
        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(16, 185, 129));
        headerPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Create New Class");
        headerPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 490, 40));

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        panel1.setBackground(new java.awt.Color(241, 243, 246));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSelectModule.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblSelectModule.setForeground(new java.awt.Color(55, 65, 81));
        lblSelectModule.setText("Select Module:");
        panel1.add(lblSelectModule, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 160, 40));

        cmbModule.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        panel1.add(cmbModule, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 640, 35));

        lblClassName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblClassName.setForeground(new java.awt.Color(55, 65, 81));
        lblClassName.setText("Class Name:");
        panel1.add(lblClassName, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 210, 160, 40));

        txtClassName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtClassName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(209, 213, 219)));
        panel1.add(txtClassName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 640, 35));

        add(panel1, java.awt.BorderLayout.CENTER);

        buttonPanel.setBackground(new java.awt.Color(243, 244, 246));
        buttonPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        buttonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCreate.setBackground(new java.awt.Color(16, 185, 129));
        btnCreate.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCreate.setForeground(new java.awt.Color(255, 255, 255));
        btnCreate.setText("CREATE CLASS");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        buttonPanel.add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 200, 40));

        btnUpdateClass.setBackground(new java.awt.Color(59, 130, 246));
        btnUpdateClass.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnUpdateClass.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateClass.setText("UPDATE CLASS");
        btnUpdateClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateClassActionPerformed(evt);
            }
        });
        buttonPanel.add(btnUpdateClass, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 200, 40));

        btnRenameClass.setBackground(new java.awt.Color(245, 158, 11));
        btnRenameClass.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRenameClass.setForeground(new java.awt.Color(255, 255, 255));
        btnRenameClass.setText("RENAME CLASS");
        btnRenameClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameClassActionPerformed(evt);
            }
        });
        buttonPanel.add(btnRenameClass, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 200, 40));

        btnDeleteClass.setBackground(new java.awt.Color(239, 68, 68));
        btnDeleteClass.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnDeleteClass.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteClass.setText("DELETE CLASS");
        btnDeleteClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClassActionPerformed(evt);
            }
        });
        buttonPanel.add(btnDeleteClass, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, 200, 40));

        add(buttonPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void addNoModuleCheckbox() {
    javax.swing.JCheckBox chkNoModule = new javax.swing.JCheckBox();
    chkNoModule.setFont(new java.awt.Font("Arial", 0, 14));
    chkNoModule.setText("Create class without module (assign later)");
    chkNoModule.setBackground(new java.awt.Color(241, 243, 246));
    chkNoModule.addActionListener(e -> {
        cmbModule.setEnabled(!chkNoModule.isSelected());
    });
    panel1.add(chkNoModule, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 155, 400, 25));
}
    
    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        createNew();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateClassActionPerformed
        updateClass();
    }//GEN-LAST:event_btnUpdateClassActionPerformed

    private void btnRenameClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameClassActionPerformed
        renameClass();
    }//GEN-LAST:event_btnRenameClassActionPerformed

    private void btnDeleteClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClassActionPerformed
        deleteClass();
    }//GEN-LAST:event_btnDeleteClassActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDeleteClass;
    private javax.swing.JButton btnRenameClass;
    private javax.swing.JButton btnUpdateClass;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox<String> cmbModule;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblClassName;
    private javax.swing.JLabel lblSelectModule;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panel1;
    private javax.swing.JTextField txtClassName;
    // End of variables declaration//GEN-END:variables
}
