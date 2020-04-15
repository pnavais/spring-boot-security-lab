package com.example.demo.student.security;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static com.example.demo.student.security.ApplicationUserRole.*;

@Component
public class UserProviderImpl implements UserProvider {

    private final PasswordEncoder passwordEncoder;
    private final Set<UserDetails> users;

    @Autowired
    public UserProviderImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        users = Sets.newHashSet(
                createUser("anna",  "password",    STUDENT),
                createUser("linda", "password123", ADMIN),
                createUser("tom",   "password123", ADMIN_TRAINEE)
        );
    }

    private UserDetails createUser(String userName, String password,
                                   ApplicationUserRole role) {
        return User.builder().username(userName)
                .password(passwordEncoder.encode(password))
                .authorities(role.getGrantedAuthorities())
                //.roles(role.name()) // ROLE_role
                .build();
    }

    @Override
    public Collection<UserDetails> getUsers() {
        return users;
    }
}
