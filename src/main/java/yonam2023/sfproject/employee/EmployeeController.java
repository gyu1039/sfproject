package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeDTO;


@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeRepository er;

    @GetMapping
    public String totalList(Model model, @PageableDefault Pageable pageable) {

        Page<Employee> all = er.findAll(pageable);
        model.addAttribute("list", all);

        return "employee/init";
    }

    @GetMapping("/add")
    public String add(Model m) {

        m.addAttribute("e", new EmployeeDTO());
        return "employee/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("e") EmployeeDTO e) {

        er.save(Employee.builder()
                .name(e.getName())
                .phoneNumber(e.getPhoneNumber())
                .department(e.getDepartment()).build());
        return "redirect:/employee";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Employee em = er.findById(id).get();
        EmployeeDTO dto = EmployeeDTO.builder().id(em.getId())
                .name(em.getName())
                .phoneNumber(em.getPhoneNumber())
                .department(em.getDepartment())
                .employeeType(em.getEmployeeType())
                .build();

        model.addAttribute("e", dto);
        return "employee/edit";
    }

    @PutMapping("/edit/{id}")
    public String edit(@ModelAttribute("e") EmployeeDTO e) {

        Employee employee = er.findById(e.getId()).get();
        employee.employeeUpdate(e);
        er.save(employee);
        return "redirect:/employee";
    }
}
