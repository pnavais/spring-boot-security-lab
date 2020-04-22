package com.github.pnavais.lab.student.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Integer id) {
        super("Cannot find entity with id ["+id+"]");
    }

    public EntityNotFoundException(String name) {
        super("Cannot find entity with name ["+name+"]");
    }

}
