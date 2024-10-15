package org.example.model;

import java.util.ArrayList;


public interface FileOperation {
    ArrayList<StudentGrade> readFile(String filePathAndName);
}