package yonam2023.sfproject.employee.domain;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Employee {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private DepartmentType department;

    @Column(nullable = false)
    private EmployeeType employeeType;

    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Builder
    public Employee(DepartmentType department, EmployeeType employeeType, String name, String phoneNumber) {
        this.department = department;
        this.employeeType = employeeType;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}

