package yonam2023.sfproject.employee.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @NotNull
    private Long id;

    private DepartmentType department;
    private EmployeeType employeeType;
    private String name;
    private String phoneNumber;
}

enum EmployeeType {

    MANAGER("관리자"), NORMAL("일반 직원");

    private final String value;

    EmployeeType(String value) {
        this.value = value;
    }
}

enum DepartmentType {

    PERSONNEL("인사"), PRODUCTION("생산"), LOGISTICS("물류");

    private final String value;

    DepartmentType(String value) {

        this.value = value;
    }
}