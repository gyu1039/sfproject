package yonam2023.sfproject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yonam2023.sfproject.employee.EmployeeService;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeType;

//@Controller
//@RequiredArgsConstructor
//@Slf4j
public class HomeController {

//    private final EmployeeService es;
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login/login";
//    }
//
//    @PostMapping("/login")
//    public String homePage(@RequestParam("username") String name, @RequestParam("password") String password) {
//
//        Employee byName = es.findByName(name);
//        log.info("{}", byName);
//        if (byName != null && byName.getEmployeeType() == EmployeeType.MANAGER && name.equals(password)) {
//            return "login/home";
//        }
//
//        return "login/login";
//
//    }
}
