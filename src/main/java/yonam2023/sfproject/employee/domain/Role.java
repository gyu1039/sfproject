package yonam2023.sfproject.employee.domain;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_ADMIN_EMP("ROLE_ADMIN_EMP"), ROLE_ADMIN_LO("ROLE_ADMIN_LO"), ROLE_ADMIN_PRO("ROLE_ADMIN_PRO"),
    ROLE_USER("ROLE_USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

}
