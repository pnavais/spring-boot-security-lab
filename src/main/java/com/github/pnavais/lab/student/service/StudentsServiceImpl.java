package com.github.pnavais.lab.student.service;

import com.github.pnavais.lab.student.exception.EntityNotFoundException;
import com.github.pnavais.lab.student.model.Student;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class StudentsServiceImpl implements StudentsService {

    private final List<Student> students = Lists.newArrayList(
            Student.from(1, "James Bond"),
            Student.from(2, "Maria Jones"),
            Student.from(3, "Anna Smith"));

    @Override
    public Collection<Student> getAll() {
        log.debug("Retrieving all students");
        return students;
    }

    @Override
    public void updateStudent(Integer studentId, Student student) {
        Student studentFound = findStudentById(studentId);
        if (Objects.nonNull(studentFound)) {
            log.debug("Student updated [{}] -> [{}]", studentFound.getName(), student.getName());
            studentFound.setName(student.getName());
        }
    }

    @Override
    public Student findStudentById(Integer studentId) {
        return students.stream().filter(s -> s.getStudentId().equals(studentId)).findFirst()
                    .orElse(null);
    }

    @Override
    public Student findStudentByName(String studentName) {
        return students.stream().filter(s -> s.getName().equals(studentName)).findFirst()
                .orElse(null);
    }

    @Override
    public void removeStudent(Integer studentId) {
        Student studentFound = findStudentById(studentId);
        if (Objects.nonNull(studentFound)) {
            students.remove(studentFound);
            log.debug("Student removed [{} - {}]", studentFound.getStudentId(), studentFound.getName());
        } else {
            throw new EntityNotFoundException(studentId);
        }
    }

    @Override
    public void addStudent(Student student) {
        Student studentFound = findStudentByName(student.getName());
        if (Objects.isNull(studentFound)) {
            students.add(student);
            student.setStudentId(students.size());
            log.debug("Student added [{} - {}]", student.getStudentId(), student.getName());
        } else {
            throw new IllegalStateException("Cannot add existing user ["+ student.getName() +"]");
        }
    }
}
