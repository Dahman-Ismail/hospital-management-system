package com.example.project.service;

import com.example.project.exception.SlotUnavailableException;
import com.example.project.model.Appointment;
import com.example.project.model.Patient;
import com.example.project.repository.AppointmentRepository;
import com.example.project.repository.PatientRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    // Normalise slot: truncate to minute (or to slot size e.g., 30 minutes)
    private String buildDoctorSlotKey(String doctorId, Instant date, int slotMinutes) {
        // Convert Instant to UTC LocalDateTime
        LocalDateTime utcDateTime = LocalDateTime.ofInstant(date, ZoneOffset.UTC);
        // normalize to slot: floor minute to nearest slotMinutes
        int minutes = utcDateTime.getMinute();
        int slotStartMinute = (minutes / slotMinutes) * slotMinutes;
        LocalDateTime slotStart = utcDateTime.withMinute(slotStartMinute).withSecond(0).withNano(0);
        return doctorId + "|" + slotStart.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
    }

    public Appointment schedule(Appointment appointment) {
        // fill createdAt/updatedAt
        Instant now = Instant.now();
        appointment.setCreatedAt(now);
        appointment.setUpdatedAt(now);
        if (appointment.getDurationMinutes() == null) appointment.setDurationMinutes(30);
        // build slot key (e.g., 30-minute slot)
        String slotKey = buildDoctorSlotKey(appointment.getDoctorId(), appointment.getDate(), appointment.getDurationMinutes());
        appointment.setDoctorSlotKey(slotKey);

        // add patient snapshot if patient exists
        if (appointment.getPatientId() != null) {
            Patient p = patientRepository.findById(appointment.getPatientId()).orElse(null);
            if (p != null) {
                appointment.setPatientSnapshot(new Appointment.PatientSnapshot(p.getFirstName(), p.getLastName(), p.getPhone()));
            }
        }

        try {
            appointment.setStatus(appointment.getStatus() == null ? "SCHEDULED" : appointment.getStatus());
            return appointmentRepository.save(appointment);
        } catch (DuplicateKeyException ex) {
            // index unique violation -> slot unavailable
            throw new SlotUnavailableException("Slot already taken for doctor " + appointment.getDoctorId() + " at slot " + slotKey);
        }
    }

    public List<Appointment> findByDoctorAndDay(String doctorId, LocalDate day) {
        Instant start = day.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = day.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        return appointmentRepository.findByDoctorIdAndDateBetweenAndStatus(doctorId, start, end, "SCHEDULED");
    }

    public List<Appointment> findByPatient(String patientId) {
        return appointmentRepository.findByPatientIdOrderByDateDesc(patientId);
    }
}
