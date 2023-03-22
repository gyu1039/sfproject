package yonam2023.sfproject.logistics.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.domain.StoredItem;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class StoredItemRepositoryTest {

    @Autowired
    StoredItemRepository itemRepo;

    @Autowired
    EntityManager em;

    @Transactional
    @Test
    public void findById() throws Exception{
        //given
        StoredItem item1 = new StoredItem("item1", 10);
        //when
        StoredItem savedItem = itemRepo.saveAndFlush(item1);
        em.flush();
        em.clear();
        //then
        StoredItem findItem = itemRepo.findById(savedItem.getId()).orElse(null);
        assertThat(findItem).isNotNull().isEqualTo(savedItem);


    }
}