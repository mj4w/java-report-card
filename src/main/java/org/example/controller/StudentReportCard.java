package org.example.controller;

import org.example.model.FileOperationImpl;
import org.example.model.StudentGrade;
import org.example.model.StudentIDNotFoundException;
import org.example.views.ConsoleView;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentReportCard {
    private static final String CSV_FILE = "D:\\programming\\JAVA\\Case01-Student-Report-Card\\src\\main\\resources\\StudentGrade.csv";  // Path to your CSV file
    private ConsoleView view;
    private FileOperationImpl fileOp;

    public StudentReportCard() {
        this.view = new ConsoleView();
        this.fileOp = new FileOperationImpl();
    }

    public void start() {
        while (true) {
            String choice = view.getUserChoice();

            switch (choice) {
                case "l":
                    listAllRecords();
                    break;
                case "r":
                    generateReportCard();
                    break;
                case "q":
                    view.displayExitMessage();
                    return;
                default:
                    view.displayInvalidCommand();
            }
        }
    }

    private void listAllRecords() {
        ArrayList<StudentGrade> studentGrades = fileOp.readFile(CSV_FILE);
        view.displayAllRecords(studentGrades);
    }

    private void generateReportCard() {
        String studentId = view.getStudentId();
        ArrayList<StudentGrade> studentGrades = fileOp.readFile(CSV_FILE);

        Optional<StudentGrade> studentOpt = studentGrades.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst();

        try {
            if (studentOpt.isPresent()) {
                StudentGrade student = studentOpt.get();
                // Only display these messages

                System.out.println("Please wait. Record found. Generating a report card for " + student.getName() + ".");
                Thread.sleep(1000);
                // Prepare the report card content
                StringBuilder reportContent = new StringBuilder();
                reportContent.append("Student ID: ").append(student.getStudentId()).append("\n")
                        .append("Name: ").append(student.getName()).append("\n")
                        .append("\n");

                reportContent.append(String.format("%-30s %s%n", "Subject", "Grade")); // Header

                // Collect grades for the report
                for (StudentGrade sg : studentGrades) {
                    if (sg.getStudentId().equals(studentId)) {
                        reportContent.append(String.format("%-30s %5.1f%n", sg.getSubject(), sg.getGrade()));
                    }
                }

                // Calculate average grade
                double average = studentGrades.stream()
                        .filter(sg -> sg.getStudentId().equals(studentId))
                        .mapToDouble(StudentGrade::getGrade)
                        .average()
                        .orElse(0.0);

                reportContent.append("\nAverage Grade\t\t\t\t\t").append(String.format("%.2f", average)).append("\n");

                // Generate filename
                String[] nameParts = student.getName().split(" ");
                String lastName = nameParts[nameParts.length - 1];
                String firstName = nameParts[0];
                String timestamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
                String filename = "output/" + lastName + "_" + firstName + "_" + timestamp + ".txt";

                // Ensure the output directory exists
                File outputDir = new File("output");
                if (!outputDir.exists() && !outputDir.mkdirs()) {
                    view.displayError("Error: Could not create output directory.");
                    return; // Exit the method if the directory cannot be created
                }

                // Save report card to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                    writer.write(reportContent.toString());
                    // Only display the final two messages
                    System.out.println("Report card generated: " + filename);
                } catch (IOException e) {
                    view.displayError("Error writing report card: " + e.getMessage());
                }
            } else {
                System.out.println("Please wait.");
                Thread.sleep(1000);
                throw new StudentIDNotFoundException("Student ID does not exist. " + studentId + ".");
            }
        } catch (StudentIDNotFoundException | InterruptedException e) {
            view.displayError(e.getMessage());
        }
    }

}
