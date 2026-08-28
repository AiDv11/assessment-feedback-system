/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import interfaces.Creatable;
import interfaces.Editor;
import javax.swing.JPanel;
import javax.swing.JOptionPane;


public class PanelManager {
    
    
    public static void handleCreate(JPanel panel) {
        if (panel instanceof Creatable) {
            
            ((Creatable) panel).createNew();
        } else {
            JOptionPane.showMessageDialog(panel, 
                "This panel does not support creation functionality",
                "Not Supported", JOptionPane.WARNING_MESSAGE);
        }
    }
    
  
    public static void handleEdit(JPanel panel) {
        if (panel instanceof Editor) {
          
            ((Editor) panel).saveChanges();
        } else {
            JOptionPane.showMessageDialog(panel, 
                "This panel does not support editing functionality",
                "Not Supported", JOptionPane.WARNING_MESSAGE);
        }
    }
    
 
    public static void handleRefresh(JPanel panel) {
        if (panel instanceof Editor) {
           
            ((Editor) panel).loadData();
        } else {
            JOptionPane.showMessageDialog(panel, 
                "This panel does not support refresh functionality",
                "Not Supported", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    public static void handleAction(JPanel panel, String action) {
        switch (action.toLowerCase()) {
            case "create":
                handleCreate(panel);
                break;
            case "edit":
            case "save":
                handleEdit(panel);
                break;
            case "refresh":
            case "load":
                handleRefresh(panel);
                break;
            default:
                JOptionPane.showMessageDialog(panel,
                    "Unknown action: " + action,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
  
    public static boolean supportsCreation(JPanel panel) {
        return panel instanceof Creatable;
    }
    
   
    public static boolean supportsEditing(JPanel panel) {
        return panel instanceof Editor;
    }
}