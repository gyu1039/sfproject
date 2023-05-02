package yonam2023.sfproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yonam2023.sfproject.employee.EmployeeRepository;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeType;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class DummyData {

    @Autowired
    EmployeeRepository er;

    @Autowired
    ProductionRepository pr;

    @Autowired
    MachineDataRepository mr;

    @PostConstruct
    public void initialize() {

        DepartmentType[] dts = {DepartmentType.PERSONNEL, DepartmentType.LOGISTICS, DepartmentType.PRODUCTION};

        IntStream.rangeClosed(1, 5).forEach(i -> {
            //센서 임시 추가
            MachineData m = MachineData.builder()
                    .mid(i+1)
                    .name("machine-"+i+1)
                    .state(false)
                    .description("machine-"+(i+1)+" description")
                    .build();
            mr.save(m);
            //값 임시 추가. 제거 또는 수정 필요
            Production p = Production.builder()
                    .svalue(i)
                    .build();
            pr.save(p);
        });

        IntStream.rangeClosed(1, 50).forEach(i -> {

            Employee e = Employee.builder()
                    .name("test" + i)
                    .phoneNumber(i + "")
                    .department(dts[i % 3])
                    .build();

            er.save(e);
        });

        Employee gyu1039 = Employee.builder().name("gyu1039").phoneNumber("22371002").employeeType(EmployeeType.MANAGER).department(DepartmentType.PERSONNEL).build();
        er.save(gyu1039);
        Employee ghk4889 = Employee.builder().name("ghk4889").phoneNumber("22371012").employeeType(EmployeeType.MANAGER).department(DepartmentType.LOGISTICS).build();
        er.save(ghk4889);
        Employee Ljh3141 = Employee.builder().name("Ljh3141").phoneNumber("22371018").employeeType(EmployeeType.MANAGER).department(DepartmentType.PRODUCTION).build();
        er.save(Ljh3141);
    }


}
