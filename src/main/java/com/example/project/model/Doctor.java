package com.example.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String doctorId;
    private String firstName;
    private String lastName;
    private String specialty;
    private List<WorkSchedule> workSchedule;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkSchedule {
        private String day; // Mon,Tue,...
        private String from; // "09:00"
        private String to;   // "12:00"
    }
}
