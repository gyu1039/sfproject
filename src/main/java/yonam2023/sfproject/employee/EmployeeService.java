package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.employee.domain.DepartmentTO;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.Role;
import yonam2023.sfproject.employee.domain.EmployeeTO;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository er;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Page<Employee> findAll(Pageable pageable) {

        return er.findAll(pageable);
    }

    public void save(EmployeeTO e) {

        String encPassword = bCryptPasswordEncoder.encode(e.getName());
        er.save(Employee.builder()
                .name(e.getName())
                .password(encPassword)
                .phoneNumber(e.getPhoneNumber())
                .department(e.getDepartment())
                .role(Role.ROLE_USER).build());
    }

    public EmployeeTO findById(Long id) {

        Employee em = er.findById(id).get();
        EmployeeTO dto = EmployeeTO.builder().id(em.getId())
                .name(em.getName())
                .phoneNumber(em.getPhoneNumber())
                .department(em.getDepartment())
                .role(em.getRole())
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

    public Employee findByName(String id) {

        if(er.findByName(id) != null) {
            return er.findByName(id);
        }

        return null;
    }
}
