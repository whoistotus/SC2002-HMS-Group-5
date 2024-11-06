import java.util.ArrayList;

public class Pharmacist extends User {

    private ArrayList<Medicine> inventory;

    // Constructor
    public Pharmacist(String userId, String name) {
        super(userId, name, "Pharmacist");
    }
    
    // Method to view a patient's appointment outcome by patient ID
    public void viewAppointmentOutcomeRecord(String patientID, String ApptNum) {
		// Code to retrieve and display patient's appointment outcome
    	AppointmentOutcomeRecord.viewRecord(patientID, ApptNum);
    }

    // Method to update the status of a prescription and return the status as a string
    public String updateStatusOfPrescription(String prescriptionID, StatusOfPrescription newStatus) {
        // Code to update prescription status in the database
        System.out.println("Updating prescription status for prescription ID: " + prescriptionID);
        return "Prescription status updated to " + newStatus;
    }

    // Method to monitor inventory
    public void displayInventory() {
        System.out.println("Medicine Inventory:");
        for (Medicine med : inventory) {
            med.print();
        }
    }
    
    public Medicine getMedicineByName(String name) {
        for (Medicine medicine : inventory) {
            if (medicine.getMedicineName().equalsIgnoreCase(name)) {
                return medicine;
            }
        }
        return null; // Return null if no match is found
    }
    // Method to prescribe medicine
    public void prescribeMed(String patientID, String ApptNum, String medicineName, int amount) {
        Medicine medicine = getMedicineByName(medicineName);

        if (medicine != null) {
            try {
                // Attempt to prescribe the requested amount of medicine
                if (!medicine.prescribe(amount)) {
                    throw new Exception("Not enough stock available. Please submit a replenishment request.");
                }
                System.out.printf("Successfully prescribed %d units of %s to patient %s.%n", amount, medicineName, patientID);
                //can add updateStatus in to AppointmentOutcomeRecord class
                AppointmentOutcomeRecord.updateStatus(patientID, ApptNum, medicineName, StatusOfPrescription.DISPENSED);
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
