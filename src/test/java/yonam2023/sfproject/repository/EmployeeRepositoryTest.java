package yonam2023.sfproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.EmployeeRepository;

@SpringBootTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void saveEmployee(){

        //given
//        Employee em = new Employee(1L, "직원 관리", "010-1233-3333");


    }
}

