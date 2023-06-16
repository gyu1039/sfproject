package yonam2023.sfproject.employee.domain;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class Employee implements UserDetails {

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
    private boolean loggedIn;

    private String token;

    public void setToken(String token) {
        this.token = token;
    }
    public void deleteToken(){
        this.token = null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void employeeUpdate(EmployeeTO dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.department = dto.getDepartment();
        this.role = dto.getRole();
    }

    @Builder
    public Employee(DepartmentType department, Role role, String name, String phoneNumber, String password) {
        this.department = department;
        this.role = role;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department=" + department +
                '}';
    }
}

