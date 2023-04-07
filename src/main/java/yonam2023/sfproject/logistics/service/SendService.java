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
@Service
public class SendService {
    private final SendRecordRepository sendRecordRepo;
    private final StoredItemRepository storedItemRepo;

    @Transactional
    public Long saveSendRecord(SendForm.Request sendReqForm){
        SendRecord sendRecord = sendReqForm.toEntity();
        sendRecordRepo.save(sendRecord);

        StoredItem storedItem = storedItemRepo.findByName(sendRecord.getItemName());

        if(storedItem == null){
            //todo: 재고에 없는 상품을 출고하는 경우. 나중에 처리.
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
        targetRecord.setAmount(sendReqForm.getAmount());
        targetRecord.setDateTime(sendReqForm.getDateTime());

    }

}
