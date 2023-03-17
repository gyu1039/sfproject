package yonam2023.sfproject.employee;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class Employee {

    @Id
    @NotNull
    private Long id;

    private String department;
    private String phoneNumber;

}
