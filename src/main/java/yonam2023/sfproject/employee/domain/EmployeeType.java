package yonam2023.sfproject.employee.domain;

import lombok.Getter;

@Getter
public enum EmployeeType {

    MANAGER("관리자"), NORMAL("일반 직원");

    private final String value;

    EmployeeType(String value) {
        this.value = value;
    }

}
