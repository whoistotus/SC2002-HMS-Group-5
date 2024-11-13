package SC2002_Assignment;

import java.util.List;
import java.util.Date;
import java.util.HashMap;


public class AppointmentOutcomeRecord {
    private String patientID;
    private String doctorID;
    private String appointmentID;
    private Date date;
    private String consultationNotes;
    private ServiceType serviceType;
    private StatusOfPrescription statusOfPrescription;
    private HashMap<String, Integer> medications; // Medication name as key, quantity as value

    // Enums for service type and prescription status
    public enum ServiceType {
        CONSULTATION,
        XRAY,
        BLOOD_TEST
    }
    public enum StatusOfPrescription { PENDING, DISPENSED }

    // Constructor
    public AppointmentOutcomeRecord(String patientID, String doctorID, String appointmentID, Date date, String consultationNotes, ServiceType serviceType) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.date = date;
        this.consultationNotes = consultationNotes;
        this.serviceType = serviceType;
        this.medications = new HashMap<>();
    }
    // Getters and Setters
    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public Date getDate() {
        return date;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public StatusOfPrescription getStatusOfPrescription() {
        return statusOfPrescription;
    }

    public void setStatusOfPrescription(StatusOfPrescription statusOfPrescription) {
        this.statusOfPrescription = statusOfPrescription;
    }

    public HashMap<String, Integer> getMedications() {
        return medications;
    }

    public void setMedications(HashMap<String, Integer> medications) {
        this.medications = medications;
    }

    // Method to add a medication to the record
    public void addMedication(String medicationName, int quantity) {
        medications.put(medicationName, quantity);
    }

    // Method to remove a medication from the record
    public void removeMedication(String medicationName) {
        medications.remove(medicationName);
    }
}
