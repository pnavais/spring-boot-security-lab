package com.example.demo.student.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface UserProvider {

    Collection<UserDetails> getUsers();
}
