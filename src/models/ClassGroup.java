/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import utils.FileManager;
import java.util.List;
import java.util.ArrayList;


public class ClassGroup {

    public static final String CLASS_FILE = "data/classes.txt";
    

    private static List<ClassGroup> allClasses = new ArrayList<>();
    
    private String classID;
    private String moduleID;
    private String className;
    private String moduleCode;
    private String moduleName;
    private String lecturerID;
    private String schedule;
    
    public ClassGroup(String classID, String moduleID, String className) {
        this.classID = classID;
        this.moduleID = moduleID;
        this.className = className;
        this.moduleCode = "";
        this.moduleName = "";
        this.lecturerID = "L001";  
        this.schedule = "TBA";     
    }
    
    public ClassGroup(String classID, String moduleID, String className, 
                     String moduleCode, String moduleName, String lecturerID, String schedule) {
        this.classID = classID;
        this.moduleID = moduleID;
        this.className = className;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.lecturerID = lecturerID;
        this.schedule = schedule;
    }
    
   
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }
    
    public String getModuleID() { return moduleID; }
    public void setModuleID(String moduleID) { this.moduleID = moduleID; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    
    public String getLecturerID() { return lecturerID; }
    public void setLecturerID(String lecturerID) { this.lecturerID = lecturerID; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public String toFileString() {
        return classID + "|" + moduleID + "|" + className + "|" + 
               moduleCode + "|" + moduleName + "|" + lecturerID + "|" + schedule;
    }
    
    public static ClassGroup fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 7) {

            return new ClassGroup(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        } else if (parts.length >= 3) {

            return new ClassGroup(parts[0], parts[1], parts[2]);
        }
        return null;
    }
    

    public boolean saveToFile() {
        if (FileManager.appendToFile(CLASS_FILE, this.toFileString())) {
            allClasses.add(this); 
            return true;
        }
        return false;
    }
    

    public boolean updateInFile() {

        for (int i = 0; i < allClasses.size(); i++) {
            if (allClasses.get(i).getClassID().equals(this.classID)) {
                allClasses.set(i, this);
                break;
            }
        }
        

        return saveAllToFile();
    }
    

    public boolean deleteFromFile() {
        allClasses.remove(this);
        return saveAllToFile();
    }
    

    public static void loadAllClasses() {
        allClasses.clear();
        List<String> lines = FileManager.readFile(CLASS_FILE);
        for (String line : lines) {
            ClassGroup classGroup = fromFileString(line);
            if (classGroup != null) {
                allClasses.add(classGroup);
            }
        }
    }
    

    public static List<ClassGroup> getAllClasses() {
        if (allClasses.isEmpty()) {
            loadAllClasses();
        }
        return new ArrayList<>(allClasses); 
    }
    

    public static ClassGroup findByID(String classID) {
        if (allClasses.isEmpty()) {
            loadAllClasses();
        }
        for (ClassGroup classGroup : allClasses) {
            if (classGroup.getClassID().equals(classID)) {
                return classGroup;
            }
        }
        return null;
    }
    

    public static List<ClassGroup> getClassesByModuleID(String moduleID) {
        if (allClasses.isEmpty()) {
            loadAllClasses();
        }
        List<ClassGroup> result = new ArrayList<>();
        for (ClassGroup classGroup : allClasses) {
            if (classGroup.getModuleID().equals(moduleID)) {
                result.add(classGroup);
            }
        }
        return result;
    }
    

    private static boolean saveAllToFile() {
        List<String> lines = new ArrayList<>();
        for (ClassGroup classGroup : allClasses) {
            lines.add(classGroup.toFileString());
        }
        return FileManager.writeFile(CLASS_FILE, lines);
    }
}