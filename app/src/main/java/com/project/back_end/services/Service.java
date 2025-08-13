package com.project.back_end.services;

import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 1. Validate Token
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        response.put("message", "Token valid");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 2. Validate Admin Login
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Admin> adminOpt = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (adminOpt.isEmpty() || !adminOpt.get().getPassword().equals(receivedAdmin.getPassword())) {
                response.put("message", "Invalid username or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            String token = tokenService.generateToken(receivedAdmin.getUsername());
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error during admin validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. Filter Doctor
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors;

        if (name != null && specialty != null && time != null) {
            doctors = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        } else if (name != null && specialty != null) {
            doctors = doctorService.filterDoctorsByNameAndSpecialty(name, specialty);
        } else if (specialty != null && time != null) {
            doctors = doctorService.filterDoctorsBySpecialtyAndTime(specialty, time);
        } else if (name != null && time != null) {
            doctors = doctorService.filterDoctorsByNameAndTime(name, time);
        } else if (name != null) {
            doctors = doctorService.filterDoctorsByName(name);
        } else if (specialty != null) {
            doctors = doctorService.filterDoctorsBySpecialty(specialty);
        } else if (time != null) {
            doctors = doctorService.filterDoctorsByTime(time);
        } else {
            doctors = doctorService.getAllDoctors();
        }

        response.put("doctors", doctors);
        return response;
    }

    // 4. Validate Appointment
    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctorId());
        if (doctorOpt.isEmpty()) {
            return -1;
        }

        List<String> availableTimes = doctorService.getDoctorAvailability(appointment.getDoctorId(), appointment.getDate());
        if (availableTimes.contains(appointment.getTime())) {
            return 1;
        }
        return 0;
    }

    // 5. Validate Patient Registration
    public boolean validatePatient(Patient patient) {
        Optional<Patient> existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing.isEmpty();
    }

    // 6. Validate Patient Login
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Patient> patientOpt = patientRepository.findByEmail(login.getEmail());
            if (patientOpt.isEmpty() || !patientOpt.get().getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid email or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            String token = tokenService.generateToken(login.getEmail());
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error during patient login");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 7. Filter Patient Appointments
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.getEmailFromToken(token);
            List<Appointment> appointments;

            if (condition != null && name != null) {
                appointments = patientService.filterByDoctorAndCondition(email, name, condition);
            } else if (condition != null) {
                appointments = patientService.filterByCondition(email, condition);
            } else if (name != null) {
                appointments = patientService.filterByDoctor(email, name);
            } else {
                appointments = patientService.getAllAppointmentsForPatient(email);
            }

            response.put("appointments", appointments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error filtering patient appointments");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
