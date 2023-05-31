package yonam2023.sfproject.employee;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository em;

    DepartmentType[] dts = {DepartmentType.PERSONNEL, DepartmentType.LOGISTICS, DepartmentType.PRODUCTION};

    @BeforeAll
    public void insertDummy() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Employee e = Employee.builder()
                    .name("test" + i)
                    .department(dts[i % 3])
                    .phoneNumber(i + "")
                    .build();

            em.save(e);
        });

    }

    @Test
    public void select_all() {

        List<Employee> all = em.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void 부서별_검색() {

        Iterable<Employee> byDepartment = em.findByDepartment(dts[0], PageRequest.ofSize(10));
        byDepartment.forEach(System.out::println);
    }
}