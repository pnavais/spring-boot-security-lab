package com.github.pnavais.lab.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "from")
@NoArgsConstructor
public class Student {
    private Integer studentId;
    private String name;
}
