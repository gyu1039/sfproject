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

}
