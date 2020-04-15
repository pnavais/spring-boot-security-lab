package com.example.demo.student.service;

import com.example.demo.student.model.Student;

import java.util.Collection;

public interface StudentsService {

    Collection<Student> getAll();

    Student findStudentById(Integer studentId);

    Student findStudentByName(String studentName);

    void updateStudent(Integer studentId, Student student);

    void removeStudent(Integer studentId);

    void addStudent(Student student);
}
