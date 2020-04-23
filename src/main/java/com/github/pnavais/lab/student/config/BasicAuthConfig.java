package com.github.pnavais.lab.student.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static com.github.pnavais.lab.student.security.ApplicationUserPermission.COURSE_WRITE;
import static com.github.pnavais.lab.student.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {

    public static final String MANAGEMENT_API = "/management/api/**";

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final PersistentTokenRepository tokenRepository;

    @Autowired
    public BasicAuthConfig(PasswordEncoder passwordEncoder,
                           @Qualifier("appUserService") UserDetailsService userDetailsService,
                           PersistentTokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()/*csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()*/
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*", "/swagger-ui*", "/login*", "/favicon.ico")
                .permitAll()
                .antMatchers("/api/**")
                .hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE, MANAGEMENT_API).hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, MANAGEMENT_API).hasAuthority(COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.PUT, MANAGEMENT_API).hasAuthority(COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.GET, MANAGEMENT_API).hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin() /* Form base auth, previously basic auth => .httpBasic() */
                .and()
                .rememberMe()
                .tokenRepository(tokenRepository);
                //.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")); // Change logout to POST when CSRF enabled
                //.loginPage("/login"); // Customize login page with thymeleaf
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Probably not needed, Spring Boot already builds it
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        // Probably not needed, Spring Boot already builds it
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
