package yonam2023.sfproject.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yonam2023.sfproject.production.domain.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

}

