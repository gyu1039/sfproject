package yonam2023.sfproject.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .mvcMatchers("/fail_login", "/css/**", "/js/**", "/machine/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/loginForm", "/").hasRole("ANONYMOUS")
                        .mvcMatchers("/index").hasRole("ADMIN")
                        .mvcMatchers("/employee-manager/**").hasAnyRole("ADMIN_EMP", "ADMIN")
                        .mvcMatchers("/production/**").hasAnyRole("ADMIN_PRO", "ADMIN")
                        .mvcMatchers("/storedItems/**", "/receiveRecords/**", "/sendRecords/**").hasAnyRole("ADMIN_LO", "ADMIN")
                        .mvcMatchers("/employee/**").hasRole("EMPLOYEE")
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/loginForm")
                        .usernameParameter("name")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureHandler(myAuthenticationFailureHandler())
                )
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler())
                .and()
                .logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/");


        http.csrf().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler myAuthenticationFailureHandler() {
        return new CustomFailureHandler();
    }

    @Bean
    public AccessDeniedHandler myAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
