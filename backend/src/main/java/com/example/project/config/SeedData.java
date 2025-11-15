package com.example.project.config;


import com.example.project.model.Doctor;
import com.example.project.model.Patient;
import com.example.project.repository.DoctorRepository;
import com.example.project.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner seed(PatientRepository patientRepo, DoctorRepository doctorRepo) {
        return args -> {
            if (patientRepo.count() == 0) {
                Patient p = Patient.builder()
                        .patientId("P2025-0001")
                        .firstName("Ismail")
                        .lastName("Dahman")
                        .birthDate(LocalDate.of(1996,4,12))
                        .phone("+212600000000")
                        .email("ismail@example.com")
                        .build();
                patientRepo.save(p);
            }
            if (doctorRepo.count() == 0) {
                Doctor d = Doctor.builder()
                        .doctorId("D2025-001")
                        .firstName("Sofia")
                        .lastName("Bennani")
                        .specialty("Cardiologie")
                        .workSchedule(List.of())
                        .build();
                doctorRepo.save(d);
            }
        };
    }
}
