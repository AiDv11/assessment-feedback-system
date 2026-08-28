package utils;


import java.util.ArrayList;
import java.util.List;
import models.Lecturer;
import models.LecturerAssignment;
import models.User;


public class AssignmentManager {
    
    /**
     * Get all assignments from file
     */
    public static List<LecturerAssignment> getAllAssignments() {
        List<LecturerAssignment> assignments = new ArrayList<>();
        List<String> lines = FileManager.readFile(FileManager.ASSIGNMENTS_FILE);
        
        for (String line : lines) {
            LecturerAssignment assignment = LecturerAssignment.fromFileString(line);
            if (assignment != null) {
                assignments.add(assignment);
            }
        }
        return assignments;
    }

    public static List<LecturerAssignment> getAssignmentsByLecturer(String lecturerID) {
        List<LecturerAssignment> results = new ArrayList<>();
        List<LecturerAssignment> all = getAllAssignments();
        
        for (LecturerAssignment assignment : all) {
            if (assignment.getLecturerID().equals(lecturerID)) {
                results.add(assignment);
            }
        }
        return results;
    }
    

    public static List<LecturerAssignment> getAssignmentsByModule(String moduleID) {
        List<LecturerAssignment> results = new ArrayList<>();
        List<LecturerAssignment> all = getAllAssignments();
        
        for (LecturerAssignment assignment : all) {
            if (assignment.getModuleID().equals(moduleID)) {
                results.add(assignment);
            }
        }
        return results;
    }
    

    public static boolean isAlreadyAssigned(String lecturerID, String moduleID) {
        List<LecturerAssignment> all = getAllAssignments();
        
        for (LecturerAssignment assignment : all) {
            if (assignment.getLecturerID().equals(lecturerID) && 
                assignment.getModuleID().equals(moduleID)) {
                return true;
            }
        }
        return false;
    }
    

    public static boolean addAssignment(LecturerAssignment assignment) {
     
        if (isAlreadyAssigned(assignment.getLecturerID(), assignment.getModuleID())) {
            System.err.println("Lecturer already assigned to this module");
            return false;
        }
        
        return FileManager.appendToFile(FileManager.ASSIGNMENTS_FILE, assignment.toFileString());
    }
    
    
    public static boolean deleteAssignment(LecturerAssignment assignment) {
        return FileManager.deleteLine(FileManager.ASSIGNMENTS_FILE, assignment.toFileString());
    }
    

    public static String generateNextAssignmentID() {
        return FileManager.generateNextID(FileManager.ASSIGNMENTS_FILE, "A");
    }
    
    
public static List<User> getAllLecturers() {
    List<User> lecturers = new ArrayList<>();
    List<String> lines = FileManager.readFile(FileManager.USERS_FILE);

    for (var line : lines) {
    String[] parts = line.split("\\|");

        if (parts.length < 9) continue;

       
        if (parts[8].equalsIgnoreCase("lecturer")) {
            try {
                lecturers.add(new Lecturer(
                    parts[0], parts[1], parts[2], parts[3],
                    parts[4], parts[5], parts[6],
                    (parts[7])));
            } catch (Exception ignored) {}
            continue;
        }

      
        if (parts[4].equalsIgnoreCase("lecturer")) {
            try {
                lecturers.add(new Lecturer(
                    parts[0], parts[1], parts[2], parts[3],
                    parts[5], parts[6], parts[7],(parts[8])
                ));
            } catch (Exception ignored) {}
        }
    }
    return lecturers;
}

    


public static String getLecturerNamesForModule(String moduleID) {
    List<LecturerAssignment> assignments = getAssignmentsByModule(moduleID);
    if (assignments.isEmpty()) return "Unassigned";

    List<String> names = new ArrayList<>();
    for (LecturerAssignment a : assignments) {
        User u = AuthManager.findUserByID(a.getLecturerID());
        if (u != null && u.getName() != null) {
            names.add(u.getName());
        } else {
            names.add(a.getLecturerID()); // fallback
        }
    }
    return String.join(", ", names);
}


public static boolean deleteAssignmentsByModule(String moduleID) {
    boolean ok = true;
    List<LecturerAssignment> assignments = getAssignmentsByModule(moduleID);
    for (LecturerAssignment a : assignments) {
        ok = deleteAssignment(a) && ok;
    }
    return ok;
}

  
    public static int getTotalAssignments() {
        return getAllAssignments().size();
    }
}