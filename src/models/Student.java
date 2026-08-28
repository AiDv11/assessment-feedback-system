/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import forms.StudentMenu;
import javax.swing.SwingUtilities;


public class Student extends User {
    
    public Student(String userID, String username, String password, String name,
                   String gender, String email, String phone, String dateOfBirth) {
        super(userID, username, password, name, gender, email, phone, dateOfBirth, "student");
    }
    

    @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            StudentMenu menu = new StudentMenu(this);
            menu.setVisible(true);
        });
    }

    @Override
    public void editProfile() {
        System.out.println("Student editing profile...");
    }
    

}

