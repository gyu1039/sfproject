package yonam2023.sfproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin((form) -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/index")
                        .failureUrl("/")
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


}
