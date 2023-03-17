package yonam2023.sfproject.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.employee.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
