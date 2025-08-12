package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Appointment {

    // 1. Auto-incremented primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Doctor assigned - ManyToOne relationship, required (NotNull)
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull
    private Doctor doctor;

    // 3. Patient assigned - ManyToOne relationship, required (NotNull)
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull
    private Patient patient;

    // 4. Appointment time - must be in the future
    @NotNull
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    // 5. Status of appointment: 0 = Scheduled, 1 = Completed; required
    @NotNull
    private int status;

    // No-args constructor (required by JPA)
    public Appointment() {}

    // Helper methods - not persisted in DB
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);  // 1 hour after start time
    }

    @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
