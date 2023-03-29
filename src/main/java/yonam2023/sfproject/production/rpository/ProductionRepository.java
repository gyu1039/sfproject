package yonam2023.sfproject.production.rpository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.production.domain.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {
    public Page<Production> findAll(Pageable pageable);
}

