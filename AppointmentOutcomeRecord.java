

import java.util.HashMap;

public class AppointmentOutcomeRecord {
    private String patientID;
    private String doctorID;
    private String appointmentID;
    private String date; // Changed from Date to String
    private String consultationNotes;
    private ServiceType serviceType;
    private StatusOfPrescription statusOfPrescription;
    private HashMap<String, Integer> medications;

    // Enums for service type and prescription status
    public enum ServiceType {
        CONSULTATION,
        XRAY,
        BLOOD_TEST
    }
    public enum StatusOfPrescription { PENDING, DISPENSED }

    // Constructor
    public AppointmentOutcomeRecord(String patientID, String doctorID, String appointmentID, String date, String consultationNotes, ServiceType serviceType) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.date = date;
        this.consultationNotes = consultationNotes;
        this.serviceType = serviceType;
        this.statusOfPrescription = StatusOfPrescription.PENDING;
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

    public String getDate() { // Changed to return String
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

    // Convert medications HashMap to a CSV string format
    public String medicationsToCsv() {
        StringBuilder medicationsCsv = new StringBuilder();
        for (String key : medications.keySet()) {
            medicationsCsv.append(key).append(":").append(medications.get(key)).append(";");
        }
        if (medicationsCsv.length() > 0) {
            medicationsCsv.setLength(medicationsCsv.length() - 1); // Remove trailing semicolon
        }
        return medicationsCsv.toString();
    }

    // Method to parse medications from CSV format
    public static HashMap<String, Integer> parseMedicationsFromCsv(String csvData) {
        HashMap<String, Integer> medications = new HashMap<>();
        if (csvData != null && !csvData.trim().isEmpty()) {
            String[] items = csvData.split(";");
            for (String item : items) {
                String[] pair = item.split(":");
                if (pair.length == 2) {
                    String medicationName = pair[0];
                    int quantity = Integer.parseInt(pair[1]);
                    medications.put(medicationName, quantity);
                }
            }
        }
        return medications;
    }
}
