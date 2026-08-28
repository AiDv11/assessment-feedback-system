/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panels;

import javax.swing.*;
import java.awt.*;


public abstract class BaseDialog extends JDialog {
    
   
    protected static final Color ERROR_COLOR = new Color(254, 202, 202);
    protected static final Color DEFAULT_COLOR = Color.WHITE;
    protected static final Color BORDER_ERROR_COLOR = new Color(239, 68, 68);
    protected static final Color BORDER_DEFAULT_COLOR = new Color(209, 213, 219);

    public BaseDialog(Window parent, String title) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
    }
    

    protected void setError(JTextField field, boolean isError) {
        if (isError) {
            field.setBackground(ERROR_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_ERROR_COLOR, 1));
        } else {
            field.setBackground(DEFAULT_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
        }
    }
    
  
    protected void setError(JPasswordField field, boolean isError) {
        if (isError) {
            field.setBackground(ERROR_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_ERROR_COLOR, 1));
        } else {
            field.setBackground(DEFAULT_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
        }
    }
    
   
    protected abstract void initializeComponents();
    
  
    protected abstract boolean validateInput();
    
  
    protected abstract void submitForm();
}