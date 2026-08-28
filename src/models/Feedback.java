package models;



public class Feedback {
    
  
    private String feedbackID;
    private String studentID;
    private String assessmentID;
    private String lecturerID;
    private String feedbackText;
    private String dateGiven;
    
 
    public Feedback(String feedbackID, String studentID, String assessmentID,
                   String lecturerID, String feedbackText, String dateGiven) {
        this.feedbackID = feedbackID;
        this.studentID = studentID;
        this.assessmentID = assessmentID;
        this.lecturerID = lecturerID;
        this.feedbackText = feedbackText;
        this.dateGiven = dateGiven;
    }
    
   
    public String getFeedbackID() {
        return feedbackID;
    }
    
    public void setFeedbackID(String feedbackID) {
        this.feedbackID = feedbackID;
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
    
    public String getLecturerID() {
        return lecturerID;
    }
    
    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }
    
    public String getFeedbackText() {
        return feedbackText;
    }
    
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }
    
    public String getDateGiven() {
        return dateGiven;
    }
    
    public void setDateGiven(String dateGiven) {
        this.dateGiven = dateGiven;
    }
    

    public String toFileString() {
        return feedbackID + "|" + studentID + "|" + assessmentID + "|" +
               lecturerID + "|" + feedbackText + "|" + dateGiven;
    }
    

    public static Feedback fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 6) {
            return new Feedback(
                parts[0],  
                parts[1],  
                parts[2], 
                parts[3], 
                parts[4],  
                parts[5]  
            );
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Feedback: " + feedbackText.substring(0, Math.min(50, feedbackText.length())) + "...";
    }
}