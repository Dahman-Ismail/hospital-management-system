package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.Doctor;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/appointments")
public class AppointmentController extends BaseController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Get all appointments
    @GetMapping
    public ModelAndView getAllAppointments() {
        return view("appointments/list", attrs(
                "appointments", appointmentRepository.findAll(),
                "patients", patientRepository.findAll(),
                "doctors", doctorRepository.findAll()));
    }

    // Show create appointment form
    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        return view("appointments/create", attrs(
                "appointment", new Appointment(),
                "patients", patientRepository.findAll(),
                "doctors", doctorRepository.findAll()));
    }

    // Create new appointment
    @PostMapping("/create")
    public ModelAndView createAppointment(@ModelAttribute Appointment appointment) {

        // Check if timeslot is free
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndDate(
                appointment.getDoctorId(), appointment.getDate());

        for (Appointment existing : existingAppointments) {
            if (existing.getTime().equals(appointment.getTime()) && !"Annulé".equals(existing.getStatus())) {
                // Slot booked → show form again with error
                return view("appointments/create", attrs(
                        "error", "This time slot is already booked",
                        "appointment", appointment,
                        "patients", patientRepository.findAll(),
                        "doctors", doctorRepository.findAll()));
            }
        }

        appointment.setAppointmentId(UUID.randomUUID().toString());

        // Default status
        if (appointment.getStatus() == null) {
            appointment.setStatus("Planifié");
        }

        appointmentRepository.save(appointment);
        return redirect("/appointments");
    }

    // Show edit appointment form
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id: " + id));

        return view("appointments/edit", attrs(
                "appointment", appointment,
                "patients", patientRepository.findAll(),
                "doctors", doctorRepository.findAll()));
    }

    // Update appointment
    @PostMapping("/edit/{id}")
    public ModelAndView updateAppointment(@PathVariable String id, @ModelAttribute Appointment appointment) {
        appointment.setId(id);
        appointmentRepository.save(appointment);
        return redirect("/appointments");
    }

    // Cancel appointment
    @GetMapping("/cancel/{id}")
    public ModelAndView cancelAppointment(@PathVariable String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id: " + id));

        appointment.setStatus("Annulé");
        appointmentRepository.save(appointment);
        return redirect("/appointments");
    }

    // Complete appointment
    @GetMapping("/complete/{id}")
    public ModelAndView completeAppointment(@PathVariable String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id: " + id));

        appointment.setStatus("Terminé");
        appointmentRepository.save(appointment);
        return redirect("/appointments");
    }

    // Delete appointment
    @GetMapping("/delete/{id}")
    public ModelAndView deleteAppointment(@PathVariable String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id: " + id));

        appointmentRepository.delete(appointment);
        return redirect("/appointments");
    }

    // Show appointments by doctor
    @GetMapping("/doctor/{doctorId}")
    public ModelAndView getAppointmentsByDoctor(@PathVariable String doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);

        return view("appointments/list", attrs(
                "appointments", appointments,
                "doctorName", doctor != null ? doctor.getName() : "Unknown Doctor"));
    }

    // Show appointments by date
    @GetMapping("/date")
    public ModelAndView getAppointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return view("appointments/list", attrs(
                "appointments", appointmentRepository.findByDate(date),
                "selectedDate", date));
    }
}
