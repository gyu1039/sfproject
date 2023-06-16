package yonam2023.sfproject.employee.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yonam2023.sfproject.employee.domain.DepartmentTO;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeTO;


@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeManagerController {

    private final EmployeeManagerService es;

    @GetMapping
    public String totalList(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {


        Page<Employee> all = es.findAll(pageable);
        model.addAttribute("pageObj", all);

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
    public String byDepartment(Model model, @ModelAttribute DepartmentTO departmentTO, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Employee> byDepartment = es.findByDepartment(departmentTO, pageable);
        model.addAttribute("pageObj", byDepartment);

        return "employee/init";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        es.deleteEmployeeById(id);

        return "redirect:/employee";
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
