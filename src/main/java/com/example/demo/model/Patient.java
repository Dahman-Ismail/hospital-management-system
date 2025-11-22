package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    private String patientId;
    private String name;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String email;
    private String address;

    private transient long totalAppointments;
    private transient long plannedAppointments;
    private transient long completedAppointments;
    private transient long cancelledAppointments;

    // Constructors
    public Patient() {
    }

    public Patient(String patientId, String name, LocalDate dob, String gender, String phone, String email,
            String address) {
        this.patientId = patientId;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Total appointments
    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    // Planned appointments (Planifié)
    public long getPlannedAppointments() {
        return plannedAppointments;
    }

    public void setPlannedAppointments(long plannedAppointments) {
        this.plannedAppointments = plannedAppointments;
    }

    // Completed appointments (Terminé)
    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(long completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    // Cancelled appointments (Annulé)
    public long getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(long cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }
}