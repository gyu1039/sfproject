package yonam2023.sfproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import yonam2023.sfproject.config.auth.MyLoginSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers("/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http
                .authorizeHttpRequests((requests) -> requests
                        .mvcMatchers("/", "/hello").permitAll()
                        .mvcMatchers("/employee/**").hasRole("ADMIN_EMP")
                        .mvcMatchers("/production/**").hasRole("ADMIN_PRO")
                        .mvcMatchers("/storedItems/**", "/receiveRecords/**", "/sendRecords/**").hasRole("ADMIN_LO")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/loginForm")
                        .permitAll()
                        .loginProcessingUrl("/loginForm")
                        .successHandler(new MyLoginSuccessHandler())
                        .defaultSuccessUrl("/index")
                        .failureUrl("/loginForm")
                )
                .exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendRedirect("/loginForm"));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


}
