package yonam2023.sfproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/")
    public String init() {

        return "hello";
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
