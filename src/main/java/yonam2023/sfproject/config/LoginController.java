package yonam2023.sfproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class LoginController {

    @GetMapping({"/"})
    public String init() {

        return "hello";
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model) {
        log.info("{}", model.getAttribute("msg"));
        return "loginForm";
    }

    @GetMapping("/fail_login")
    public String fail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("msg", "아이디나 비밀번호를 확인해주세요");
        return "redirect:/loginForm";
    }

    @GetMapping("/index")
    public String index() {

        return "index";
    }
}
