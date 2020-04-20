package com.example.demo.student.config;

import com.example.demo.student.security.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static com.example.demo.student.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.demo.student.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {

    public static final String MANAGEMENT_API = "/management/api/**";

    private final UserProvider userProvider;

    private final PersistentTokenRepository tokenRepository;

    @Autowired
    public BasicAuthConfig(UserProvider userProvider, PersistentTokenRepository tokenRepository) {
        this.userProvider = userProvider;
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
                //.loginPage("/login"); // Customize login page with thymeleaf

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(userProvider.getUsers());
    }

}
