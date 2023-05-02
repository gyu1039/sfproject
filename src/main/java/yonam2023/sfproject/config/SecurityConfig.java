package yonam2023.sfproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import yonam2023.sfproject.employee.EmployeeService;

import javax.servlet.DispatcherType;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    /*@Autowired
    private EmployeeService employeeService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers("/css/**", "/js/**", "/");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request ->
                        request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(Customizer.withDefaults());

        return http.build();
    }
*/

}
