package com.example.demo.repository;

import com.example.demo.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDoctorId(String doctorId);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByPatientId(String patientId);
    List<Appointment> findByDoctorIdAndDate(String doctorId, LocalDate date);
    Appointment findByAppointmentId(String appointmentId);
    
    @Query("{ 'appointmentId': ?0 }")
    Appointment findByAppointmentIdQuery(String appointmentId);
    
    @Query("{ 'doctorId': ?0, 'date': ?1, 'status': { $ne: 'Annulé' } }")
    List<Appointment> findActiveAppointmentsByDoctorAndDate(String doctorId, LocalDate date);
}