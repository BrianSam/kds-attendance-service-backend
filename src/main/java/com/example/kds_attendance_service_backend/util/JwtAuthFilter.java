package com.example.kds_attendance_service_backend.util;

import com.example.kds_attendance_service_backend.repository.EmployeeRepository;
import com.example.kds_attendance_service_backend.security.CustomUserDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String authHeader = req.getHeader("Authorization");

        if(authHeader!=null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);
            if(jwtUtil.isTokenValid(token)){
                Long userID = jwtUtil.extractUserId(token);
                employeeRepository.findById(userID).ifPresent(
                        user->{
                            CustomUserDetails userDetails = new CustomUserDetails(user);

                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                );
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
