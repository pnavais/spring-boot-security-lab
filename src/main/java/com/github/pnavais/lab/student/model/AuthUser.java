package com.github.pnavais.lab.student.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "auth_user")
@Getter
public class AuthUser {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "auth_user_role",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<AuthRole> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "auth_user_permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "perm_id"))
    Set<AuthPermission> permissions;
}
