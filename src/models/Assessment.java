package models;


public class Assessment {
    
    // Fields
    private String assessmentID;
    private String moduleCode;
    private String assessmentName;
    private String assessmentType;
    private int totalMarks;
    private double weightage;
    private String lecturerID;
    private String createdDate;
    

    public Assessment(String assessmentID, String moduleCode, String assessmentName,
                     String assessmentType, int totalMarks, double weightage,
                     String lecturerID, String createdDate) {
        this.assessmentID = assessmentID;
        this.moduleCode = moduleCode;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.totalMarks = totalMarks;
        this.weightage = weightage;
        this.lecturerID = lecturerID;
        this.createdDate = createdDate;
    }
    

    public String getAssessmentID() {
        return assessmentID;
    }
    
    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }
    
    public String getModuleCode() {
        return moduleCode;
    }
    
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public String getAssessmentName() {
        return assessmentName;
    }
    
    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }
    
    public String getAssessmentType() {
        return assessmentType;
    }
    
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    
    public int getTotalMarks() {
        return totalMarks;
    }
    
    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
    
    public double getWeightage() {
        return weightage;
    }
    
    public void setWeightage(double weightage) {
        this.weightage = weightage;
    }
    
    public String getLecturerID() {
        return lecturerID;
    }
    
    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
 
    public String toFileString() {
        return assessmentID + "|" + moduleCode + "|" + assessmentName + "|" +
               assessmentType + "|" + totalMarks + "|" + weightage + "|" +
               lecturerID + "|" + createdDate;
    }
    

    public static Assessment fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 8) {
            return new Assessment(
                parts[0],  
                parts[1],  
                parts[2],  
                parts[3],  
                Integer.parseInt(parts[4]),  
                Double.parseDouble(parts[5]), 
                parts[6],  
                parts[7]   
            );
        }
        return null;
    }
    
    @Override
    public String toString() {
        return assessmentName + " (" + assessmentType + ") - " + totalMarks + " marks";
    }
}