import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentRequestCsvHelper {
    private static final String FILE_PATH = "data/ReplenishmentRequests.csv";

    // Method to load all replenishment requests from the CSV
    public static List<ReplenishmentRequest> loadReplenishmentRequests() {
        List<ReplenishmentRequest> requests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String requestID = values[0];
                String medicineName = values[1];
                int quantity = Integer.parseInt(values[2]);
                ReplenishmentRequest.RequestStatus status = ReplenishmentRequest.RequestStatus.valueOf(values[3]);

                // Create a ReplenishmentRequest object and add to the list
                ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicineName, quantity);
                request.setStatus(status);
                requests.add(request);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + FILE_PATH);
            e.printStackTrace();
        }
        return requests;
    }

    // Method to load all request IDs from the CSV file (used for generating next ID)
    public static List<String> loadRequestIDsFromCSV() {
        List<String> requestIDs = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    requestIDs.add(values[0]); // Add requestID to the list
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + FILE_PATH);
            e.printStackTrace();
        }
        
        return requestIDs;
    }

    // Method to save all replenishment requests back to the CSV file
    public static void saveAllReplenishmentRequests(List<ReplenishmentRequest> requests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("RequestID,MedicineName,Quantity,Status"); // Header row
            for (ReplenishmentRequest request : requests) {
                writer.println(requestToCsv(request));
            }
            writer.flush();  // Explicit flush to ensure all data is written
        } catch (IOException e) {
            System.err.println("Error writing to file: " + FILE_PATH);
            e.printStackTrace();
        }
    }

    // Helper method to convert a ReplenishmentRequest object to CSV string
    private static String requestToCsv(ReplenishmentRequest request) {
        return String.join(",",
            request.getRequestID(),
            request.getMedicineName(),
            String.valueOf(request.getQuantity()),
            request.getStatus().toString()
        );
    }

    // Method to print all requests from the CSV
    public static void printAllRequests() {
        List<ReplenishmentRequest> requests = loadReplenishmentRequests();
        
        if (requests.isEmpty()) {
            System.out.println("No replenishment requests found.");
            return;
        }
        
        System.out.println("Replenishment Requests:");
        for (ReplenishmentRequest request : requests) {
            request.printRequest();  // Using the existing printRequest method to display details
        }
    }

}
