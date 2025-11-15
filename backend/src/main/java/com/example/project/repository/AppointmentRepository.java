package com.example.project.repository;

import com.example.project.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDoctorIdAndDateBetweenAndStatus(String doctorId, Instant start, Instant end, String status);
    List<Appointment> findByPatientIdOrderByDateDesc(String patientId);
}
