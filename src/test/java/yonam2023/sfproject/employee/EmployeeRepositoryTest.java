package yonam2023.sfproject.employee;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.manager.EmployeeManagerRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeManagerRepository em;

    DepartmentType[] dts = {DepartmentType.PERSONNEL, DepartmentType.LOGISTICS, DepartmentType.PRODUCTION};


}