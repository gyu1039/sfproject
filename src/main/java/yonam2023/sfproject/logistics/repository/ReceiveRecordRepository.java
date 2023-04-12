package yonam2023.sfproject.logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yonam2023.sfproject.logistics.domain.ReceiveRecord;

//@Repository  //생략해도 스프링 빈으로 등록됨
public interface ReceiveRecordRepository extends JpaRepository<ReceiveRecord, Long> {

}
