package yonam2023.sfproject.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class DummyData {

    @Autowired
    EmployeeRepository er;

    @PostConstruct
    public void initialize() {

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Employee e = Employee.builder()
                    .name("test " + i)
                    .phoneNumber(i + " ")
                    .department(DepartmentType.PERSONNEL)
                    .build();

            er.save(e);
        });
    }
}
