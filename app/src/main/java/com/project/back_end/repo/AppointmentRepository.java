package com.project.back_end.repo;

import com.project.back_end.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Retrieve appointments for a doctor within a given time range (with doctor + availability info)
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.doctor d " +
           "LEFT JOIN FETCH d.availability " +
           "WHERE d.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId,
                                                               LocalDateTime start,
                                                               LocalDateTime end);

    // Filter appointments by doctor ID, partial patient name (case-insensitive), and time range
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.patient p " +
           "LEFT JOIN FETCH a.doctor d " +
           "WHERE d.id = :doctorId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%')) " +
           "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId,
                                                                                                 String patientName,
                                                                                                 LocalDateTime start,
                                                                                                 LocalDateTime end);

    // Delete all appointments related to a specific doctor
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    // Find all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // Retrieve appointments for a patient by status, ordered by appointment time
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Search appointments by partial doctor name and patient ID
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN a.doctor d " +
           "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
           "AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    // Filter appointments by doctor name, patient ID, and status
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN a.doctor d " +
           "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
           "AND a.patient.id = :patientId AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName,
                                                              Long patientId,
                                                              int status);
}
