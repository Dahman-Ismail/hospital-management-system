package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/patients")
public class PatientController extends BaseController {

    @Autowired
    private PatientRepository patientRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Get all patients
    @GetMapping
    public ModelAndView getAllPatients() {
        return view("patients/list", attrs(
                "patients", patientRepository.findAll()));
    }

    // Show create patient form
    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        return view("patients/create", attrs(
                "patient", new Patient()));
    }

    // Create new patient with validation
    @PostMapping("/create")
    public ModelAndView createPatient(@ModelAttribute Patient patient) {
        String error = validatePatient(patient, null);
        if (error != null) {
            // Return form with error and old values
            return view("patients/create", attrs(
                    "error", error,
                    "patient", patient));
        }

        patientRepository.save(patient);
        return redirect("/patients");
    }

    // Show edit patient form
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        return view("patients/edit", attrs(
                "patient", patient));
    }

    // Update patient with validation
    @PostMapping("/edit/{id}")
    public ModelAndView updatePatient(@PathVariable String id, @ModelAttribute Patient patient) {
        String error = validatePatient(patient, id);
        if (error != null) {
            patient.setId(id); // preserve ID
            return view("patients/edit", attrs(
                    "error", error,
                    "patient", patient));
        }

        patient.setId(id);
        patientRepository.save(patient);
        return redirect("/patients");
    }

    // Delete patient
    @GetMapping("/delete/{id}")
    public ModelAndView deletePatient(@PathVariable String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        patientRepository.delete(patient);
        return redirect("/patients");
    }

    // Search patients
    @GetMapping("/search")
    public ModelAndView searchPatients(@RequestParam String name) {
        List<Patient> patients = patientRepository.findByNameContainingIgnoreCase(name);
        return view("patients/list", attrs(
                "patients", patients,
                "searchTerm", name));
    }

    // ------------------ Validation Method ------------------
    private String validatePatient(Patient patient, String currentId) {
        if (patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
            return "Patient ID is required.";
        }

        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            return "Name is required.";
        }

        if (patient.getDob() == null) {
            return "Date of birth is required.";
        }

        if (patient.getGender() == null || patient.getGender().trim().isEmpty()) {
            return "Gender is required.";
        }

        if (patient.getPhone() == null || patient.getPhone().trim().isEmpty()) {
            return "Phone number is required.";
        }

        if (patient.getEmail() != null && !patient.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(patient.getEmail()).matches()) {
                return "Invalid email format.";
            }
        }

        // Check unique patientId
        Patient existing = patientRepository.findByPatientId(patient.getPatientId());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            return "Patient ID already exists.";
        }

        return null; // No errors
    }
}
