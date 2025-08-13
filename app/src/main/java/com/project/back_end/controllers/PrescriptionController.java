package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final Service service;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
                                   AppointmentService appointmentService,
                                   Service service) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 1. Save Prescription
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription) {

        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid token or unauthorized access"));
        }

        // Update appointment status after prescription is issued
        appointmentService.updateAppointmentStatus(prescription.getAppointmentId(), "Prescription Added");

        return prescriptionService.savePrescription(prescription);
    }

    // 2. Get Prescription by Appointment ID
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable int appointmentId,
            @PathVariable String token) {

        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid token or unauthorized access"));
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}
