package com.example.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;

    private String appointmentId;

    private String patientId;
    private String doctorId;

    private Instant date; // start datetime (UTC recommended)
    private Integer durationMinutes;

    private String status; // SCHEDULED, CANCELLED, COMPLETED, NOSHOW

    private Instant createdAt;
    private Instant updatedAt;

    // Slot start normalized for unique index (e.g., truncated to minute or slot boundary)
    @Indexed(unique = true, name = "doctor_slot_unique") // NOTE: ensure index creation in config
    private String doctorSlotKey;

    // Optional snapshot
    private PatientSnapshot patientSnapshot;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientSnapshot {
        private String firstName;
        private String lastName;
        private String phone;
    }
}
