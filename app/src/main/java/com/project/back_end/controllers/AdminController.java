package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final AdminService adminService;

    // 2. Constructor Injection for Service Dependency
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Handles admin login requests.
     * @param admin Admin object containing login credentials (username/password).
     * @return ResponseEntity containing a map with login result or JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return adminService.validateAdmin(admin);
    }
}
