package yonam2023.sfproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import yonam2023.sfproject.config.auth.CustomAccessDeniedHandler;
import yonam2023.sfproject.config.auth.CustomFailureHandler;
import yonam2023.sfproject.config.auth.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers("/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .mvcMatchers("/", "/fail_login").permitAll()
                        .antMatchers(HttpMethod.GET, "/loginForm").hasRole("ANONYMOUS")
                        .mvcMatchers("/employee/**").hasRole("ADMIN_EMP")
                        .mvcMatchers("/production/**").hasRole("ADMIN_PRO")
                        .mvcMatchers("/storedItems/**", "/receiveRecords/**", "/sendRecords/**").hasRole("ADMIN_LO")
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/loginForm")
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
