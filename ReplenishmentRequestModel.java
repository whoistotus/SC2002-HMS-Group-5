import java.io.FileWriter;
import java.io.IOException;

public class ReplenishmentRequestModel {
    
    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    private String medicineName;
    private int quantity;
    private String requestID;
    private RequestStatus status;

    // Constructor
    public ReplenishmentRequestModel(String requestID, String medicineName, int quantity) {
        this.requestID = requestID;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.status = RequestStatus.PENDING; // Default status
    }

    // Getter for medicineName
    public String getMedicineName() {
        return medicineName;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Getter for requestID
    public String getRequestID() {
        return requestID;
    }

    // Getter for status
    public RequestStatus getStatus() {
        return status;
    }

    // Method to approve the request
    public void approve() {
        this.status = RequestStatus.APPROVED;
    }

    // Method to reject the request
    public void reject() {
        this.status = RequestStatus.REJECTED;
    }

    // Method to print the request details
    public void printRequest() {
        System.out.println("Request ID: " + requestID);
        System.out.println("Medicine Name: " + medicineName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Status: " + status);
    }

    // Method to save the request to a CSV file
    public void writeToCSV() {
        try (FileWriter writer = new FileWriter("replenishment_requests.csv", true)) {
            // Append data as a new line in the CSV file
            writer.append(requestID)
                  .append(',')
                  .append(medicineName)
                  .append(',')
                  .append(String.valueOf(quantity))
                  .append(',')
                  .append(status.toString())
                  .append('\n');
            writer.flush();
            System.out.println("Replenishment request saved to CSV with ID: " + requestID);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
