import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryController {
    private MedicationCSVReader csvReader;
    private List<Medication> medicationList;
    private List<ReplenishmentRequest> replenishmentRequests;

    // Constructor
    public InventoryController() {
        try {
            this.csvReader = new MedicationCSVReader();
            this.medicationList = csvReader.getAllMedications();
            this.replenishmentRequests = new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Error initializing inventory: " + e.getMessage());
            this.medicationList = new ArrayList<>();
        }
    }

    public void displayInventory() {
        if (medicationList.isEmpty()) {
            System.out.println("No medications in the inventory.");
            return;
        }
        System.out.println("\n--- Medication Inventory Overview ---");
        for (Medication medication : medicationList) {
            System.out.println("Medication: " + medication.getName() + 
                             " | Quantity: " + medication.getQuantity() + 
                             " | Threshold: " + medication.getThreshold());
        }
    }

    public void addMedication(String name, int quantity, int threshold) {
        try {
            csvReader.addMedication(name, quantity, "", threshold);
            // Refresh the local list
            medicationList = csvReader.getAllMedications();
            System.out.println("Added new medication: " + name + " with quantity " + quantity + ".");
        } catch (IOException e) {
            System.out.println("Error adding medication: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeMedication(String name, int quantity) {
        Medication medication = findMedicationByName(name);
        if (medication != null) {
            try {
                if (medication.getQuantity() < quantity) {
                    System.out.println("Cannot remove " + quantity + " units. Only " + medication.getQuantity() + " available.");
                    return;
                }
                
                int newQuantity = medication.getQuantity() - quantity;
                if (newQuantity == 0) {
                    csvReader.removeMedication(name);
                } else {
                    csvReader.updateMedication(name, newQuantity);
                }
                
                // Refresh the local list
                medicationList = csvReader.getAllMedications();
                System.out.println("Removed " + quantity + " units of " + name + ".");
            } catch (IOException e) {
                System.out.println("Error updating medication: " + e.getMessage());
            }
        } else {
            System.out.println("Medication " + name + " not found in the inventory.");
        }
    }

    public void updateStockLevel(String name, int quantity) {
        Medication medication = findMedicationByName(name);
        if (medication != null) {
            try {
                int newQuantity = medication.getQuantity() + quantity;
                if (newQuantity < 0) {
                    System.out.println("Cannot set stock level to " + newQuantity + ". Quantity cannot be negative.");
                    return;
                }
                
                if (newQuantity == 0) {
                    csvReader.removeMedication(name);
                } else {
                    csvReader.updateMedication(name, newQuantity);
                }
                
                // Refresh the local list
                medicationList = csvReader.getAllMedications();
                System.out.println("Stock level of " + name + " updated to " + newQuantity + ".");
            } catch (IOException e) {
                System.out.println("Error updating stock level: " + e.getMessage());
            }
        } else {
            System.out.println("Medication " + name + " not found in the inventory.");
        }
    }

    public void updateThreshold(String medicationName, int newThreshold) {
        try {
            csvReader.updateThreshold(medicationName, newThreshold);
            // Refresh the local list
            medicationList = csvReader.getAllMedications();
            System.out.println("Threshold updated successfully for " + medicationName);
        } catch (IOException e) {
            System.out.println("Error updating threshold: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Medication findMedicationByName(String name) {
        return medicationList.stream()
            .filter(med -> med.getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
            .findFirst()
            .orElse(null);
    }
    public boolean approveReplenishmentRequest(String requestId) {
        try {
            // Load replenishment requests
            replenishmentRequests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
            
            // Find the specific request
            ReplenishmentRequest requestToApprove = null;
            for (ReplenishmentRequest request : replenishmentRequests) {
                if (request.getRequestID().trim().equals(requestId.trim())) {
                    requestToApprove = request;
                    break;
                }
            }
            
            // Check if request exists and is pending
            if (requestToApprove == null) {
                System.out.println("Request ID not found.");
                return false;
            }
            
            if (requestToApprove.getStatus() != ReplenishmentRequest.RequestStatus.PENDING) {
                System.out.println("Request has already been " + requestToApprove.getStatus().toString().toLowerCase());
                return false;
            }
            
            // Update the request status
            requestToApprove.setStatus(ReplenishmentRequest.RequestStatus.APPROVED);
            
            // Update medication quantity in inventory
            try {
                String medicineName = requestToApprove.getMedicineName();
                Medication existingMed = findMedicationByName(medicineName);
                
                if (existingMed != null) {
                    // Get current quantity and add the requested amount
                    int currentQuantity = existingMed.getQuantity();
                    int newQuantity = currentQuantity + requestToApprove.getQuantity();
                    
                    // Update the medication in CSV
                    csvReader.updateMedication(medicineName, newQuantity);
                    
                    // Refresh local medication list
                    medicationList = csvReader.getAllMedications();
                    
                    System.out.println("Successfully approved replenishment request for " + medicineName);
                    System.out.println("Updated quantity from " + currentQuantity + " to " + newQuantity);
                } else {
                    // If medication doesn't exist, add it as new
                    csvReader.addMedication(
                        requestToApprove.getMedicineName(),
                        requestToApprove.getQuantity(),
                        "",  // Default empty description
                        10   // Default threshold
                    );
                    
                    // Refresh local medication list
                    medicationList = csvReader.getAllMedications();
                    
                    System.out.println("Added new medication: " + requestToApprove.getMedicineName());
                }
                
                // Save updated request status back to CSV
                ReplenishmentRequestCsvHelper.saveAllReplenishmentRequests(replenishmentRequests);
                
                return true;
                
            } catch (IOException e) {
                System.out.println("Error updating medication inventory: " + e.getMessage());
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Error processing replenishment request: " + e.getMessage());
            return false;
        }
    }

    public void checkLowStockMedications() {
        System.out.println("\n=== Low Stock Alert ===");
        boolean hasLowStock = false;
        
        for (Medication medication : medicationList) {
            if (medication.getQuantity() <= medication.getThreshold()) {
                System.out.printf("%s is low on stock! Current quantity: %d (Threshold: %d)%n",
                    medication.getName(),
                    medication.getQuantity(),
                    medication.getThreshold());
                hasLowStock = true;
            }
        }
        
        if (!hasLowStock) {
            System.out.println("All medications are above their threshold levels.");
        }
    }

    public void getReplenishmentRequests() {
        try {
            List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
            
            if (requests.isEmpty()) {
                System.out.println("No replenishment requests found.");
                return;
            }
            
            System.out.println("\n=== Replenishment Requests ===");
            System.out.printf("%-15s %-20s %-10s %-10s%n", 
                "Request ID", "Medicine Name", "Quantity", "Status");
            System.out.println("=".repeat(60));
            
            for (ReplenishmentRequest request : requests) {
                System.out.printf("%-15s %-20s %-10d %-10s%n",
                    request.getRequestID(),
                    request.getMedicineName(),
                    request.getQuantity(),
                    request.getStatus());
            }
            
        } catch (Exception e) {
            System.out.println("Error loading replenishment requests: " + e.getMessage());
        }
    }

    // Helper method to get pending requests only
    public List<ReplenishmentRequest> getPendingRequests() {
        List<ReplenishmentRequest> allRequests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
        return allRequests.stream()
            .filter(req -> req.getStatus() == ReplenishmentRequest.RequestStatus.PENDING)
            .collect(Collectors.toList());
    }
}