/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class Module {

    public static final String MODULE_FILE = "data/modules.txt";
    public static final int MAX_MODULE_NAME_LENGTH = 100;
    
    private String moduleID;
    private String moduleName;
    private String moduleCode;
    private int credits;
    private String description;
    private String semester;
    private String assignedLecturer; 

    public Module(String moduleID, String moduleName, String moduleCode, 
              int credits, String description, String semester, String lecturer) {

    this.moduleID = moduleID;
    this.moduleName = moduleName;
    this.moduleCode = moduleCode;
    this.credits = credits;
    this.description = description;
    this.semester = semester;
    this.assignedLecturer = (lecturer != null) ? lecturer : "";
}


        public Module(String moduleID, String moduleName, String moduleCode,
                  int credits, String description, String semester) {
        this(moduleID, moduleName, moduleCode, credits, description, semester, ""); 
      
    }
    

    public String getModuleID() { return moduleID; }
    public void setModuleID(String moduleID) { this.moduleID = moduleID; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
  
    public String getAssignedLecturer() { return assignedLecturer; }
    public void setAssignedLecturer(String assignedLecturer) { this.assignedLecturer = assignedLecturer; }
    

    public String toFileString() {
        return moduleID + "|" + moduleName + "|" + moduleCode + "|" + 
               credits + "|" + description + "|" + semester + "|" + 
               (assignedLecturer != null ? assignedLecturer : "");
    }
    
   
    public static Module fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 6) {
            String lecturer = (parts.length >= 7) ? parts[6] : "";
            return new Module(
                parts[0], 
                parts[1],  
                parts[2],  
                Integer.parseInt(parts[3]), 
                parts[4],
                parts[5],  
                lecturer  
            );
        }
        return null;
    }
    @Override
    public String toString() {
        return moduleCode + " - " + moduleName + " (" + credits + " credits)";
    }
    
    public String getDetailedInfo() {
        return "Module ID: " + moduleID + "\n" +
               "Name: " + moduleName + "\n" +
               "Code: " + moduleCode + "\n" +
               "Credits: " + credits + "\n" +
               "Description: " + description + "\n" +
               "Semester: " + semester + "\n" +
               "Assigned Lecturer: " + (assignedLecturer.isEmpty() ? "None" : assignedLecturer);
    }
}
