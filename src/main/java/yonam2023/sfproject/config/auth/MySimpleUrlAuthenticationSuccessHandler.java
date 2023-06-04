package yonam2023.sfproject.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("{}", "onAuthenticationSuccess method");
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        if(response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {

        Map<String, String> rolesTargetUrlMap = new HashMap<>();
        rolesTargetUrlMap.put("ROLE_ADMIN_EMP", "/employee");
        rolesTargetUrlMap.put("ROLE_ADMIN_LO", "/receiveRecords");
        rolesTargetUrlMap.put("ROLE_ADMIN_PRO", "/production");

        List<GrantedAuthority> roles = new ArrayList<>();
        authentication.getAuthorities().forEach(r -> roles.add(r));

        for(GrantedAuthority g : roles){
            String auth = g.getAuthority();
            if(rolesTargetUrlMap.containsKey(auth)) {
                return rolesTargetUrlMap.get(auth);
            }

        }

        return "/index";
    }
}
