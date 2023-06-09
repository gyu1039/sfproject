package yonam2023.sfproject.notification.fcm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.EmployeeManagerRepository;

import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationApiController {

    private final EmployeeManagerRepository employeeManagerRepository;

    @PostMapping("/register")
    @Transactional  //@Transactional이 있어야 JPA 더티체킹이 동작한다.
    public ResponseEntity register(@RequestBody String userToken, HttpSession httpSession) {
        SecurityContextImpl ssc = (SecurityContextImpl) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
      
        Employee loginUser = (Employee) ssc.getAuthentication().getPrincipal();
        Employee employee = employeeManagerRepository.findByName(loginUser.getUsername());
      
        employee.setToken(userToken);
        return ResponseEntity.ok().build();
    }

}
