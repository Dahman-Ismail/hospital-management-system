package com.example.project.controller;

import com.example.project.model.Appointment;
import com.example.project.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment a = service.schedule(appointment);
        return ResponseEntity.ok(a);
    }

    @GetMapping("/doctor/{doctorId}/day")
    public ResponseEntity<List<Appointment>> getDoctorDay(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return ResponseEntity.ok(service.findByDoctorAndDay(doctorId, day));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(service.findByPatient(patientId));
    }
}
