package yonam2023.sfproject.logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yonam2023.sfproject.logistics.domain.SendRecord;

//@Repository  //생략해도 스프링 빈으로 등록됨
public interface SendRecordRepository extends JpaRepository<SendRecord, Long> {

}
