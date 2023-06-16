package yonam2023.sfproject.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yonam2023.sfproject.employee.domain.Employee;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final TargetUrlFactory targetUrlFactory;

    @GetMapping("/")
    public String init(@AuthenticationPrincipal Employee employee) {

        if (employee == null) {
            return "redirect:/loginForm";
        }

        return "redirect:" + targetUrlFactory.createTargetUrl(employee.getAuthorities());
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model) {
        return "loginForm";
    }

    @GetMapping("/fail_login")
    public String fail(RedirectAttributes redirectAttributes) {
        log.info("fail 메서드 호출됨");
        redirectAttributes.addFlashAttribute("msg", "아이디나 비밀번호를 다시 확인해주세요");
        return "redirect:/loginForm";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
