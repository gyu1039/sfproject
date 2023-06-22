package yonam2023.sfproject.logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yonam2023.sfproject.logistics.domain.StoredItem;


//@Repository  //생략해도 스프링 빈으로 등록됨
public interface StoredItemRepository extends JpaRepository<StoredItem, Long> {

    StoredItem findByName(String name);
}
