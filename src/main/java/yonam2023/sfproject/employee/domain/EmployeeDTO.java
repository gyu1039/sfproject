package yonam2023.sfproject.employee.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmployeeDTO {

    private String name;
    private String phoneNumber;
    private DepartmentType department;
    private EmployeeType employee;
}
