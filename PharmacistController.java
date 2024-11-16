

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PharmacistController extends User {
    
    private static int requestCounter = 1;  // Counter to track the ID generation
    private String hospitalID;

    public PharmacistController(String hospitalID, String password, String userRole) {
        super(hospitalID, password, userRole); 
        System.out.println("Hello " + hospitalID);
    }

    // Update the status of the prescription in the appointment record
    public String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                record.setStatusOfPrescription(newStatus);
                return "Status updated successfully";
            }
        }
        return "Appointment ID not found";
    }

    // Submit a replenishment request for medicines
    public void submitReplenishmentRequest(String medicineName, int quantity) {
        String requestID = getNextRequestID();  // Get the next request ID
        
        ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicineName, quantity);
        
        // Load existing requests, add the new one, and save all back to the CSV
        List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
        requests.add(request);
        
        ReplenishmentRequestCsvHelper.saveAllReplenishmentRequests(requests); // Save all requests back to the CSV
        
        System.out.println("Replenishment request submitted with ID: " + requestID);
    }
    
    // Helper method to get the next request ID
    private String getNextRequestID() {
        // Load previous request IDs from the CSV
        List<String> requestIDs = ReplenishmentRequestCsvHelper.loadRequestIDsFromCSV();
        
        if (requestIDs.isEmpty()) {
            return "R001";  // If there are no previous requests, start with R001
        }
    
        // Get the last request ID
        String lastRequestID = requestIDs.get(requestIDs.size() - 1);
        
        // Extract the number from the last request ID
        int lastNumber = Integer.parseInt(lastRequestID.substring(1)); // Remove 'R' and parse the number
        
        // Increment the number
        int newNumber = lastNumber + 1;
        
        // Format the new number as a 3-digit string (e.g., R002, R003)
        return String.format("R%03d", newNumber);
    }
    



    // Prescribe medicine based on quantities already set by the doctor
    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        // Iterate through the list of appointment outcome records to find the specific appointment
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                // Check if prescribed medicines are available
                HashMap<String, Integer> prescribedMedicines = record.getMedications();
                if (prescribedMedicines == null || prescribedMedicines.isEmpty()) {
                    throw new IllegalStateException("No prescribed medicines found for appointment ID: " + appointmentID);
                }
    
                // Process the prescribed medicines (e.g., update their status, quantity, etc.)
                for (String medicineName : prescribedMedicines.keySet()) {
                    int quantity = prescribedMedicines.get(medicineName);
    
                    // Check for invalid or zero quantity
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Invalid quantity for medicine: " + medicineName);
                    }
    
                    // Remove the prescribed quantity from inventory using InventoryController
                    inventoryController.removeMedication(medicineName, quantity);
                    
                    System.out.println("Processing medicine: " + medicineName + " with quantity: " + quantity);
                }
    
                // Update the status of the prescription to DISPENSED
                updateStatusOfPrescription(appointmentID, AppointmentOutcomeRecord.StatusOfPrescription.DISPENSED, records);
    
                return "Medicines processed successfully for appointment ID: " + appointmentID;
            }
        }
    
        // If appointment ID is not found
        throw new IllegalArgumentException("Appointment ID not found: " + appointmentID);
    }
    
    
    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equalsIgnoreCase(appointmentID)) {
                return record;
            }
        }
        return null;  // Return null if the record is not found
    }
}
