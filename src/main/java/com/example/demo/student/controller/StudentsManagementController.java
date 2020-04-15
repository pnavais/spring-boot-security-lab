package com.example.demo.student.controller;

import com.example.demo.student.model.Student;
import com.example.demo.student.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentsManagementController {

    private final StudentsService studentsService;

    @Autowired
    public StudentsManagementController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
    public Collection<Student> getAll() {
        return studentsService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void addUser(@RequestBody Student student) {
        this.studentsService.addStudent(student);
    }

    @PutMapping(path = "{studentId}")
    public void updateUser(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        this.studentsService.updateStudent(studentId, student);
    }

    @DeleteMapping( path = "{studentId}")
    public void removeUser(@PathVariable("studentId") Integer studentId) {
        this.studentsService.removeStudent(studentId);
    }
}
