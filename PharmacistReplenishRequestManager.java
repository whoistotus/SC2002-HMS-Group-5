import java.util.List;

public class PharmacistReplenishRequestManager {

    public void submitReplenishmentRequest(String medicineName, int quantity) {
        String requestID = getNextRequestID();
        ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicineName, quantity);

        List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
        requests.add(request);
        ReplenishmentRequestCsvHelper.saveAllReplenishmentRequests(requests);

        System.out.println("Replenishment request submitted with ID: " + requestID);
    }

    private String getNextRequestID() {
        List<String> requestIDs = ReplenishmentRequestCsvHelper.loadRequestIDsFromCSV();
        if (requestIDs.isEmpty()) return "R001";

        String lastRequestID = requestIDs.get(requestIDs.size() - 1);
        int lastNumber = Integer.parseInt(lastRequestID.substring(1));
        return String.format("R%03d", lastNumber + 1);
    }
}
