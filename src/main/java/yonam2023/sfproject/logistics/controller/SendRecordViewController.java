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
import yonam2023.sfproject.logistics.controller.form.SendForm;
import yonam2023.sfproject.logistics.domain.SendRecord;
import yonam2023.sfproject.logistics.repository.SendRecordRepository;
import yonam2023.sfproject.logistics.service.SendService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/sendRecords")
@Controller
@RequiredArgsConstructor
@Slf4j
public class SendRecordViewController {
    private final SendRecordRepository sendRecordRepo;
    private final SendService sendService;

    @PostConstruct
    public void init(){
        List<SendRecord> sendRecords = new ArrayList<>();
        LocalDate date = Year.of(2023).atMonth(3).atDay(20);
        for(int i = 0; i<20; i++){
            sendRecords.add(new SendRecord("item "+i, 11, "부산", date.atTime(15, i+1)));
        }
        sendRecordRepo.saveAll(sendRecords);
    }

    @GetMapping
    public String sendRecordsHome(Model model, @PageableDefault(sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable){
        Page<SendRecord> all = sendRecordRepo.findAll(pageable);
        model.addAttribute("pageObj", all);

        return "logistics/sendRecord/sendRecords";
    }

    @GetMapping("/reserve")
    public String reserveForm(Model model){
        model.addAttribute("today", LocalDate.now());
        return "logistics/sendRecord/sendReserveForm";
    }

    @PostMapping("/reserve")
    public String reserveItem(@ModelAttribute SendForm.Request sendReqForm){
        sendService.saveSendRecord(sendReqForm);
        // todo: 값 검증 로직 추가하기. ex) 재고량을 초과하는 출고량이 입력되면 경고 문구.
        return "redirect:/sendRecords";
    }

    // edit form page
    @GetMapping("/{recordId}/edit")
    public String editForm(@PathVariable long recordId, Model model){
        SendRecord targetRecord = sendRecordRepo.findById(recordId).orElse(null);
        model.addAttribute("record", targetRecord);
        model.addAttribute("today", LocalDate.now());
        return "logistics/sendRecord/sendEditForm";
    }

    @PatchMapping("/{recordId}")
    public String editReserveRecord(@PathVariable long recordId, @ModelAttribute SendForm.Request sendReqForm){
        sendService.editSendRecord(recordId, sendReqForm);
        return "redirect:/sendRecords";
    }

    @DeleteMapping("/{recordId}")
    @ResponseBody
    public ResponseEntity deleteReserve(@PathVariable long recordId){
        sendService.deleteReceiveRecord(recordId);
        return ResponseEntity.ok(recordId);
    }


}
