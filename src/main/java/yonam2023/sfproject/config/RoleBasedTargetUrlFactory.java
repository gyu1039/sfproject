package yonam2023.sfproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class RoleBasedTargetUrlFactory implements TargetUrlFactory {

    private Map<String, String> rolesTargetUrlMap = new HashMap<>();

    public RoleBasedTargetUrlFactory() {
        rolesTargetUrlMap.put("ROLE_ADMIN_EMP", "/employee");
        rolesTargetUrlMap.put("ROLE_ADMIN_LO", "/storedItems");
        rolesTargetUrlMap.put("ROLE_ADMIN_PRO", "/production");
        rolesTargetUrlMap.put("ROLE_ADMIN", "/index");
        rolesTargetUrlMap.put("ROLE_EMPLOYEE", "/test");
    }


    @Override
    public String createTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        List<GrantedAuthority> roles = new ArrayList<>(authorities);
        log.info("crateTartUrl 메서드 실행");
        for (GrantedAuthority authority : roles) {
            String role = authority.getAuthority();
            if (rolesTargetUrlMap.containsKey(role)) {
                return rolesTargetUrlMap.get(role);
            }
        }

        return "redirect:/loginForm";
    }
}
