import java.util.List;

public interface IPrescriptionManager {
    String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController);
    String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records);
}