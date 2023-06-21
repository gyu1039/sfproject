package yonam2023.sfproject.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.employee.EmployeeManagerRepository;
import yonam2023.sfproject.employee.domain.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    @Autowired
    private EmployeeManagerRepository employeeManagerRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .mvcMatchers("/fail_login", "/css/**", "/js/**", "/machine/**", "/").permitAll()
                        .antMatchers(HttpMethod.GET, "/loginForm").hasRole("ANONYMOUS")
                        .mvcMatchers("/index").hasRole("ADMIN")
                        .mvcMatchers("/employee/**").hasAnyRole("ADMIN_EMP", "ADMIN")
                        .mvcMatchers("/production/**").hasAnyRole("ADMIN_PRO", "ADMIN")
                        .mvcMatchers("/storedItems/**", "/receiveRecords/**", "/sendRecords/**").hasAnyRole("ADMIN_LO", "ADMIN")
                        .mvcMatchers("/normal/**").hasRole("EMPLOYEE")
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
                .logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(myLogoutSuccessHandler(employeeManagerRepository));
                //.logoutSuccessUrl("/loginForm")


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

    @Bean
    public LogoutSuccessHandler myLogoutSuccessHandler(EmployeeManagerRepository employeeManagerRepository) {
        return new LogoutSuccessHandler() {
            @Override
            @Transactional  //@Transactional이 있어야 JPA 더티체킹이 동작한다.
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                Employee user = (Employee)authentication.getPrincipal();
                Employee employee = employeeManagerRepository.findByName(user.getUsername());
                log.info("[logout success handler] 삭제 직전 token = " + employee.getToken());
                employee.deleteToken();
                response.sendRedirect("/loginForm");
            }
        };
    }
}
