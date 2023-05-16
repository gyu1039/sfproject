package yonam2023.sfproject.logistics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.logistics.controller.form.ReceiveForm;
import yonam2023.sfproject.logistics.domain.ReceiveRecord;
import yonam2023.sfproject.logistics.repository.ReceiveRecordRepository;
import yonam2023.sfproject.logistics.service.ReceiveService;
import yonam2023.sfproject.notification.fcm.NotifyService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/receiveRecords")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ReceiveRecordViewController {
    private final ReceiveRecordRepository receiveRecordRepo;
    private final ReceiveService receiveService;
    private final NotifyService notifyService;

    //더미 데이터 생성.
    @PostConstruct
    public void init(){
        List<ReceiveRecord> receiveRecords = new ArrayList<>();
        LocalDate date = Year.of(2023).atMonth(3).atDay(12);
        for(int i = 0; i<20; i++){
            receiveRecords.add(new ReceiveRecord("item "+i, 11, "XX 회사",date.atTime(15, i+1)));
        }

        receiveRecordRepo.saveAll(receiveRecords);
    }


    @GetMapping
    public String receiveRecordsHome(Model model, @PageableDefault(sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ReceiveRecord> all = receiveRecordRepo.findAll(pageable);
        model.addAttribute("pageObj", all);
        return "logistics/receiveRecord/receiveRecords";
    }

    // add form page
    @GetMapping("/reserve")
    public String reserveForm(Model model){
        return "logistics/receiveRecord/receiveReserveForm";
    }

    @PostMapping("/reserve")
    public String reserveItem(@ModelAttribute ReceiveForm.Request receiveReqForm) {
        receiveService.saveReceiveRecord(receiveReqForm);
        //todo: 특정 부서의 사용자들에게만 알림을 보내도록 수정
        notifyService.allUserNotify("[입고 예약 알림]", receiveReqForm.getItemName()+"이 입고되었습니다!");
        return "redirect:/receiveRecords";
    }

    // edit form page
    @GetMapping("/{recordId}/edit")
    public String editForm(@PathVariable long recordId, Model model){
        ReceiveRecord targetRecord = receiveRecordRepo.findById(recordId).orElse(null);
        model.addAttribute("record", targetRecord);
        return "logistics/receiveRecord/receiveEditForm";
    }

    @PatchMapping("/{recordId}")
    public String editReserveRecord(@PathVariable long recordId, @ModelAttribute ReceiveForm.Request receiveReqForm){
        receiveService.editReceiveRecord(recordId, receiveReqForm);
        notifyService.allUserNotify("[입고 수정 알림]", receiveReqForm.getItemName()+"의 입고예약이 수정되었습니다!");
        return "redirect:/receiveRecords";
    }
    @DeleteMapping("/{recordId}")
    @ResponseBody
    public ResponseEntity deleteReserve(@PathVariable long recordId){
        String deletedItemName = receiveService.deleteReceiveRecord(recordId);
        notifyService.allUserNotify("[입고 취소 알림]", deletedItemName+"의 입고예약이 취소되었습니다!");
        return ResponseEntity.ok(recordId);
    }

}
