/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces; 
/**
 *
 * @author Hussain Alkhaldi
 */
public interface Feedbackable {
    void provideFeedback(String studentID, String assessmentID, String comment);
    void viewFeedback();
}
