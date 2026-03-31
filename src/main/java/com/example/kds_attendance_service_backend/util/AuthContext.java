package com.example.kds_attendance_service_backend.util;

import com.example.kds_attendance_service_backend.model.Employee;

public class AuthContext {

    private static final ThreadLocal<Employee> currentUser  = new ThreadLocal<>();

    public static void set(Employee user){
        currentUser.set(user);
    }

    public static Employee getUser(){
        return currentUser.get();
    }

    public static void clear(){
        currentUser.remove();
    }
}
