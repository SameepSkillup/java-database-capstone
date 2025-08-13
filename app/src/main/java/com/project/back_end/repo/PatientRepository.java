package com.project.back_end.repo;

import com.project.back_end.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patient by email
    Patient findByEmail(String email);

    // Find patient by email OR phone
    Patient findByEmailOrPhone(String email, String phone);
}
