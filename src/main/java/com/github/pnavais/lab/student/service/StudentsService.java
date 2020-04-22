package com.github.pnavais.lab.student.service;

import com.github.pnavais.lab.student.model.Student;

import java.util.Collection;

public interface StudentsService {

    Collection<Student> getAll();

    Student findStudentById(Integer studentId);

    Student findStudentByName(String studentName);

    void updateStudent(Integer studentId, Student student);

    void removeStudent(Integer studentId);

    void addStudent(Student student);
}
