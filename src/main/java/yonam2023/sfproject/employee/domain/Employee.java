package yonam2023.sfproject.employee.domain;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Employee {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private DepartmentType department;

    private EmployeeType employeeType;


    public void employeeUpdate(EmployeeDTO dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.department = dto.getDepartment();
        this.employeeType = dto.getEmployeeType();
    }

    @Builder
    public Employee(DepartmentType department, EmployeeType employeeType, String name, String phoneNumber) {
        this.department = department;
        this.employeeType = employeeType;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}

