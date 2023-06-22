package yonam2023.sfproject.employee.domain;

import lombok.*;

import java.time.LocalDateTime;

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
    private Role role;
    private boolean isCheckedIn;
    private LocalDateTime checkInTime;

    public Employee toEntity() {
        return Employee.builder()
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .department(this.department)
                .role(this.role)
                .isCheckedIn(this.isCheckedIn)
                .checkInTime(this.checkInTime)
                .build();
    }


}
