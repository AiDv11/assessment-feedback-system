package utils;

import models.LecturerAssignment;
import java.util.ArrayList;
import java.util.List;

public class ModuleLecturerAssignmentManager {

    private static final String FILE = FileManager.MODULE_LECTURER_ASSIGNMENTS_FILE;

    
    public static boolean addAssignment(LecturerAssignment assignment) {
        String line = serialize(assignment);
        return FileManager.appendToFile(FILE, line);
    }

   
    public static List<LecturerAssignment> getAllAssignments() {
        List<LecturerAssignment> list = new ArrayList<>();
        List<String> lines = FileManager.readFile(FILE);

        for (String line : lines) {
            LecturerAssignment assignment = deserialize(line);
            if (assignment != null) list.add(assignment);
        }

        return list;
    }

    
    public static boolean deleteAssignmentsByModule(String moduleID) {
        List<LecturerAssignment> all = getAllAssignments();
        List<LecturerAssignment> updated = new ArrayList<>();

        for (LecturerAssignment a : all) {
            if (!a.getModuleID().equals(moduleID)) {
                updated.add(a);
            }
        }

        List<String> lines = new ArrayList<>();
        for (LecturerAssignment a : updated) lines.add(serialize(a));

        return FileManager.writeFile(FILE, lines);
    }

  
    public static String generateNextAssignmentID() {
        List<LecturerAssignment> all = getAllAssignments();
        int max = 0;

        for (LecturerAssignment a : all) {
            String idNum = a.getAssignmentID().replaceAll("[^0-9]", "");
            try { max = Math.max(max, Integer.parseInt(idNum)); } catch (NumberFormatException e) {}
        }

        return "MLA" + String.format("%03d", max + 1);
    }

   

    private static String serialize(LecturerAssignment a) {
        return a.getAssignmentID() + "|" + a.getLecturerID() + "|" + a.getModuleID()
                + "|" + a.getSemester() + "|" + a.getYear();
    }

    private static LecturerAssignment deserialize(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;
        return new LecturerAssignment(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
