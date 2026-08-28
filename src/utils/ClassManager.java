package utils;

import java.util.ArrayList;
import java.util.List;

public class ClassManager {
    
   
    public static class ClassEntry {
        public String classID;
        public String moduleCode;
        public String className;
        public String moduleName1;  
        public String moduleName2; 
        public String lecturerID;
        public String schedule;
        
        public ClassEntry(String classID, String moduleCode, String className,
                         String moduleName1, String moduleName2, 
                         String lecturerID, String schedule) {
            this.classID = classID;
            this.moduleCode = moduleCode;
            this.className = className;
            this.moduleName1 = moduleName1;
            this.moduleName2 = moduleName2;
            this.lecturerID = lecturerID;
            this.schedule = schedule;
        }
        
        public String toFileString() {
            return String.join("|", 
                classID, moduleCode, className, 
                moduleName1, moduleName2, lecturerID, schedule);
        }
        
        public static ClassEntry fromFileString(String line) {
            if (line == null || line.trim().isEmpty()) return null;
            
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                return new ClassEntry(
                    parts[0].trim(), parts[1].trim(), parts[2].trim(),
                    parts[3].trim(), parts[4].trim(), 
                    parts[5].trim(), parts[6].trim()
                );
            }
            return null;
        }
    }
    
  
    public static List<ClassEntry> getAllClasses() {
        List<ClassEntry> classes = new ArrayList<>();
        List<String> lines = FileManager.readFile(FileManager.CLASSES_FILE);
        
        for (String line : lines) {
            ClassEntry cls = ClassEntry.fromFileString(line);
            if (cls != null) {
                classes.add(cls);
            }
        }
        return classes;
    }
    
    
    public static List<ClassEntry> getClassesByModule(String moduleCode) {
        List<ClassEntry> results = new ArrayList<>();
        List<ClassEntry> all = getAllClasses();
        
        for (ClassEntry cls : all) {
            if (cls.moduleCode.equals(moduleCode)) {
                results.add(cls);
            }
        }
        return results;
    }
    
    
    public static List<ClassEntry> getClassesByLecturer(String lecturerID) {
        List<ClassEntry> results = new ArrayList<>();
        List<ClassEntry> all = getAllClasses();
        
        for (ClassEntry cls : all) {
            if (cls.lecturerID.equals(lecturerID)) {
                results.add(cls);
            }
        }
        return results;
    }
    
    
    public static ClassEntry findClassByID(String classID) {
        List<ClassEntry> all = getAllClasses();
        for (ClassEntry cls : all) {
            if (cls.classID.equals(classID)) {
                return cls;
            }
        }
        return null;
    }
    
   
    public static boolean addClass(ClassEntry cls) {
        return FileManager.appendToFile(FileManager.CLASSES_FILE, cls.toFileString());
    }
    
    
    public static boolean updateClass(ClassEntry oldClass, ClassEntry newClass) {
        List<String> lines = FileManager.readFile(FileManager.CLASSES_FILE);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            ClassEntry cls = ClassEntry.fromFileString(line);
            if (cls != null && cls.classID.equals(oldClass.classID)) {
                updatedLines.add(newClass.toFileString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (found) {
            return FileManager.writeFile(FileManager.CLASSES_FILE, updatedLines);
        }
        return false;
    }
    
    
    public static boolean deleteClass(String classID) {
        List<String> lines = FileManager.readFile(FileManager.CLASSES_FILE);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            ClassEntry cls = ClassEntry.fromFileString(line);
            if (cls == null || !cls.classID.equals(classID)) {
                updatedLines.add(line);
            } else {
                found = true;
            }
        }
        
        if (found) {
            return FileManager.writeFile(FileManager.CLASSES_FILE, updatedLines);
        }
        return false;
    }
    
   
    public static boolean updateLecturerForModule(String moduleCode, String lecturerID) {
        List<String> lines = FileManager.readFile(FileManager.CLASSES_FILE);
        List<String> updatedLines = new ArrayList<>();
        boolean anyUpdated = false;
        
        for (String line : lines) {
            ClassEntry cls = ClassEntry.fromFileString(line);
            if (cls != null && cls.moduleCode.equals(moduleCode)) {
                
                cls.lecturerID = lecturerID;
                updatedLines.add(cls.toFileString());
                anyUpdated = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (anyUpdated) {
            return FileManager.writeFile(FileManager.CLASSES_FILE, updatedLines);
        }
        return false;
    }
    
    
    public static boolean createDefaultClassForModule(String moduleCode, String moduleName, 
                                                     String lecturerID, String schedule) {
     
        List<ClassEntry> existing = getClassesByModule(moduleCode);
        if (!existing.isEmpty()) {
          
            return updateLecturerForModule(moduleCode, lecturerID);
        }
        
       
        String classID = generateNextClassID();
        
        
        ClassEntry newClass = new ClassEntry(
            classID,
            moduleCode,
            moduleName + " - Section A", 
            moduleCode,                  
            moduleName,                   
            lecturerID,
            schedule != null ? schedule : "TBA"
        );
        
        return addClass(newClass);
    }
    
    
    public static String generateNextClassID() {
        return FileManager.generateNextID(FileManager.CLASSES_FILE, "C");
    }
    
   
    public static int getEnrollmentCount(String classID) {
        List<String> enrollments = FileManager.readFile(FileManager.ENROLLMENTS_FILE);
        int count = 0;
        
        for (String line : enrollments) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length >= 2 && parts[1].trim().equals(classID)) {
                count++;
            }
        }
        return count;
    }
}