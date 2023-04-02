package yonam2023.sfproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yonam2023.sfproject.employee.EmployeeRepository;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.ProductionRepository;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class DummyData {

    @Autowired
    EmployeeRepository er;

    @Autowired
    ProductionRepository pr;

    @PostConstruct
    public void initialize() {

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Employee e = Employee.builder()
                    .name("test " + i)
                    .phoneNumber(i + " ")
                    .department(DepartmentType.PERSONNEL)
                    .build();

            er.save(e);

            Production p = Production.builder()
                    .stype("sensor1")
                    .svalue(i)
                    .build();
            pr.save(p);
        });
    }
}
