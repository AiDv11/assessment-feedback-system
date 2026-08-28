/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import models.*;



public class AuthManager {
    
   
    public static User login(String username, String password) {
    User user = User.findByUsername(username);
    if (user != null && user.getPassword().equals(password)) {
        return user;
    }
    return null;
}
    
   
    private static User createUser(String[] parts) {
        String userID = parts[0];
        String username = parts[1];
        String password = parts[2];
        String name = parts[3];
        String gender = parts[4];
        String email = parts[5];
        String phone = parts[6];
        String dob = parts[7];
        String userType = parts[8];
        
   
        switch (userType.toLowerCase()) {
            case "admin":
                return new AdminStaff(userID, username, password, name, gender, email, phone, dob);
            case "academic leader":
                return new AcademicLeader(userID, username, password, name, gender, email, phone, dob);
            case "lecturer":
                return new Lecturer(userID, username, password, name, gender, email, phone, dob);
            case "student":
                return new Student(userID, username, password, name, gender, email, phone, dob);
            default:
                return null;
        }
    }
    
 
    public static boolean usernameExists(String username) {
    return User.usernameExists(username);
}
    
 
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
       
public static User findUserByID(String userID) {
    return User.findByID(userID);
}
    
  
        public static User createNewUser(String userID, String username, String password,
        String name, String gender, String email, String phone, String dateOfBirth, String userType) {
        String[] parts = {userID, username, password, name, gender, email, phone, 
                 dateOfBirth, userType};
        return createUser(parts);
    }
}
