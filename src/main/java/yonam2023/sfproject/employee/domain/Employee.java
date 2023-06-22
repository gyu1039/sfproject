package yonam2023.sfproject.employee.domain;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class Employee implements UserDetails, Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    private DepartmentType department;
    private Role role;
    private boolean isCheckedIn;
    private LocalDateTime checkInTime;

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    private String token;

    public void setToken(String token) {
        this.token = token;
    }
    public void deleteToken(){
        this.token = null;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }
    public void setCheckedIn(boolean checkedIn) {
        this.isCheckedIn = checkedIn;
    }

    public void employeeUpdate(EmployeeTO dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.department = dto.getDepartment();
        this.role = dto.getRole();
        this.isCheckedIn = dto.isCheckedIn();
        this.checkInTime = dto.getCheckInTime();
    }

    @Builder
    public Employee(Long id, String name, String password, String phoneNumber, DepartmentType department, Role role, boolean isCheckedIn, LocalDateTime checkInTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.role = role;
        this.isCheckedIn = isCheckedIn;
        this.checkInTime = checkInTime;
    }





    @Transient
    private Collection<GrantedAuthority> authorities;


    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        for(GrantedAuthority g : this.authorities) {
            collection.add(g);
        }
        return collection;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department=" + department +
                ", role=" + role +
                ", isCheckedIn=" + isCheckedIn +
                ", checkInTime=" + checkInTime +
                '}';
    }
}

