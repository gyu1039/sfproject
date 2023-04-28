package yonam2023.sfproject.employee.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTO {

    private Long id;
    private String name;
    private String password;
    private String phoneNumber;
    private DepartmentType department;
    private EmployeeType employeeType;

    public Employee toEntity() {
        return new Employee();
    }
}
