package yonam2023.sfproject.production.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.production.domain.Production;

import java.util.List;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {
    public Page<Production> findAll(Pageable pageable);
    public List<Production> findTop10ByOrderByIdDesc();
}

