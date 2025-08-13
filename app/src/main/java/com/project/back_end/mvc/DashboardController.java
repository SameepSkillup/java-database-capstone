package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private Service service;

    // Admin Dashboard
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        Map<String, Object> validationResult = service.validateToken(token, "admin");
        if (validationResult.isEmpty()) {
            // Token is valid → load admin dashboard
            return "admin/adminDashboard";
        }
        // Token invalid → redirect to login page
        return "redirect:/";
    }

    // Doctor Dashboard
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        Map<String, Object> validationResult = service.validateToken(token, "doctor");
        if (validationResult.isEmpty()) {
            // Token is valid → load doctor dashboard
            return "doctor/doctorDashboard";
        }
        // Token invalid → redirect to login page
        return "redirect:/";
    }
}
