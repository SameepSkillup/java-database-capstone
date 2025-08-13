package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    @Autowired
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 1. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, String>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable int doctorId,
            @PathVariable String date,
            @PathVariable String token) {
        return doctorService.getDoctorAvailability(user, doctorId, date, token);
    }

    // 2. Get List of Doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return doctorService.getDoctors();
    }

    // 3. Add New Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {
        return doctorService.saveDoctor(doctor, token);
    }

    // 4. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 5. Update Doctor Details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {
        return doctorService.updateDoctor(doctor, token);
    }

    // 6. Delete Doctor
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable int id,
            @PathVariable String token) {
        return doctorService.deleteDoctor(id, token);
    }

    // 7. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        return service.filterDoctor(name, time, speciality);
    }
}
