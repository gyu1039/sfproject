package yonam2023.sfproject.employee.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
public class DepartmentTO {
    private DepartmentType departmentType;
}
