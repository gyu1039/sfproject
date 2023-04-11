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
        model.addAttribute("today", LocalDate.now());
        return "logistics/receiveRecord/receiveRecords";
    }

    // add form page
    @GetMapping("/reserve")
    public String reserveForm(Model model){
        model.addAttribute("today", LocalDate.now());
        return "logistics/receiveRecord/receiveReserveForm";
    }

    @PostMapping("/reserve")
    public String reserveItem(@ModelAttribute ReceiveForm.Request receiveReqForm){
        receiveService.saveReceiveRecord(receiveReqForm);
        return "redirect:/receiveRecords";
    }

    // edit form page
    @GetMapping("/{recordId}/edit")
    public String editForm(@PathVariable long recordId, Model model){
        ReceiveRecord targetRecord = receiveRecordRepo.findById(recordId).orElse(null);
        model.addAttribute("record", targetRecord);
        model.addAttribute("today", LocalDate.now());
        return "logistics/receiveRecord/receiveEditForm";
    }

    @PatchMapping("/{recordId}")
    public String editReserveRecord(@PathVariable long recordId, @ModelAttribute ReceiveForm.Request receiveReqForm){
        receiveService.editReceiveRecord(recordId, receiveReqForm);
        return "redirect:/receiveRecords";
    }
    @DeleteMapping("/{recordId}")
    @ResponseBody
    public ResponseEntity deleteReserve(@PathVariable long recordId){
        receiveService.deleteReceiveRecord(recordId);
        return ResponseEntity.ok(recordId);
    }

}
