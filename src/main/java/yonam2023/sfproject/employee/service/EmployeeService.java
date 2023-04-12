package yonam2023.sfproject.employee.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeDTO;
import yonam2023.sfproject.employee.repository.EmployeeRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository er;

    public Page<Employee> findAll(Pageable pageable) {

        return er.findAll(pageable);
    }

    public void save(EmployeeDTO e) {

        er.save(Employee.builder()
                .name(e.getName())
                .phoneNumber(e.getPhoneNumber())
                .department(e.getDepartment()).build());
    }
}
