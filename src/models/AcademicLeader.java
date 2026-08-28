/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package models;
import afs.users.AcademicLeaderMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author Abdulrahman
 */
public class AcademicLeader extends User {
    
    public AcademicLeader(String userID, String username, String password, String name,
                          String gender, String email, String phone, String dateOfBirth) {
        super(userID, username, password, name, gender, email, phone, dateOfBirth, "academic leader");
    }
   
   
     @Override
    public void editProfile() {
        System.out.println("Academic Leader editing profile...");
    }

   
  @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            AcademicLeaderMenu menu = new AcademicLeaderMenu(this);
            menu.setVisible(true);
        });
    }

    
    
    
}
