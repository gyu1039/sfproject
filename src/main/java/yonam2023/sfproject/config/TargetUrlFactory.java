package yonam2023.sfproject.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TargetUrlFactory {
    String createTargetUrl(Collection<? extends GrantedAuthority> authorities);
}
