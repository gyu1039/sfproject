package yonam2023.sfproject.employee;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.employee.domain.Employee;

import java.util.List;


@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeRepository er;

    @GetMapping
    public String test(Model model, @PageableDefault(sort = "department", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Employee> all = er.findAll(pageable);
        model.addAttribute("list", all);
        return "employee";
    }
}
