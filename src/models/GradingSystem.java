/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import utils.FileManager;
import java.util.List;
import java.util.ArrayList;


public class GradingSystem {
    public static final String GRADING_FILE = "data/grading.txt";
    
 
    private static List<GradingSystem> allGrades = new ArrayList<>();
    
    private String grade;
    private double minMark;
    private double maxMark;
    
    public GradingSystem(String grade, double minMark, double maxMark) {
        this.grade = grade;
        this.minMark = minMark;
        this.maxMark = maxMark;
    }
    
   
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public double getMinMark() { return minMark; }
    public void setMinMark(double minMark) { this.minMark = minMark; }
    
    public double getMaxMark() { return maxMark; }
    public void setMaxMark(double maxMark) { this.maxMark = maxMark; }
    

    public static String calculateGrade(double mark) {
       
        if (allGrades.isEmpty()) {
            loadAllGrades();
        }
        
      
        for (GradingSystem gs : allGrades) {
            if (mark >= gs.getMinMark() && mark <= gs.getMaxMark()) {
                return gs.getGrade();
            }
        }
        
  
        if (mark >= 80) return "A+";
        else if (mark >= 75) return "A";
        else if (mark >= 70) return "B+";
        else if (mark >= 65) return "B";
        else if (mark >= 60) return "C+";
        else if (mark >= 55) return "C";
        else if (mark >= 50) return "C-";
        else if (mark >= 40) return "D";
        else if (mark >= 30) return "F+";
        else if (mark >= 20) return "F";
        else return "F-";
    }
    

    public String toFileString() {
        return grade + "|" + minMark + "|" + maxMark;
    }
    

    public static GradingSystem fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 3) {
            try {
                return new GradingSystem(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    

    public boolean saveToFile() {
        if (FileManager.appendToFile(GRADING_FILE, this.toFileString())) {
            allGrades.add(this); 
            return true;
        }
        return false;
    }
    

    public boolean updateInFile() {
      
        for (int i = 0; i < allGrades.size(); i++) {
            if (allGrades.get(i).getGrade().equals(this.grade)) {
                allGrades.set(i, this);
                break;
            }
        }
        
       
        return saveAllToFile();
    }
    

    public boolean deleteFromFile() {
        allGrades.remove(this);
        return saveAllToFile();
    }
    

    public static void loadAllGrades() {
        allGrades.clear();
        List<String> lines = FileManager.readFile(GRADING_FILE);
        for (String line : lines) {
            GradingSystem gs = fromFileString(line);
            if (gs != null) {
                allGrades.add(gs);
            }
        }
    }

    public static List<GradingSystem> getAllGrades() {
        if (allGrades.isEmpty()) {
            loadAllGrades();
        }
        return new ArrayList<>(allGrades); // Return copy
    }
    

    public static GradingSystem findByGrade(String grade) {
        if (allGrades.isEmpty()) {
            loadAllGrades();
        }
        for (GradingSystem gs : allGrades) {
            if (gs.getGrade().equals(grade)) {
                return gs;
            }
        }
        return null;
    }

    private static boolean saveAllToFile() {
        List<String> lines = new ArrayList<>();
        for (GradingSystem gs : allGrades) {
            lines.add(gs.toFileString());
        }
        return FileManager.writeFile(GRADING_FILE, lines);
    }
}