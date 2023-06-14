package yonam2023.sfproject.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yonam2023.sfproject.config.security.MyUserDetails;
import yonam2023.sfproject.employee.EmployeeRepository;
import yonam2023.sfproject.employee.domain.Employee;

import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final EmployeeRepository employeeRepository;

    //todo: 부서별로 요청을 처리할 수 있도록 path variable 사용
    @PostMapping("/register")
    @Transactional  //@Transactional이 있어야 JPA 더티체킹이 동작한다.
    public ResponseEntity register(@RequestBody String userToken, HttpSession httpSession) {
        SecurityContextImpl ssc = (SecurityContextImpl) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        MyUserDetails user = (MyUserDetails) ssc.getAuthentication().getPrincipal();
        System.out.println(user.getUsername());

        Employee employee = employeeRepository.findByName(user.getUsername());
        System.out.println("userToken = " + userToken);
        employee.setToken(userToken);
        return ResponseEntity.ok().build();
    }

// 로그인 구현이 완료되면 UserSession으로 현재 로그인한 유저 정보가 들어오도록 구현. (인터셉터나 세션 따위를 사용)
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody String userToken, UserSession userSession) {
//        notificationService.register(userSession.getId(), userToken);
//        return ResponseEntity.ok().build();
//    }

}
