package SC2002_Assignment;

import SC2002_Assignment.StatusOfPrescription;
import SC2002_Assignment.Medicine;

public class Pharmacist {
    // Pharmacist's own attributes
    private String pharmacistID;
    private String name;
    private String emailAddress;
    private Pharmacy pharmacy;  // Reference to Pharmacy to access inventory

    // Constructor
    public Pharmacist(String pharmacistID, String name, String emailAddress, Pharmacy pharmacy) {
        this.pharmacistID = pharmacistID;
        this.name = name;
        this.emailAddress = emailAddress;
        this.pharmacy = pharmacy;
    }

    // Method to view a patient's appointment outcome by patient ID
    public void viewAppointmentOutcomeRecord(String patientID) {
        // Code to retrieve and display patient's appointment outcome
        System.out.println("Displaying appointment outcome for patient ID: " + patientID);
    }

    // Method to update the status of a prescription and return the status as a string
    public String updateStatusOfPrescription(String prescriptionID, StatusOfPrescription newStatus) {
        // Code to update prescription status in the database
        System.out.println("Updating prescription status for prescription ID: " + prescriptionID);
        return "Prescription status updated to " + newStatus;
    }

    // Method to monitor inventory
    public void monitorInventory() {
        // Display inventory levels from the Pharmacy
        pharmacy.displayInventory();
    }

    // Method to prescribe medicine
    public void prescribeMed(String patientID, String medicineName, int amount) {
        Medicine medicine = pharmacy.getMedicineByName(medicineName);

        if (medicine != null) {
            try {
                // Attempt to prescribe the requested amount of medicine
                if (!medicine.prescribe(amount)) {
                    throw new Exception("Not enough stock available. Please submit a replenishment request.");
                }
                System.out.printf("Successfully prescribed %d units of %s to patient %s.%n", amount, medicineName, patientID);
            } catch (Exception e) {
                System.out.printf("Failed to prescribe medication for patient %s. Error: %s%n", patientID, e.getMessage());
            }
        } else {
            System.out.printf("Medicine %s is not available in the inventory.%n", medicineName);
        }
    }


    // Method to submit replenishment requests for a medicine
    public String submitReplenishmentRequest(String medicineName, int quantity, InventoryManager inventoryManager) {
        ReplenishmentRequest request = new ReplenishmentRequest(medicineName, quantity);
        inventoryManager.addRequest(request);
        System.out.printf("Replenishment request for %d units of %s submitted to inventory manager.%n", quantity, medicineName);
        return "Replenishment request submitted for " + medicineName;
    }

}
