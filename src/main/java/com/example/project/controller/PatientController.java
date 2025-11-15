package com.example.project.controller;

import com.example.project.model.Patient;
import com.example.project.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient p) {
        return ResponseEntity.ok(service.create(p));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> all() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> get(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable String id, @RequestBody Patient p) { return ResponseEntity.ok(service.update(id,p)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
