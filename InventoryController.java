

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryController {
    private List<Medication> medicationList;
    private List<ReplenishmentRequest> replenishmentRequests;

    // Constructor
    public InventoryController() {
        this.medicationList = new ArrayList<>();
        this.replenishmentRequests = new ArrayList<>();
    }

    public void displayInventory() {
        if (medicationList.isEmpty()) {
            System.out.println("No medications in the inventory.");
            return;
        }
        System.out.println("\n--- Medication Inventory Overview ---");
        for (Medication medication : medicationList) {
            System.out.println("Medication: " + medication.getName() + " | Quantity: " + medication.getQuantity());
        }
    }

    public void addMedication(String name, int quantity, int threshold) {
        Medication medication = findMedicationByName(name);
        if (medication != null) {
            medication.setQuantity(medication.getQuantity() + quantity);
            System.out.println("Updated quantity of " + name + " to " + medication.getQuantity() + ".");
        } else {
            Medication newMedication = new Medication(name, quantity, "", threshold);
            medicationList.add(newMedication);
            System.out.println("Added new medication: " + name + " with quantity " + quantity + ".");
        }
    }

    public void removeMedication(String name, int quantity) {
        Medication medication = findMedicationByName(name);
        if (medication != null) {
            if (medication.getQuantity() < quantity) {
                System.out.println("Cannot remove " + quantity + " units. Only " + medication.getQuantity() + " available.");
                return;
            }
            medication.setQuantity(medication.getQuantity() - quantity);
            System.out.println("Removed " + quantity + " units of " + name + ". New quantity: " + medication.getQuantity() + ".");
            if (medication.getQuantity() == 0) {
                medicationList.remove(medication);
                System.out.println(name + " has been removed from the inventory as its quantity reached zero.");
            }
        } else {
            System.out.println("Medication " + name + " not found in the inventory.");
        }
    }


    public void updateStockLevel(String name, int quantity) {
        Medication medication = findMedicationByName(name);
        if (medication != null) {
            int newQuantity = medication.getQuantity() + quantity;
            if (newQuantity < 0) {
                System.out.println("Cannot set stock level to " + newQuantity + ". Quantity cannot be negative.");
                return;
            }
            medication.setQuantity(newQuantity);
            System.out.println("Stock level of " + name + " updated to " + newQuantity + ".");
            if (medication.getQuantity() == 0) {
                medicationList.remove(medication);
                System.out.println(name + " has been removed from the inventory as its quantity reached zero.");
            }
        } else {
            System.out.println("Medication " + name + " not found in the inventory.");
        }
    }

    
    public boolean approveReplenishmentRequest(String requestId) {
        // Load replenishment requests
        replenishmentRequests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
        System.out.println("Loaded " + replenishmentRequests.size() + " replenishment requests.");
        
        // Iterate through requests to find the matching one
        for (ReplenishmentRequest request : replenishmentRequests) {
            System.out.println("Checking request ID: " + request.getRequestID() + ", Status: " + request.getStatus());
            
            if (request.getRequestID().trim().equals(requestId.trim()) 
                && request.getStatus() == ReplenishmentRequest.RequestStatus.PENDING) {
                
                // Update status to APPROVED
                request.setStatus(ReplenishmentRequest.RequestStatus.APPROVED);
                
                // Find medication in inventory
                String lowerCaseMedicineName = request.getMedicineName().trim().toLowerCase();
                Medication medication = findMedicationByName(lowerCaseMedicineName);
                
                if (medication != null) {
                    // Update medication quantity
                    medication.setQuantity(medication.getQuantity() + request.getQuantity());
                    System.out.println("Approved replenishment of " + request.getQuantity() + " units for " + medication.getName() + ".");
                    
                    // Save changes back to CSV
                    ReplenishmentRequestCsvHelper.saveAllReplenishmentRequests(replenishmentRequests);
                    return true;
                } else {
                    System.out.println("Medication " + request.getMedicineName() + " not found in inventory.");
                    return false;
                }
            }
        }
        
        // If no matching request found
        System.out.println("Replenishment request not found or already completed.");
        return false;
    }
    

    private Medication findMedicationByName(String name) {
        for (Medication medication : medicationList) {
            if (medication.getName().trim().toLowerCase().equals(name)) {
                return medication;
            }
        }
        return null;
    }

        // In InventoryController class
    public void updateThreshold(String medicationName, int newThreshold) {
        for (Medication med : medicationList) {
            if (med.getName().equalsIgnoreCase(medicationName)) {
                med.setThreshold(newThreshold);
                return;
            }
        }
        System.out.println("Medication not found: " + medicationName);
    }

    public void getReplenishmentRequests()
    {

    }

}
