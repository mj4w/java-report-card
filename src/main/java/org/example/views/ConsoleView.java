package org.example.views;

import org.example.model.StudentGrade;

import java.util.*;

public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public String getUserChoice() {
        System.out.println("[L] - List All Records");
        System.out.println("[R] - Report Card Generate");
        System.out.println("[Q] - Quit");
        System.out.print("Enter command of choice: ");
        return scanner.nextLine().toLowerCase();
    }

    public void displayAllRecords(List<StudentGrade> studentGrades) {
        // Map to store total grades and count for each student
        Map<String, List<Double>> studentMap = new HashMap<>();

        // Group grades by student ID and name
        for (StudentGrade student : studentGrades) {
            String key = student.getStudentId() + "|" + student.getName();
            studentMap.putIfAbsent(key, new ArrayList<>());
            studentMap.get(key).add(student.getGrade());
        }

        // Display header
        System.out.printf("%-12s %-20s %-22s%n", "Student ID", "Name", "Computed Average Grades");

        // Calculate and display averages
        for (Map.Entry<String, List<Double>> entry : studentMap.entrySet()) {
            String[] keyParts = entry.getKey().split("\\|");
            String studentId = keyParts[0];
            String name = keyParts[1];
            List<Double> grades = entry.getValue();

            double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            System.out.printf("%-12s %-20s %-22.2f%n", studentId, name, average);
        }
    }

    public String getStudentId() {
        System.out.print("Enter Student ID: ");
        return scanner.nextLine();
    }

    public void displayReportCard(String reportContent, String filename) {
        System.out.println("Report card generated: " + filename);
        System.out.println(reportContent);
    }

    public void displayError(String message) {
        System.out.println(message);
    }

    public void displayExitMessage() {
        System.out.println("Exiting the system. Goodbye!");
    }

    public void displayInvalidCommand() {
        System.out.println("Invalid command! Please enter L, R, or Q.");
    }
}
