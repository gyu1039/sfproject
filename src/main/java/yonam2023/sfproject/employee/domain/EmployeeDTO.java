package yonam2023.sfproject.employee.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private DepartmentType department;
    private EmployeeType employeeType;
}
