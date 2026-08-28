/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import forms.AdminMenu;
import javax.swing.SwingUtilities;
import interfaces.Editable;



public class AdminStaff extends User implements Editable{
    
    public AdminStaff(String userID, String username, String password, String name,
                      String gender, String email, String phone, String dateOfBirth) {
        super(userID, username, password, name, gender, email, phone, dateOfBirth, "admin");
    }

    @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            AdminMenu menu = new AdminMenu(this);
            menu.setVisible(true);
        });
    }
    

    @Override
    public void editProfile() {
        System.out.println("Admin editing profile...");
      
    }
    
}
