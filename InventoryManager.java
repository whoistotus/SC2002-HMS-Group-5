package SC2002_Assignment;

import java.util.ArrayList;

public class InventoryManager {
    private Pharmacy pharmacy;
    private ArrayList<ReplenishmentRequest> requests;

    public InventoryManager(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        this.requests = new ArrayList<>();
    }

    // Add a new request to the list
    public void addRequest(ReplenishmentRequest request) {
        requests.add(request);
    }

    // Approve a request and restock the medicine
    public void approveRequest(int requestIndex) {
        if (requestIndex < 0 || requestIndex >= requests.size()) {
            System.out.println("Invalid request index.");
            return;
        }

        ReplenishmentRequest request = requests.get(requestIndex);
        if (request.getStatus().equals("Pending")) {
            Medicine medicine = pharmacy.getMedicineByName(request.getMedicineName());
            if (medicine != null) {
                medicine.restockMedicine(request.getQuantity());
                request.approve();
                System.out.printf("Request approved. %d units of %s have been added to inventory.%n", 
                                  request.getQuantity(), request.getMedicineName());
            } else {
                System.out.printf("Medicine %s not found in inventory.%n", request.getMedicineName());
            }
        } else {
            System.out.println("Request has already been approved.");
        }
    }

    // Display all requests
    public void displayRequests() {
        System.out.println("Replenishment Requests:");
        for (int i = 0; i < requests.size(); i++) {
            System.out.print(i + ": ");
            requests.get(i).printRequest();
        }
    }
}

