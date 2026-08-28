package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hussain Alkhaldi
 */
import interfaces.Loginable;
import interfaces.Editable;
import java.util.ArrayList;
import utils.FileManager;
import java.util.List;

public abstract class User implements Loginable, Editable {
    
   private static List<User> allUsers = new ArrayList<>();

    private String userID;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String dateOfBirth; 
    private String userType;
    
    
    public User(String userID, String username, String password, String name, 
            String gender, String email, String phone, String dateOfBirth, String userType) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.userType = userType;
    }
    

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    

    public abstract void showMenu();
    

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    @Override
    public void logout() {
        System.out.println(name + " has logged out.");
    }
    
    @Override
    public abstract void editProfile();
   
    

    public String toFileString() {
        return userID + "|" + username + "|" + password + "|" + name + "|" + 
                        gender + "|" + email + "|" + phone + "|" + dateOfBirth + "|" + userType;
    }

    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 9) {
            String id = parts[0];
            String user = parts[1];
            String pass = parts[2];
            String n = parts[3];
            String g = parts[4];
            String e = parts[5];
            String p = parts[6];
            String dob = parts[7];
            String type = parts[8];
            
            switch (type.toLowerCase()) {
                case "admin":
                    return new AdminStaff(id, user, pass, n, g, e, p, dob);
                case "academic leader":
                    return new AcademicLeader(id, user, pass, n, g, e, p, dob);
                case "lecturer":
                    return new Lecturer(id, user, pass, n, g, e, p, dob);
                case "student":
                    return new Student(id, user, pass, n, g, e, p, dob);
                default:
                    return null;
            }
        }
        return null;
    }
    

    public String findRawLine() {
        List<String> lines = FileManager.readFile(FileManager.USERS_FILE);
        for (String line : lines) {
            if (line.startsWith(this.userID + "|")) {
                return line;
            }
        }
        return null;
    }
    

public void updateProfile(String name, String gender, String email, String phone) {
    this.name = name;
    this.gender = gender;
    this.email = email;
    this.phone = phone;
}
    


public boolean saveToFile() {
    if (FileManager.appendToFile(FileManager.USERS_FILE, this.toFileString())) {
        allUsers.add(this); 
        return true;
    }
    return false;
}



public boolean updateInFile(String oldRawLine) {
  
    for (int i = 0; i < allUsers.size(); i++) {
        if (allUsers.get(i).getUserID().equals(this.userID)) {
            allUsers.set(i, this);
            break;
        }
    }
    
 
    return saveAllToFile();
}



public boolean deleteFromFile() {
    allUsers.remove(this);
    return saveAllToFile();
}

    public int getAge() {
    try {
        String[] parts = dateOfBirth.split("-");
        int birthYear = Integer.parseInt(parts[0]);
        int birthMonth = Integer.parseInt(parts[1]);
        int birthDay = Integer.parseInt(parts[2]);
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate birthDate = java.time.LocalDate.of(birthYear, birthMonth, birthDay);
        
        return java.time.Period.between(birthDate, today).getYears();
    } catch (Exception e) {
        return 0;
    }
}
    
public static void loadAllUsers() {
    allUsers.clear();
    List<String> lines = FileManager.readFile(FileManager.USERS_FILE);
    for (String line : lines) {
        User user = fromFileString(line);
        if (user != null) {
            allUsers.add(user);
        }
    }
}


public static List<User> getAllUsers() {
    if (allUsers.isEmpty()) {
        loadAllUsers();
    }
    return new ArrayList<>(allUsers);
}


public static User findByID(String userID) {
    if (allUsers.isEmpty()) {
        loadAllUsers();
    }
    for (User user : allUsers) {
        if (user.getUserID().equals(userID)) {
            return user;
        }
    }
    return null;
}


public static User findByUsername(String username) {
    if (allUsers.isEmpty()) {
        loadAllUsers();
    }
    for (User user : allUsers) {
        if (user.getUsername().equals(username)) {
            return user;
        }
    }
    return null;
}


public static boolean usernameExists(String username) {
    return findByUsername(username) != null;
}


private static boolean saveAllToFile() {
    List<String> lines = new ArrayList<>();
    for (User user : allUsers) {
        lines.add(user.toFileString());
    }
    return FileManager.writeFile(FileManager.USERS_FILE, lines);
    }
}