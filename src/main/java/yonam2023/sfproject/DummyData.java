package yonam2023.sfproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import yonam2023.sfproject.employee.EmployeeRepository;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.Role;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
@Slf4j
public class DummyData {

    @Autowired
    EmployeeRepository er;

    @Autowired
    ProductionRepository pr;

    @Autowired
    MachineDataRepository mr;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void initialize() {

        DepartmentType[] dts = {DepartmentType.PERSONNEL, DepartmentType.LOGISTICS, DepartmentType.PRODUCTION};

        IntStream.rangeClosed(1, 50).forEach(i -> {
            //센서 임시 추가
            MachineData m = MachineData.builder()
                    .mid(1010+i)
                    .name("machine-"+(1010+i))
                    .state(false)
                    .description("machine-"+(i+1010 )+" description")
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

        String encode = bCryptPasswordEncoder.encode("gyu1039");
        Employee gyu1039 = Employee.builder().name("gyu1039").password(encode).phoneNumber("22371002").role(Role.ROLE_ADMIN_EMP).department(DepartmentType.PERSONNEL).build();
        er.save(gyu1039);

        String encode1 = bCryptPasswordEncoder.encode("ghk4889");
        Employee ghk4889 = Employee.builder().name("ghk4889").password(encode1).phoneNumber("22371012").role(Role.ROLE_ADMIN_LO).department(DepartmentType.LOGISTICS).build();
        er.save(ghk4889);

        String encode2 = bCryptPasswordEncoder.encode("Ljh3141");
        Employee Ljh3141 = Employee.builder().name("Ljh3141").password(encode2).phoneNumber("22371018").role(Role.ROLE_ADMIN_PRO).department(DepartmentType.PRODUCTION).build();
        er.save(Ljh3141);

        String encode3 = bCryptPasswordEncoder.encode("test");
        Employee admin = Employee.builder().name("test").password(encode3).phoneNumber("0000").department(DepartmentType.TEST).role(Role.ROLE_ADMIN).build();
        er.save(admin);
    }


}
