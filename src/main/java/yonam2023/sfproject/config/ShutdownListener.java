package yonam2023.sfproject.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
@Slf4j
public class ShutdownListener implements HttpSessionListener {


    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        log.info("sessionDestroyed 메서드 호출");
        // 세션 소멸 이벤트 처리
        HttpSession session = httpSessionEvent.getSession();
        HttpServletRequest request = (HttpServletRequest) session.getAttribute("request");
        HttpServletResponse response = (HttpServletResponse) session.getAttribute("response");
        deleteJSESSIONIDCookie(request, response);
    }

    private void deleteJSESSIONIDCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

}