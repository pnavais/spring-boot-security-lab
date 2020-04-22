package com.github.pnavais.lab.student.mapper;

import com.github.pnavais.lab.student.model.ApplicationUser;
import com.github.pnavais.lab.student.model.AuthUser;

public interface StudentMapper {

    /**
     * Maps an auth user to security user.
     *
     * @param authUser the auth user
     * @return the security user
     */
    ApplicationUser mapUser(AuthUser authUser);
}
