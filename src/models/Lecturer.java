package models;



import gui.LecturerDashboard;
import interfaces.Feedbackable;
import utils.FileManager;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import models.User;

public class Lecturer extends User implements Feedbackable {
    

    public Lecturer(String userID, String username, String password, String name, String gender, String email, String phone, String dateOfBirth) {
        
        super(userID, username, password, name, gender, email, phone, dateOfBirth, "lecturer");
    }
    
    
    
    @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            LecturerDashboard menu = new LecturerDashboard(this);
            menu.setVisible(true);
        });
    }
    
    

    @Override
    public void editProfile() {

        System.out.println("Opening Edit Profile panel for: " + getName());
    }
    
    
   
    @Override
    public void provideFeedback(String studentID, String assessmentID, String comment) {
       
        String feedbackID = FileManager.generateNextID("data/feedback.txt", "FDB");
        
  
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateGiven = today.format(formatter);
        

        String feedbackLine = feedbackID + "|" + studentID + "|" + assessmentID + "|" + 
                             this.getUserID() + "|" + comment + "|" + dateGiven;
        
      
        boolean success = FileManager.appendToFile("data/feedback.txt", feedbackLine);
        
        if (success) {
            System.out.println("Feedback provided successfully!");
        } else {
            System.err.println("Error: Failed to save feedback.");
        }
    }

    public void updateProfile(String name, String email, String phone) {
    setName(name);
    setEmail(email);
    setPhone(phone);
    }

public void updateProfileWithPassword(String name, String email, String phone, String password) {
    setName(name);
    setEmail(email);
    setPhone(phone);
    setPassword(password);
}



    
    @Override
    public void viewFeedback() {
        List<String> feedbackLines = FileManager.readFile("data/feedback.txt");
        
        System.out.println("\n========== MY FEEDBACK ==========");
        boolean found = false;
        
        for (String line : feedbackLines) {
            String[] parts = line.split("\\|");

            
            if (parts.length >= 6 && parts[3].equals(this.getUserID())) {
                found = true;
                System.out.println("Feedback ID: " + parts[0]);
                System.out.println("Student ID: " + parts[1]);
                System.out.println("Assessment ID: " + parts[2]);
                System.out.println("Feedback: " + parts[4]);
                System.out.println("Date: " + parts[5]);
                System.out.println("----------------------------------");
            }
        }
        
        if (!found) {
            System.out.println("No feedback found.");
        }
        System.out.println("=================================");
    }

    public List<String> getMyAssessments() {
        List<String> allAssessments = FileManager.readFile("data/assessments.txt");
        List<String> myAssessments = new ArrayList<>();
        
        for (String line : allAssessments) {
            String[] parts = line.split("\\|");
           
            
            if (parts.length >= 7 && parts[6].equals(this.getUserID())) {
                myAssessments.add(line);
            }
        }
        
        return myAssessments;
    }
    

    public List<String> getAssignedModules() {
        List<String> assignments = FileManager.readFile("data/lecturerAssignments.txt");
        List<String> moduleCodes = new ArrayList<>();
        
        for (String line : assignments) {
            String[] parts = line.split("\\|");
         
            
            if (parts.length >= 3 && parts[1].equals(this.getUserID())) {
                moduleCodes.add(parts[2]); // Add module code
            }
        }
        
        return moduleCodes;
    }
    

    public boolean enterMarks(String studentID, String assessmentID, double marksObtained, String grade) {

            String markID = FileManager.generateNextID("data/marks.txt", "MRK");
         java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateEntered = today.format(fmt);


            String markLine = markID + "|" + studentID + "|" + assessmentID + "|" +
                              marksObtained + "|" + grade + "|" + this.getUserID() + "|" + dateEntered;

            return FileManager.appendToFile("data/marks.txt", markLine);
        }
            java.time.LocalDate today = java.time.LocalDate.now();
   
        
    public boolean createAssessment(String moduleCode,
                                String assessmentName,
                                String assessmentType,
                                int totalMarks,
                                double weightage,
                                LocalDate date) {

    String assessmentID = FileManager.generateNextID("data/assessments.txt", "ASS");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String createdDate = date.format(formatter);

  
    String assessmentLine = assessmentID + "|" + moduleCode + "|" + assessmentName + "|" +
                            assessmentType + "|" + totalMarks + "|" + weightage + "|" +
                            this.getUserID() + "|" + createdDate;

    return FileManager.appendToFile("data/assessments.txt", assessmentLine);
}

    public boolean enterMarks(String studentID, String assessmentID, double marksObtained) {

        String markID = FileManager.generateNextID("data/marks.txt", "MRK");
        

        String grade = calculateGrade(marksObtained);
        
       
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateEntered = today.format(formatter);
        

        String markLine = markID + "|" + studentID + "|" + assessmentID + "|" + 
                         marksObtained + "|" + grade + "|" + this.getUserID() + "|" + dateEntered;
        
      
        return FileManager.appendToFile("data/marks.txt", markLine);
    }
    

    private String calculateGrade(double marks) {
        
        List<String> gradingLines = FileManager.readFile("data/grading.txt");
        
        for (String line : gradingLines) {
            String[] parts = line.split("\\|");
           
            
            if (parts.length >= 3) {
                try {
                    double minMarks = Double.parseDouble(parts[1]);
                    double maxMarks = Double.parseDouble(parts[2]);
                    
                    if (marks >= minMarks && marks <= maxMarks) {
                        return parts[0]; 
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        
   
        return "F";
    }
    

    public String getDisplayInfo() {
        return "Lecturer ID: " + getUserID() + "\n" +
               "Name: " + getName() + "\n" +
               "Gender: " + getGender() + "\n" +
               "Email: " + getEmail() + "\n" +
               "Phone: " + getPhone() + "\n" +
               "Date of Birth: " + getDateOfBirth() + "\n" +
               "Age: " + getAge();
    }
    

    public boolean isAssignedToModule(String moduleCode) {
        List<String> assignedModules = getAssignedModules();
        return assignedModules.contains(moduleCode);
    }
}