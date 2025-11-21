package com.example.demo.controller;

import com.example.demo.model.Doctor;
import com.example.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController extends BaseController {

    @Autowired
    private DoctorRepository doctorRepository;

    // Get all doctors
    @GetMapping
    public ModelAndView getAllDoctors() {
        return view("doctors/list", attrs(
                "doctors", doctorRepository.findAll()));
    }

    // Show create doctor form
    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        return view("doctors/create", attrs(
                "doctor", new Doctor()));
    }

    // Create new doctor with validation
    @PostMapping("/create")
    public ModelAndView createDoctor(@ModelAttribute Doctor doctor) {
        String validationError = validateDoctor(doctor, null);
        if (validationError != null) {
            return view("doctors/create", attrs(
                    "doctor", doctor,
                    "error", validationError));
        }

        doctorRepository.save(doctor);
        return redirect("/doctors");
    }

    // Show edit doctor form
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));
        return view("doctors/edit", attrs(
                "doctor", doctor));
    }

    // Update doctor with validation
    @PostMapping("/edit/{id}")
    public ModelAndView updateDoctor(@PathVariable String id, @ModelAttribute Doctor doctor) {
        String validationError = validateDoctor(doctor, id);
        if (validationError != null) {
            doctor.setId(id);
            return view("doctors/edit", attrs(
                    "doctor", doctor,
                    "error", validationError));
        }

        doctor.setId(id);
        doctorRepository.save(doctor);
        return redirect("/doctors");
    }

    // Delete doctor
    @GetMapping("/delete/{id}")
    public ModelAndView deleteDoctor(@PathVariable String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));
        doctorRepository.delete(doctor);
        return redirect("/doctors");
    }

    // Search doctors by name
    @GetMapping("/search/name")
    public ModelAndView searchDoctorsByName(@RequestParam String name) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCase(name);
        return view("doctors/list", attrs(
                "doctors", doctors,
                "searchTerm", name));
    }

    // Search doctors by specialization
    @GetMapping("/search/specialization")
    public ModelAndView searchDoctorsBySpecialization(@RequestParam String specialization) {
        List<Doctor> doctors = doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        return view("doctors/list", attrs(
                "doctors", doctors,
                "searchTerm", specialization));
    }

    // --- Validation Helper ---
    private String validateDoctor(Doctor doctor, String currentDoctorId) {
        // Required fields
        if (!StringUtils.hasText(doctor.getName()))
            return "Name is required";
        if (!StringUtils.hasText(doctor.getSpecialization()))
            return "Specialization is required";
        if (!StringUtils.hasText(doctor.getEmail()))
            return "Email is required";
        if (!StringUtils.hasText(doctor.getPhone()))
            return "Phone is required";

        // Email format
        if (!doctor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            return "Invalid email format";

        // Phone format (digits only, 7-15 characters)
        if (!doctor.getPhone().matches("^\\d{7,15}$"))
            return "Invalid phone number";

        // Unique name check
        Doctor existingByName = doctorRepository.findByNameContainingIgnoreCase(doctor.getName())
                .stream()
                .filter(d -> currentDoctorId == null || !d.getId().equals(currentDoctorId))
                .findFirst()
                .orElse(null);
        if (existingByName != null)
            return "A doctor with this name already exists";

        // Unique doctorId check if provided
        if (doctor.getDoctorId() != null && !doctor.getDoctorId().isEmpty()) {
            Doctor existingById = doctorRepository.findByDoctorId(doctor.getDoctorId());
            if (existingById != null && (currentDoctorId == null || !existingById.getId().equals(currentDoctorId))) {
                return "A doctor with this doctorId already exists";
            }
        }

        // At least 1 working day
        if (doctor.getWorkingDays() == null || doctor.getWorkingDays().isEmpty()) {
            return "Select at least one working day";
        }

        return null; // All good
    }
}
