import java.util.List;

public interface IReplenishmentManager {
    void submitReplenishmentRequest(String medicineName, int quantity);
    List<ReplenishmentRequest> viewAllReplenishmentRequests();
}
