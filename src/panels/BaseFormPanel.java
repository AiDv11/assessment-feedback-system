/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panels;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import java.awt.Color;


public abstract class BaseFormPanel extends JPanel {
    

    protected static final Color ERROR_COLOR = new Color(254, 202, 202);
    protected static final Color DEFAULT_COLOR = Color.WHITE;
    protected static final Color BORDER_ERROR_COLOR = new Color(239, 68, 68);
    protected static final Color BORDER_DEFAULT_COLOR = new Color(209, 213, 219);
    

    protected void setFieldError(JTextField field, boolean isError) {
        if (isError) {
            field.setBackground(ERROR_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_ERROR_COLOR, 1));
        } else {
            field.setBackground(DEFAULT_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
        }
    }
    

    protected void setFieldError(JPasswordField field, boolean isError) {
        if (isError) {
            field.setBackground(ERROR_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_ERROR_COLOR, 1));
        } else {
            field.setBackground(DEFAULT_COLOR);
            field.setBorder(BorderFactory.createLineBorder(BORDER_DEFAULT_COLOR, 1));
        }
    }
    

    protected boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    

    protected boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("^[0-9]{10,15}$");
    }

    protected boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s]+$");
    }

    protected boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }
    

    protected abstract boolean validateInput();
}