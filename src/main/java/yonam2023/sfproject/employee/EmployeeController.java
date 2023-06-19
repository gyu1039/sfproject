package yonam2023.sfproject.employee;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.employee.domain.Employee;
import yonam2023.sfproject.employee.domain.EmployeeTO;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Controller
@RequestMapping("/normal")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeManagerService ems;

    @GetMapping
    public String init(@AuthenticationPrincipal Employee employee, Model model) {

        log.info("init메서드 진입");
        model.addAttribute("employee", employee);
        return "employee/dashboard";
    }

    @GetMapping("/{id}")
    public String init(Model model, @PathVariable Integer id) {

        log.info("pathVariable 메서드 진입");
        Employee employee = ems.findById(id.longValue()).toEntity();
        log.info("{}", employee);
        model.addAttribute("employee", employee);

        LocalTime officeStartTime = LocalTime.of(9, 0);
        LocalDateTime now = employee.getCheckInTime();
        if (now.toLocalTime().isAfter(officeStartTime)) {
            long minutesLate = now.toLocalTime().until(officeStartTime, ChronoUnit.MINUTES);
            model.addAttribute("lateMinutes", -1* minutesLate);
        }

        return "employee/dashboard";
    }

    @PostMapping("/check-in")
    public String checkIn(@RequestParam Integer employeeId, Model model) {

        log.info("checkin 메서드");

        EmployeeTO e = ems.findById(employeeId.longValue());
        LocalDateTime now = LocalDateTime.now();
        e.setCheckInTime(now);
        e.setCheckedIn(true);



        log.info("{}", e);
        ems.employeeUpdate(e);

        return "redirect:/normal/" + employeeId;
    }

    @PostMapping("/check-out")
    public String checkOut(@RequestParam Integer employeeId, Model model) {

        log.info("checkout 메서드");
        EmployeeTO e = ems.findById(employeeId.longValue());
        e.setCheckedIn(false);
        log.info("{}", e);
        ems.employeeUpdate(e);

        return "redirect:/normal/" + employeeId;
    }
}
