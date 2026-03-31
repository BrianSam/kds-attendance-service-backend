package com.example.kds_attendance_service_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableMethodSecurity
public class KdsAttendanceServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdsAttendanceServiceBackendApplication.class, args);
	}


}
