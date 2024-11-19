package view;
import java.util.List;

import controller.InventoryController;
import model.AppointmentOutcomeRecord;

public interface IPrescriptionManager {
    String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController);
    String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records);
}