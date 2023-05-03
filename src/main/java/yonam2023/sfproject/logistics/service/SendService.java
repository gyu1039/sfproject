package yonam2023.sfproject.logistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.controller.form.SendForm;
import yonam2023.sfproject.logistics.domain.ReceiveRecord;
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

    @Transactional
    public void deleteReceiveRecord(long recordId){

        SendRecord findRecord = sendRecordRepo.findById(recordId).orElseThrow();
        StoredItem byNameItem = storedItemRepo.findByName(findRecord.getItemName());
        if(byNameItem == null){
            storedItemRepo.save(new StoredItem(findRecord.getItemName(), findRecord.getAmount()));
        }
        else{
            byNameItem.addAmount(findRecord.getAmount());
        }
        sendRecordRepo.deleteById(recordId);
    }

}
