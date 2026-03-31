package com.example.kds_attendance_service_backend.security;
import com.example.kds_attendance_service_backend.model.Employee;
import com.example.kds_attendance_service_backend.model.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
public class CustomUserDetails implements UserDetails {
    private final Employee employee;

    public CustomUserDetails(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + employee.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        if(employee.getStatus().equals(Status.INACTIVE)){
            return false;
        }
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(employee.getStatus().equals(Status.INACTIVE)){
            return false;
        }
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
}
