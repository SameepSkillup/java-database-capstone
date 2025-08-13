package com.project.back_end.services;

import com.project.back_end.model.Doctor;
import com.project.back_end.model.Appointment;
import com.project.back_end.model.Login;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.security.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        List<String> allSlots = Arrays.asList("09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00");
        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorIdAndDate(doctorId, date);

        Set<String> bookedSlots = bookedAppointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1; // Doctor already exists
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) {
            return -1; // Not found
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        if (!doctorRepository.existsById(id)) {
            return -1; // Not found
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        Map<String, String> response = new HashMap<>();
        if (doctor != null && doctor.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(doctor.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid email or password");
        return ResponseEntity.status(401).body(response);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filtered);
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doc -> doc.getAvailableTimes().stream()
                        .anyMatch(time -> amOrPm.equalsIgnoreCase("AM") ? time.endsWith("AM") : time.endsWith("PM")))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filtered);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        List<Doctor> doctors = doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filtered);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filtered);
    }
}
