package models;


public class LecturerAssignment {
    
    private String assignmentID;
    private String lecturerID;
    private String moduleID;
    private String semester;
    private String year;
    
   
    public LecturerAssignment(String assignmentID, String lecturerID, 
                             String moduleID, String semester, String year) {
        this.assignmentID = assignmentID;
        this.lecturerID = lecturerID;
        this.moduleID = moduleID;
        this.semester = semester;
        this.year = year;
    }
    
  
    public String getAssignmentID() {
        return assignmentID;
    }
    
    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }
    
    public String getLecturerID() {
        return lecturerID;
    }
    
    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }
    
    public String getModuleID() {
        return moduleID;
    }
    
    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    /**
     * Convert to file format
     */
    public String toFileString() {
        return assignmentID + "|" + lecturerID + "|" + moduleID + "|" + 
               semester + "|" + year;
    }
    
   
    public static LecturerAssignment fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 5) {
            return new LecturerAssignment(
                parts[0],  
                parts[1],  
                parts[2],  
                parts[3],  
                parts[4]   
            );
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Assignment " + assignmentID + ": Lecturer " + lecturerID + 
               " -> Module " + moduleID + " (" + semester + " " + year + ")";
    }
}