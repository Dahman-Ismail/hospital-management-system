package com.example.project.service;

import com.example.project.model.Doctor;
import com.example.project.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository repo;
    public DoctorService(DoctorRepository repo) { this.repo = repo; }
    public Doctor create(Doctor d) { return repo.save(d); }
    public Doctor update(String id, Doctor d) { d.setId(id); return repo.save(d); }
    public Doctor getById(String id) { return repo.findById(id).orElse(null); }
    public List<Doctor> getAll() { return repo.findAll(); }
    public void delete(String id) { repo.deleteById(id); }
}

