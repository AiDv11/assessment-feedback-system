package models;



public class Mark {
    

    private String markID;
    private String studentID;
    private String assessmentID;
    private double marksObtained;
    private String grade;
    private String enteredBy;  
    private String dateEntered;
    
  
    public Mark(String markID, String studentID, String assessmentID,
               double marksObtained, String grade, String enteredBy, String dateEntered) {
        this.markID = markID;
        this.studentID = studentID;
        this.assessmentID = assessmentID;
        this.marksObtained = marksObtained;
        this.grade = grade;
        this.enteredBy = enteredBy;
        this.dateEntered = dateEntered;
    }
    
  
    public String getMarkID() {
        return markID;
    }
    
    public void setMarkID(String markID) {
        this.markID = markID;
    }
    
    public String getStudentID() {
        return studentID;
    }
    
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    
    public String getAssessmentID() {
        return assessmentID;
    }
    
    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }
    
    public double getMarksObtained() {
        return marksObtained;
    }
    
    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getEnteredBy() {
        return enteredBy;
    }
    
    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }
    
    public String getDateEntered() {
        return dateEntered;
    }
    
    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }
 
    public String toFileString() {
        return markID + "|" + studentID + "|" + assessmentID + "|" +
               marksObtained + "|" + grade + "|" + enteredBy + "|" + dateEntered;
    }
    

    public static Mark fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 7) {
            return new Mark(
                parts[0],
                parts[1],  
                parts[2],  
                Double.parseDouble(parts[3]),  
                parts[4], 
                parts[5],  
                parts[6]   
            );
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Mark: " + marksObtained + " | Grade: " + grade;
    }
}