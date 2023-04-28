package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.employee.domain.DepartmentTO;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeTO;


@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService es;

    @GetMapping
    public String totalList(Model model, @PageableDefault Pageable pageable) {
        Page<Employee> all = es.findAll(pageable);
        model.addAttribute("list", all);
        return "employee/init";
    }

    @GetMapping("/add")
    public String add(Model m) {

        m.addAttribute("e", new EmployeeTO());
        return "employee/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute EmployeeTO e) {

        es.save(e);
        return "redirect:/employee";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {

        EmployeeTO byId = es.findById(id);
        model.addAttribute("e", byId);
        return "employee/edit";
    }

    @PutMapping("/edit/{id}")
    public String edit(@ModelAttribute("e") EmployeeTO e) {

        es.employeeUpdate(e);
        return "redirect:/employee";
    }

    @PostMapping("/bydepartment")
    public String byDepartment(@ModelAttribute DepartmentTO departmentTO, @PageableDefault Pageable pageable, Model model) {

        Iterable<Employee> byDepartment = es.findByDepartment(departmentTO, pageable);
        model.addAttribute("list", byDepartment);
        return "employee/init";
    }

    @ModelAttribute("d")
    public DepartmentTO d() {
        return new DepartmentTO();
    }

    @ModelAttribute("departments")
    public DepartmentType[] departments() {
        return DepartmentType.values();
    }
}
