package yonam2023.sfproject.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import yonam2023.sfproject.employee.domain.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<GrantedAuthority> roles = new ArrayList<>();
        authentication.getAuthorities().forEach(auth -> roles.add(auth));

        for(GrantedAuthority g : roles) {
            System.out.println(g.getAuthority());
           if(g.getAuthority().equals("ROLE_ADMIN_EMP")) {
               response.sendRedirect("/employee");

           } else if (g.getAuthority().equals("ROLE_ADMIN_LO")) {
               response.sendRedirect("/receiveRecords");

           } else if(g.getAuthority().equals("ROLE_ADMIN_PRO")) {
               response.sendRedirect("/production");
           }

           return;
        }
    }
}
