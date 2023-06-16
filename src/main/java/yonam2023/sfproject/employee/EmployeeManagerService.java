package yonam2023.sfproject.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.employee.domain.*;

@Service
@RequiredArgsConstructor
public class EmployeeManagerService {

    private final EmployeeManagerRepository er;
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
                .role(Role.ROLE_EMPLOYEE).build());
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

    public Page<Employee> findByDepartment(DepartmentType departmentType, Pageable pageable) {

        return er.findByDepartment(departmentType, pageable);
    }

    public void deleteEmployeeById(Long id) {

         er.deleteById(id);
    }
}
