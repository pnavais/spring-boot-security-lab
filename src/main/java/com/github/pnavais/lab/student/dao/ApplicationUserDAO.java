package com.github.pnavais.lab.student.dao;

import com.github.pnavais.lab.student.model.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserDAO {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
