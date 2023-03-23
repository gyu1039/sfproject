package yonam2023.sfproject.employee.domain;

public enum DepartmentType {

    PERSONNEL("인사"), PRODUCTION("생산"), LOGISTICS("물류");

    private final String value;
    DepartmentType(String value) {
        this.value = value;
    }
}
