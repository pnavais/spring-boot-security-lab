package com.github.pnavais.lab.student.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ApplicationUserRole {

    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.STUDENT_READ, ApplicationUserPermission.STUDENT_WRITE, ApplicationUserPermission.COURSE_READ, ApplicationUserPermission.COURSE_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(ApplicationUserPermission.STUDENT_READ, ApplicationUserPermission.COURSE_READ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        // Add permissions as list of authorities
        Set<GrantedAuthority> authorities =
                getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());

        // Add Role as last authority
        authorities.add(new SimpleGrantedAuthority("ROLE_"+name()));

        return authorities;
    }
}
