package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.employee.domain.DepartmentTO;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeTO;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository er;

    public Page<Employee> findAll(Pageable pageable) {

        return er.findAll(pageable);
    }

    public void save(EmployeeTO e) {

        er.save(Employee.builder()
                .name(e.getName())
                .phoneNumber(e.getPhoneNumber())
                .department(e.getDepartment()).build());
    }

    public EmployeeTO findById(Long id) {

        Employee em = er.findById(id).get();
        EmployeeTO dto = EmployeeTO.builder().id(em.getId())
                .name(em.getName())
                .phoneNumber(em.getPhoneNumber())
                .department(em.getDepartment())
                .employeeType(em.getEmployeeType())
                .build();

        return dto;
    }

    public void employeeUpdate(EmployeeTO e) {

        Employee employee = er.findById(e.getId()).get();
        employee.employeeUpdate(e);
        er.save(employee);
    }

    public Iterable<Employee> findByDepartment(DepartmentTO departmentTO, Pageable pageable) {

        return er.findByDepartment(departmentTO.getDepartmentType(), pageable);
    }
}
