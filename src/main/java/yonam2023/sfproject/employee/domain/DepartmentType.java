package yonam2023.sfproject.employee.domain;


import lombok.Getter;

@Getter
public enum DepartmentType {

    PERSONNEL("인사"), PRODUCTION("생산"), LOGISTICS("물류");

    private final String description;
    DepartmentType(String description) {
        this.description = description;
    }
}
