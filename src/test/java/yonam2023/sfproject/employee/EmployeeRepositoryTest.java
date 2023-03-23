package yonam2023.sfproject.employee;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeType;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository em;


    @BeforeAll
    public void insertDummy() {


        Employee e = Employee.builder()
                .name("강인규")
                .department(DepartmentType.PERSONNEL)
                .employeeType(EmployeeType.MANAGER)
                .phoneNumber("010-1111-111")
                .build();

        em.save(e);
    }

    @Test
    public void select_all() {

        List<Employee> all = em.findAll();
        all.forEach(System.out::println);
    }

}