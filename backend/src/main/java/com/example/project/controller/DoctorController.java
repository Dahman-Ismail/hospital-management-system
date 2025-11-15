package com.example.project.controller;

import com.example.project.model.Doctor;
import com.example.project.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor d) { return ResponseEntity.ok(service.create(d)); }

    @GetMapping
    public ResponseEntity<List<Doctor>> all() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> get(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable String id, @RequestBody Doctor d) { return ResponseEntity.ok(service.update(id,d)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
