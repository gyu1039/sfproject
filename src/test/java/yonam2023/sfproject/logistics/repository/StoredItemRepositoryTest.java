package yonam2023.sfproject.logistics.repository;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.domain.StoredItem;
import javax.persistence.EntityManager;


import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
class StoredItemRepositoryTest {

    @Autowired
    StoredItemRepository itemRepo;

    @Autowired
    EntityManager em;

    @Transactional  //Test 클래스에서 @Transactional을 붙이면 최종적으로 DB를 롤백하므로 DB에 반영되지 않음
    @Test
    public void findByIdAndSave() throws Exception{
        //given
        StoredItem item1 = new StoredItem("item1", 10);
        StoredItem savedItem = itemRepo.save(item1);
        em.flush();
        em.clear();
        //when
        StoredItem findItem = itemRepo.findById(savedItem.getId()).orElse(null);
        //then
        assertThat(findItem).isNotNull().isEqualTo(savedItem);
    }

    @Transactional  //Test 클래스에서 @Transactional을 붙이면 최종적으로 DB를 롤백하므로 DB에 반영되지 않음
    @Test
    public void Save() throws Exception{
        //given
        StoredItem item1 = new StoredItem("item1", 10);

        //when
        itemRepo.save(item1);

        //then
        log.info(item1.toString()); //id를 자동으로 삽입해줌.
    }

    @Transactional
    @Test
    public void update() throws Exception{
        //given
        StoredItem item2 = new StoredItem("item2", 11);
        StoredItem savedItem = itemRepo.saveAndFlush(item2);

        //when
        //item2나 savedItem 아무거나 삭제해도 됨. & 둘 다 갱신됨!
        item2.setName("updatedItem");
        em.flush();
        em.clear();

        //then
        StoredItem findItem = itemRepo.findById(savedItem.getId()).orElse(null);

        assertThat(findItem).isNotNull();
        assertThat(findItem.getName()).isEqualTo("updatedItem");
        assertThat(savedItem.getName()).isEqualTo("updatedItem");
        assertThat(item2.getName()).isEqualTo("updatedItem");


    }

    @Transactional
    @Test
    public void delete() throws Exception{
        //given
        StoredItem item3 = new StoredItem("item3", 13);
        StoredItem savedItem = itemRepo.saveAndFlush(item3);

        //when
        itemRepo.delete(savedItem);

        em.flush();
        em.clear();
        savedItem.setName("updatedItem3");

        //then
        StoredItem findItem = itemRepo.findById(savedItem.getId()).orElse(null);
        assertThat(findItem).isNull();
        assertThat(item3).isNotNull();
        assertThat(savedItem).isNotNull();

    }
}

