package yonam2023.sfproject.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.employee.domain.DepartmentType;
import yonam2023.sfproject.employee.domain.Employee;

import java.util.List;


@Repository
public interface EmployeeManagerRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findAll(Pageable pageable);
    Page<Employee> findByDepartment(DepartmentType departmentType, Pageable pageable);
    Employee findByName(String name);
    List<Employee> findByRole(String role);



    //알림기능 때문에 만듦.
    @Query("SELECT e.token FROM Employee e WHERE e.department = :dtype and e.token is not null")
    List<String> findTokensByDepartment(@Param("dtype") DepartmentType departmentType);
}
