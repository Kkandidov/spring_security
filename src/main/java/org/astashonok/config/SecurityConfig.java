package org.astashonok.config;

import org.astashonok.model.Permission;
import org.astashonok.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)

                .withUser("user")
                .password(passwordEncoder.encode("user"))
                .authorities(Role.USER.getGrantedAuthorities())

                .and()

                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities(Role.ADMIN.getGrantedAuthorities())
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login")
                .permitAll()

                .antMatchers(HttpMethod.POST, "/api/**")
                .hasAuthority(Permission.WRITE.getPermission())

                .antMatchers(HttpMethod.DELETE, "/api/**")
                .hasAuthority(Permission.WRITE.getPermission())

                .antMatchers(HttpMethod.GET, "/api/**")
                .hasAnyAuthority(Permission.READ.getPermission(), Permission.WRITE.getPermission())

                .and()
                .formLogin()

                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll()

                .and()
                .csrf()
                .disable();
    }
}
