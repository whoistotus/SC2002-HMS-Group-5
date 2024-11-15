package SC2002_Assignment;

import java.util.Map;

public class PharmacistView {
	
    public void viewAppointmentOutcomeRecord(AppointmentOutcomeRecord record) {
        if (record == null) {
            System.out.println("Appointment record not found.");
            return;
        }

        // Displaying basic appointment information
        System.out.println("Appointment ID: " + record.getAppointmentID());
        System.out.println("Patient ID: " + record.getPatientID());
        System.out.println("Doctor ID: " + record.getDoctorID());
        System.out.println("Date: " + record.getDate());
        System.out.println("Consultation Notes: " + record.getConsultationNotes());
        System.out.println("Service Type: " + record.getServiceType());

        // Displaying the status of the prescription
        System.out.println("Prescription Status: " + (record.getStatusOfPrescription() != null ? record.getStatusOfPrescription() : "No status"));

        // Displaying medications (if any)
        if (record.getMedications().isEmpty()) {
            System.out.println("No medications prescribed.");
        } else {
            System.out.println("Prescribed Medications:");
            for (String medicationName : record.getMedications().keySet()) {
                int quantity = record.getMedications().get(medicationName);
                System.out.println("  - " + medicationName + ": " + quantity);
            }
        }
    }
    
    public void displayInventory(Map<Medication, Integer> medicationStock) {
        if (medicationStock == null || medicationStock.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }

        System.out.println("Inventory:");
        for (Map.Entry<Medication, Integer> entry : medicationStock.entrySet()) {
            Medication medication = entry.getKey();
            Integer quantity = entry.getValue();

            // Display the medication name and its stock quantity
            System.out.println(medication.getName() + ": " + quantity + " units available");
        }
    }


}