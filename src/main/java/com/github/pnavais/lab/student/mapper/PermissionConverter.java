package com.github.pnavais.lab.student.mapper;

import com.github.pnavais.lab.student.model.AuthPermission;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class PermissionConverter extends CustomConverter<AuthPermission, GrantedAuthority> {
    @Override
    public GrantedAuthority convert(AuthPermission permission, Type<? extends GrantedAuthority> destinationType, MappingContext mappingContext) {
        return new SimpleGrantedAuthority(permission.getName());
    }

    public String getId() {
        return getClass().getSimpleName();
    }
}
