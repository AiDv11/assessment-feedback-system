package models;

import utils.ModuleManager;
import utils.AssignmentManager;
import utils.ClassManager; 
import utils.AuthManager;
import java.util.List;


public class ReportGenerator {


    public static String generateModuleSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("╔════════════════════════════════════════════════════════════════╗\n");
        report.append("║           MODULE SUMMARY REPORT                                ║\n");
        report.append("╚════════════════════════════════════════════════════════════════╝\n\n");

        List<Module> modules = ModuleManager.getAllModules();
        report.append("Total Modules: ").append(modules.size()).append("\n\n");

        if (modules.isEmpty()) {
            report.append("No modules found in the system.\n");
            return report.toString();
        }

        for (Module module : modules) {
            report.append("─────────────────────────────────────────────────────────────\n");
            report.append("Module ID:    ").append(module.getModuleID()).append("\n");
            report.append("Name:         ").append(module.getModuleName()).append("\n");
            report.append("Code:         ").append(module.getModuleCode()).append("\n");
            report.append("Credits:      ").append(module.getCredits()).append("\n");
            report.append("Semester:     ").append(module.getSemester()).append("\n");
            report.append("Description:  ").append(module.getDescription()).append("\n");

  
            List<LecturerAssignment> assignments = AssignmentManager.getAssignmentsByModule(module.getModuleID());
            report.append("Lecturers:    ").append(assignments.size()).append(" assigned\n");

            List<ClassManager.ClassEntry> classes = ClassManager.getClassesByModule(module.getModuleCode());
            report.append("Classes:      ").append(classes.size()).append(" sections\n");
            report.append("\n");
        }

        return report.toString();
    }


    public static String generateLecturerAssignmentReport() {
        StringBuilder report = new StringBuilder();
        report.append("╔════════════════════════════════════════════════════════════════╗\n");
        report.append("║        LECTURER ASSIGNMENT REPORT                              ║\n");
        report.append("╚════════════════════════════════════════════════════════════════╝\n\n");

        List<LecturerAssignment> assignments = AssignmentManager.getAllAssignments();
        report.append("Total Assignments: ").append(assignments.size()).append("\n\n");

        if (assignments.isEmpty()) {
            report.append("No lecturer assignments found.\n");
            return report.toString();
        }

        for (LecturerAssignment assignment : assignments) {
            Module module = ModuleManager.findModuleByID(assignment.getModuleID());
            User lecturer = AuthManager.findUserByID(assignment.getLecturerID());

            report.append("─────────────────────────────────────────────────────────────\n");
            report.append("Assignment ID: ").append(assignment.getAssignmentID()).append("\n");

            if (lecturer != null) {
                report.append("Lecturer:      ").append(lecturer.getName()).append(" (").append(lecturer.getUserID()).append(")\n");
            } else {
                report.append("Lecturer:      ID ").append(assignment.getLecturerID()).append("\n");
            }

            if (module != null) {
                report.append("Module:        ").append(module.getModuleName()).append(" (").append(module.getModuleCode()).append(")\n");
            } else {
                report.append("Module:        ID ").append(assignment.getModuleID()).append("\n");
            }

            report.append("Semester:      ").append(assignment.getSemester()).append("\n");
            report.append("Year:          ").append(assignment.getYear()).append("\n");
            report.append("\n");
        }

        return report.toString();
    }

 
    public static String generateClassCapacityReport() {
        StringBuilder report = new StringBuilder();
        report.append("╔════════════════════════════════════════════════════════════════╗\n");
        report.append("║          CLASS CAPACITY REPORT                                 ║\n");
        report.append("╚════════════════════════════════════════════════════════════════╝\n\n");

        List<ClassManager.ClassEntry> classes = ClassManager.getAllClasses();
        report.append("Total Classes: ").append(classes.size()).append("\n\n");

        if (classes.isEmpty()) {
            report.append("No classes found in the system.\n");
            return report.toString();
        }

        int totalCapacity = 0;
        int totalEnrolled = 0;
        int fullClasses = 0;

        for (ClassManager.ClassEntry cls : classes) {
            Module module = ModuleManager.findModuleByCode(cls.moduleCode);

    
            int capacity = 30; // or any default number
            int enrolled = ClassManager.getEnrollmentCount(cls.classID);
            int available = capacity - enrolled;
            boolean full = available <= 0;

            report.append("─────────────────────────────────────────────────────────────\n");
            report.append("Class ID:      ").append(cls.classID).append("\n");
            report.append("Class Name:    ").append(cls.className).append("\n");

            if (module != null) {
                report.append("Module:        ").append(module.getModuleName()).append(" (").append(module.getModuleCode()).append(")\n");
            }

            report.append("Schedule:      ").append(cls.schedule).append("\n");
            report.append("Capacity:      ").append(capacity).append("\n");
            report.append("Enrolled:      ").append(enrolled).append("\n");
            report.append("Available:     ").append(available).append("\n");
            report.append("Status:        ").append(full ? "FULL ⚠" : "Available ✓").append("\n\n");

            totalCapacity += capacity;
            totalEnrolled += enrolled;
            if (full) fullClasses++;
        }

        report.append("═════════════════════════════════════════════════════════════\n");
        report.append("                    SUMMARY STATISTICS                       \n");
        report.append("═════════════════════════════════════════════════════════════\n");
        report.append("Total Capacity:    ").append(totalCapacity).append(" seats\n");
        report.append("Total Enrolled:    ").append(totalEnrolled).append(" students\n");
        report.append("Total Available:   ").append(totalCapacity - totalEnrolled).append(" seats\n");
        report.append("Full Classes:      ").append(fullClasses).append(" / ").append(classes.size()).append("\n");

        if (totalCapacity > 0) {
            int utilization = (totalEnrolled * 100) / totalCapacity;
            report.append("Utilization:       ").append(utilization).append("%\n");
        }

        return report.toString();
    }

  
    public static String generateDashboardSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════════\n");
        summary.append("       ACADEMIC LEADER DASHBOARD          \n");
        summary.append("═══════════════════════════════════════════\n\n");

        summary.append("Modules:       ").append(ModuleManager.getTotalModules()).append("\n");
        summary.append("Classes:       ").append(ClassManager.getAllClasses().size()).append("\n");
        summary.append("Assignments:   ").append(AssignmentManager.getTotalAssignments()).append("\n");
        summary.append("Lecturers:     ").append(AssignmentManager.getAllLecturers().size()).append("\n");

        return summary.toString();
    }
}
