package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List; 


public class FileManager {
    
    
    public static final String MODULE_LECTURER_ASSIGNMENTS_FILE = "data/moduleLecturerAssignments.txt";
    public static final String USERS_FILE = "data/users.txt";
    public static final String MODULES_FILE = "data/modules.txt";
    public static final String CLASSES_FILE = "data/classes.txt";
    public static final String GRADING_FILE = "data/grading.txt";
    public static final String ASSIGNMENTS_FILE = "data/lecturerAssignments.txt";
    public static final String MARKS_FILE       = "data/marks.txt";
    public static final String ENROLLMENTS_FILE = "data/enrollments.txt";
    public static final String COMMENTS_FILE    = "data/comments.txt";
    public static final String FEEDBACK_FILE    = "data/feedback.txt";
    
   
    public static void initializeDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
       
        createFileIfNotExists(USERS_FILE);
        createFileIfNotExists(MODULES_FILE);
        createFileIfNotExists(CLASSES_FILE);
        createFileIfNotExists(GRADING_FILE);
        createFileIfNotExists(ASSIGNMENTS_FILE);
        createFileIfNotExists(ENROLLMENTS_FILE);
        createFileIfNotExists(COMMENTS_FILE);
        createFileIfNotExists(FEEDBACK_FILE);
        createFileIfNotExists(MARKS_FILE);
        createFileIfNotExists(MODULE_LECTURER_ASSIGNMENTS_FILE);

    }
    
    private static void createFileIfNotExists(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + filepath);
                e.printStackTrace();
            }
        }
    }
    
   
    public static List<String> readFile(String filepath) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filepath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filepath);
            e.printStackTrace();
        }
        
        return lines;
    }
    
    
    public static boolean writeFile(String filepath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filepath);
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static boolean appendToFile(String filepath, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true))) {
            bw.write(line);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filepath);
            e.printStackTrace();
            return false;
        }
    }
    
    
  
    public static boolean updateLine(String filepath, String oldLine, String newLine) {
        List<String> lines = readFile(filepath);
        boolean found = false;
        
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(oldLine)) {
                lines.set(i, newLine);
                found = true;
                break;
            }
        }
        
        if (found) {
            return writeFile(filepath, lines);
        }
        return false;
    }
    
    

    public static boolean deleteLine(String filepath, String lineToDelete) {
        List<String> lines = readFile(filepath);
        boolean removed = lines.remove(lineToDelete);
        
        if (removed) {
            return writeFile(filepath, lines);
        }
        return false;
    }
    
   
    public static String generateNextID(String filepath, String prefix) {
        List<String> lines = readFile(filepath);
        int maxNum = 0;
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 0 && parts[0].startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(parts[0].substring(prefix.length()));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException e) {
                   
                }
            }
        }
        
        return prefix + String.format("%03d", maxNum + 1);
    }
}