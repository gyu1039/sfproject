package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeDTO;


@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeRepository er;

    @GetMapping
    public String page1(Model model, @PageableDefault Pageable pageable) {

        Page<Employee> all = er.findAll(pageable);
        model.addAttribute("list", all);
        return "employee/init";
    }

    @GetMapping("/add")
    public String page2(Model m) {

        m.addAttribute("e", new EmployeeDTO());
        return "employee/add";
    }

    @PostMapping("/add")
    public String page3(@ModelAttribute("e") EmployeeDTO e) {

        Employee build = Employee.builder()
                .name(e.getName())
                .phoneNumber(e.getPhoneNumber())
                .department(e.getDepartment()).build();

        er.save(build);
        return "redirect:/employee";
    }
}
