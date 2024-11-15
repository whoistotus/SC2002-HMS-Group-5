package SC2002_Assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVread {

    public static List<ReplenishmentRequest> loadRequestsFromCSV() {
        List<ReplenishmentRequest> requests = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("replenishment_requests.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String requestID = data[0];
                    String medicineName = data[1];
                    int quantity = Integer.parseInt(data[2]);
                    ReplenishmentRequest.RequestStatus status = ReplenishmentRequest.RequestStatus.valueOf(data[3]);

                    ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicineName, quantity);
                    if (status == ReplenishmentRequest.RequestStatus.APPROVED) {
                        request.approve();
                    } else if (status == ReplenishmentRequest.RequestStatus.REJECTED) {
                        request.reject();
                    }
                    
                    requests.add(request);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from CSV file: " + e.getMessage());
        }
        
        return requests;
    }
}