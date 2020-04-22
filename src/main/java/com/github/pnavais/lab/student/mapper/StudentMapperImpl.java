package com.github.pnavais.lab.student.mapper;

import com.github.pnavais.lab.student.model.ApplicationUser;
import com.github.pnavais.lab.student.model.AuthUser;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {

    private final DefaultMapperFactory mapperFactory;
    private final PermissionConverter permissionConverter;

    public StudentMapperImpl(PermissionConverter permissionConverter) {
        this.permissionConverter = permissionConverter;

        this.mapperFactory = new DefaultMapperFactory.Builder().build();

        // Configure converters
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(this.permissionConverter.getId(), this.permissionConverter);

        // Configure mappings
        mapperFactory.classMap(AuthUser.class, ApplicationUser.class).byDefault()
                .exclude("id")
                .exclude("roles")
                .field("name", "username")
                .fieldMap("permissions", "authorities").converter(this.permissionConverter.getId()).add()
                .register();
    }

    @Override
    public ApplicationUser mapUser(AuthUser authUser) {
        BoundMapperFacade<AuthUser, ApplicationUser> mapperFacade = mapperFactory.getMapperFacade(AuthUser.class,
                ApplicationUser.class);
        ApplicationUser appUser = mapperFacade.map(authUser);
        // Map permissions in roles to authorities
        authUser.getRoles().forEach(role -> {
            role.getPermissions()
                    .stream()
                    .map(authPermission -> permissionConverter.convert(authPermission, null, null))
                    .forEach(grantedAuthority -> appUser.getAuthorities().add(grantedAuthority));
            // Map role as authority
            appUser.getAuthorities().add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        });

        return appUser;
    }
}
