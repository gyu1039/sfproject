package yonam2023.sfproject.logistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.controller.form.ReceiveForm;
import yonam2023.sfproject.logistics.domain.ReceiveRecord;
import yonam2023.sfproject.logistics.domain.StoredItem;
import yonam2023.sfproject.logistics.repository.ReceiveRecordRepository;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;

@RequiredArgsConstructor
@Service
public class ReceiveService {
    private final ReceiveRecordRepository receiveRecordRepo;
    private final StoredItemRepository storedItemRepo;

    @Transactional
    public Long saveReceiveRecord(ReceiveForm.Request receiveReqForm){
        ReceiveRecord receiveRecord = receiveReqForm.toEntity();
        receiveRecordRepo.save(receiveRecord);

        StoredItem storedItem = storedItemRepo.findByName(receiveRecord.getItemName());

        if(storedItem == null){
            return storedItemRepo.save(new StoredItem(receiveRecord.getItemName(),receiveRecord.getAmount())).getId();
        }
        else{
            storedItem.addAmount(receiveRecord.getAmount());
            return storedItem.getId();
        }

    }

    @Transactional
    public void editReceiveRecord(long id, ReceiveForm.Request receiveReqForm){
        ReceiveRecord targetRecord = receiveRecordRepo.findById(id).orElseThrow();
        StoredItem storedItem = storedItemRepo.findByName(receiveReqForm.getItemName());

        int previousReservedAmount = targetRecord.getAmount();

        //입고 예약 수정
        targetRecord.setAmount(receiveReqForm.getAmount());
        targetRecord.setDateTime(receiveReqForm.getDateTime());

        //재고 수정
        storedItem.subAmount(previousReservedAmount);       //원상 복구
        storedItem.addAmount(receiveReqForm.getAmount());   //수정된 입고량 반영
    }

    @Transactional
    public void deleteReceiveRecord(long recordId){

        ReceiveRecord findRecord = receiveRecordRepo.findById(recordId).orElseThrow();
        StoredItem byNameItem = storedItemRepo.findByName(findRecord.getItemName());
        if(byNameItem == null){
            storedItemRepo.save(new StoredItem(findRecord.getItemName(), -findRecord.getAmount()));
        }
        else{
            byNameItem.subAmount(findRecord.getAmount());
        }
        receiveRecordRepo.deleteById(recordId);
    }

}
