package org.example.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileOperationImpl implements FileOperation {
    @Override
    public ArrayList<StudentGrade> readFile(String filePathAndName) {
        ArrayList<StudentGrade> studentGrades = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePathAndName));
            for (String line : lines.subList(1, lines.size())) {  // Skip header
                String[] tokens = line.split(",");
                String studentId = tokens[0];
                String name = tokens[1];
                String subject = tokens[2];
                double grade = Double.parseDouble(tokens[3]);

                StudentGrade student = new StudentGrade(studentId, name, subject, grade);
                studentGrades.add(student);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return studentGrades;
    }
}