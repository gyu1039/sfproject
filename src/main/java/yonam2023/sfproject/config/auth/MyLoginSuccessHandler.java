package yonam2023.sfproject.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


@Slf4j
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<GrantedAuthority> roles = new ArrayList<>();
        authentication.getAuthorities().forEach(auth -> {
            roles.add(auth);
        });
        log.info("{}", roles);

        for(GrantedAuthority g : roles) {
           if(g.getAuthority().equals("ROLE_ADMIN_EMP")) {
               System.out.println(g.getAuthority());
               response.sendRedirect("/employee");

           } else if (g.getAuthority().equals("ROLE_ADMIN_LO")) {
               response.sendRedirect("/receiveRecords");

           } else if(g.getAuthority().equals("ROLE_ADMIN_PRO")) {
               response.sendRedirect("/production");
           }
        }
    }


}
