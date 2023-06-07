package yonam2023.sfproject.config.security;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

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
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();
    private TargetUrlFactory targetUrlFactory = new RoleBasedTargetUrlFactory();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("{}", "onAuthenticationSuccess method");
        clearAuthenticationAttributes(request);
        handle(request, response, authentication);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication, requestCache.getRequest(request, response));

        if(response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }


        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication, SavedRequest savedRequest) {

        if(savedRequest != null) {
            return savedRequest.getRedirectUrl();
        }

        return targetUrlFactory.createTargetUrl(authentication);
    }

}

interface TargetUrlFactory {
    String createTargetUrl(Authentication authentication);
}

@Log4j2
class RoleBasedTargetUrlFactory implements TargetUrlFactory {
    private Map<String, String> rolesTargetUrlMap = new HashMap<>();

    public RoleBasedTargetUrlFactory() {
        rolesTargetUrlMap.put("ROLE_ADMIN_EMP", "/employee");
        rolesTargetUrlMap.put("ROLE_ADMIN_LO", "/storedItems");
        rolesTargetUrlMap.put("ROLE_ADMIN_PRO", "/production");
        rolesTargetUrlMap.put("ROLE_ADMIN", "/index");
    }

    @Override
    public String createTargetUrl(Authentication authentication) {
        List<GrantedAuthority> roles = new ArrayList<>(authentication.getAuthorities());
        log.info("crateTartUrl 메서드 실행");
        for (GrantedAuthority authority : roles) {
            log.info(authority.getAuthority());
            String role = authority.getAuthority();
            if (rolesTargetUrlMap.containsKey(role)) {
                return rolesTargetUrlMap.get(role);
            }
        }

        return "/";
    }
}