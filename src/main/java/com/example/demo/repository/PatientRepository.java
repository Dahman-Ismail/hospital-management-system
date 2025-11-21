package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface PatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findByNameContainingIgnoreCase(String name);
    Patient findByPatientId(String patientId);
    
    @Query("{ 'patientId': ?0 }")
    Patient findByPatientIdQuery(String patientId);
}