package com.github.pnavais.lab.student.dao;

import com.github.pnavais.lab.student.mapper.StudentMapper;
import com.github.pnavais.lab.student.model.ApplicationUser;
import com.github.pnavais.lab.student.model.AuthUser;
import com.github.pnavais.lab.student.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DBApplicationUserDAO implements ApplicationUserDAO {

    private final AuthUserRepository authUserRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public DBApplicationUserDAO(AuthUserRepository authUserRepository, StudentMapper studentMapper) {
        this.authUserRepository = authUserRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        AuthUser authUser = authUserRepository.findUserByName(username).orElse(null);
        return Optional.ofNullable(studentMapper.mapUser(authUser));
    }

}
