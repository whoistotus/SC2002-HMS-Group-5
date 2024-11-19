package view;
import java.util.List;

import model.ReplenishmentRequest;

public interface IReplenishmentManager {
    void submitReplenishmentRequest(String medicineName, int quantity);
    List<ReplenishmentRequest> viewAllReplenishmentRequests();
}
