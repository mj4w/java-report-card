package org.example.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Implements the Serializable interface
public class StudentGrade implements Serializable {
    private String studentId;
    private String name;
    private String subject;
    private double grade;
    private double averageGrade;  // Computed instance variable

    // No-argument constructor
    public StudentGrade() {
    }

    // Parameterized constructor
    public StudentGrade(String studentId, String name, String subject, double grade) {
        this.studentId = studentId;
        this.name = name;
        this.subject = subject;
        this.grade = grade;
    }

    // Setter and Getter methods
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    // Method to compute the average grade
    public void computeAverage(List<StudentGrade> studentGrades) {
        this.averageGrade = studentGrades.stream()
                .filter(s -> s.getStudentId().equals(this.studentId))
                .mapToDouble(StudentGrade::getGrade)
                .average()
                .orElse(0);
    }

    // Method to generate a report card
    public void generateStudentReport() {
        String[] nameParts = this.name.split(" ");
        String lastName = nameParts[nameParts.length - 1].toLowerCase();
        String firstName = nameParts[0].toLowerCase();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "output/" + lastName + "_" + firstName + "_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Student ID: " + this.studentId);
            writer.println("Name: " + this.name);
            writer.println(this.subject + ": " + this.grade);
            writer.printf("Average Grade: %.2f", this.averageGrade);
            System.out.println("Report card generated: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}