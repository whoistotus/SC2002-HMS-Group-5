package SC2002_Assignment;

public class ReplenishmentRequest {
    private String medicineName;
    private int quantity;
    private String status; // "Pending" or "Approved"

    public ReplenishmentRequest(String medicineName, int quantity) {
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.status = "Pending";
    }

    // Getters
    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    // Approve the request, which changes the status
    public void approve() {
        this.status = "Approved";
    }

    public void printRequest() {
        System.out.printf("Replenishment Request - Medicine: %s, Quantity: %d, Status: %s%n", medicineName, quantity, status);
    }
}
