package utils;

import java.util.ArrayList;
import java.util.List;
import models.Module;


public class ModuleManager {
    
   
    public static List<Module> getAllModules() {
        List<Module> modules = new ArrayList<>();
        List<String> lines = FileManager.readFile(FileManager.MODULES_FILE);
        
        for (String line : lines) {
            Module module = Module.fromFileString(line);
            if (module != null) {
                modules.add(module);
            }
        }
        return modules;
    }
    
   
    public static Module findModuleByID(String moduleID) {
        List<Module> modules = getAllModules();
        for (Module module : modules) {
            if (module.getModuleID().equals(moduleID)) {
                return module;
            }
        }
        return null;
    }
    
   
    public static Module findModuleByCode(String moduleCode) {
        List<Module> modules = getAllModules();
        for (Module module : modules) {
            if (module.getModuleCode().equalsIgnoreCase(moduleCode)) {
                return module;
            }
        }
        return null;
    }
    
    
    public static boolean addModule(Module module) {
       
        if (findModuleByCode(module.getModuleCode()) != null) {
            System.err.println("Module code already exists: " + module.getModuleCode());
            return false;
        }
        
        return FileManager.appendToFile(FileManager.MODULES_FILE, module.toFileString());
    }
    
    
    public static boolean updateModule(Module oldModule, Module newModule) {
        String oldLine = oldModule.toFileString();
        String newLine = newModule.toFileString();
        return FileManager.updateLine(FileManager.MODULES_FILE, oldLine, newLine);
    }
    
   
    public static boolean deleteModule(Module module) {
        String line = module.toFileString();
        return FileManager.deleteLine(FileManager.MODULES_FILE, line);
    }
    
  
    public static List<Module> searchModules(String searchTerm) {
        List<Module> results = new ArrayList<>();
        List<Module> allModules = getAllModules();
        
        String lowerSearch = searchTerm.toLowerCase();
        for (Module module : allModules) {
            if (module.getModuleName().toLowerCase().contains(lowerSearch) ||
                module.getModuleCode().toLowerCase().contains(lowerSearch)) {
                results.add(module);
            }
        }
        return results;
    }
    
  
    public static List<Module> getModulesBySemester(String semester) {
        List<Module> results = new ArrayList<>();
        List<Module> allModules = getAllModules();
        
        for (Module module : allModules) {
            if (module.getSemester().equalsIgnoreCase(semester)) {
                results.add(module);
            }
        }
        return results;
    }
    
   
    public static String generateNextModuleID() {
        return FileManager.generateNextID(FileManager.MODULES_FILE, "M");
    }
    
   
    public static int getTotalModules() {
        return getAllModules().size();
    }
}