package com.github.pnavais.lab.student.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "auth_permission")
@Getter
public class AuthPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
