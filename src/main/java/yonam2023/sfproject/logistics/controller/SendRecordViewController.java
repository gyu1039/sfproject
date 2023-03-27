package yonam2023.sfproject.logistics.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.logistics.domain.SendRecord;
import yonam2023.sfproject.logistics.repository.SendRecordRepository;

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
    public String sendRecordsHome(Model model){
        List<SendRecord> sendRecords = sendRecordRepo.findAll();
        model.addAttribute("sendRecords", sendRecords);
        return "logistics/sendRecords";
    }


}
