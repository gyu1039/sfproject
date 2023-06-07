package yonam2023.sfproject.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.employee.EmployeeRepository;
import yonam2023.sfproject.employee.domain.Employee;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("{}, {}", "loadUserByUsername 메서드 호출", username);
        Employee em = employeeRepository.findByName(username);
        log.info("{}", em);
        if(em == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(em.getRole().getRole()));
        em.setAuthorities(roles);

        return new MyUserDetails(em);
    }
}
