package com.example.kds_attendance_service_backend.exception;

import com.example.kds_attendance_service_backend.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto("NOT_FOUND", ex.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto("BAD_REQUEST", ex.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorized(UnauthorizedException ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto("FORBIDDEN", ex.getMessage(), LocalDateTime.now()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto(
                        "FORBIDDEN",
                        "You are not allowed to access this resource",
                        LocalDateTime.now()
                ),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthError(AuthenticationCredentialsNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto(
                        "UNAUTHORIZED",
                        "Authentication required",
                        LocalDateTime.now()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto("INTERNAL_ERROR", "Something went wrong", LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
