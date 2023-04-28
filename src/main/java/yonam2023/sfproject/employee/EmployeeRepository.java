package yonam2023.sfproject.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeType;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Page<Employee> findAll(Pageable pageable);
    public Page<Employee> findByDepartment(DepartmentType departmentType, Pageable pageable);
    public List<Employee> findByEmployeeType(EmployeeType employeeType);
}
