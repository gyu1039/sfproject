package yonam2023.sfproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.employee.EmployeeService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final EmployeeService er;

    @GetMapping
    public String initPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String homePage() {

        return null;
    }
}
