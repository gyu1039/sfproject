package yonam2023.sfproject.logistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.controller.form.SendForm;
import yonam2023.sfproject.logistics.domain.SendRecord;
import yonam2023.sfproject.logistics.domain.StoredItem;
import yonam2023.sfproject.logistics.repository.SendRecordRepository;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;

@RequiredArgsConstructor
//@Service
public class SendService {
    private final SendRecordRepository sendRecordRepo;
    private final StoredItemRepository storedItemRepo;

    @Transactional
    public Long saveSendRecord(SendForm.Request sendReqForm){
        SendRecord sendRecord = sendReqForm.toEntity();
        sendRecordRepo.save(sendRecord);

        StoredItem storedItem = storedItemRepo.findByName(sendRecord.getItemName());

        if(storedItem == null){
            return storedItemRepo.save(new StoredItem(sendRecord.getItemName(),-sendRecord.getAmount())).getId();
        }
        else{
            storedItem.subAmount(sendRecord.getAmount());
            return storedItem.getId();
        }
    }

    @Transactional
    public void editSendRecord(long id, SendForm.Request sendReqForm){
        SendRecord targetRecord = sendRecordRepo.findById(id).orElseThrow();
        StoredItem storedItem = storedItemRepo.findByName(sendReqForm.getItemName());

        int previousReservedAmount = targetRecord.getAmount();

        // 출고 예약 수정
        targetRecord.setAmount(sendReqForm.getAmount());
        targetRecord.setDateTime(sendReqForm.getDateTime());

        //재고 수정
        storedItem.addAmount(previousReservedAmount); //원상 복구
        storedItem.subAmount(sendReqForm.getAmount()); //수정된 출고 amount 반영
    }

    @Transactional
    public void deleteSendRecord(long recordId){

        SendRecord findRecord = sendRecordRepo.findById(recordId).orElseThrow();
        StoredItem byNameItem = storedItemRepo.findByName(findRecord.getItemName());
        // 출고 예약을 삭제하려고 하는데 예약된 게 없는 경우 byNameItem == null이 된다. 그런데 이런 경우가
        // 실제로 발생할 수 있을 것 같진 않다. 출고 예약 목록에 데이터가 존재해서 고객이
        if(byNameItem == null){
            storedItemRepo.save(new StoredItem(findRecord.getItemName(), findRecord.getAmount()));
        }
        else{
            byNameItem.addAmount(findRecord.getAmount());
        }
        sendRecordRepo.deleteById(recordId);
    }

}
