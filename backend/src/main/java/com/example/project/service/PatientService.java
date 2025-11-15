package com.example.project.service;

import com.example.project.model.Patient;
import com.example.project.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repo;

    public PatientService(PatientRepository repo) { this.repo = repo; }

    public Patient create(Patient p) { return repo.save(p); }
    public Patient update(String id, Patient p) {
        p.setId(id);
        return repo.save(p);
    }
    public Patient getById(String id) { return repo.findById(id).orElse(null); }
    public List<Patient> getAll() { return repo.findAll(); }
    public void delete(String id) { repo.deleteById(id); }
}
