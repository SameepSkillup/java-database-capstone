package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    // 1. Save a prescription
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();
        try {
            // Check if prescription already exists for this appointment
            List<Prescription> existingPrescriptions =
                    prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());

            if (!existingPrescriptions.isEmpty()) {
                response.put("message", "Prescription already exists for this appointment");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("message", "Error saving prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Get prescription by appointment ID
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                response.put("message", "No prescription found for this appointment");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("prescriptions", prescriptions);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error fetching prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
