package controller;
import java.util.List;

import model.AppointmentOutcomeRecord;
import model.PharmacistPrescribeManager;
import model.ReplenishmentRequest;
import view.IPrescriptionManager;
import view.IReplenishmentManager;

public class PharmacistController {
    private IReplenishmentManager replenishmentManager;
    private IPrescriptionManager prescriptionManager;

    public PharmacistController(String hospitalID) {
        this.replenishmentManager = new PharmacistReplenishRequestManager();
        this.prescriptionManager = new PharmacistPrescribeManager();
    }

    public void submitReplenishmentRequest(String medicineName, int quantity) {
        replenishmentManager.submitReplenishmentRequest(medicineName, quantity);
    }

    public List<ReplenishmentRequest> viewAllReplenishmentRequests() {
        return replenishmentManager.viewAllReplenishmentRequests();
    }

    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        return prescriptionManager.prescribeMed(appointmentID, records, inventoryController);
    }

    public String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records) {
        return prescriptionManager.updateStatusOfPrescription(appointmentID, newStatus, records);
    }

    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equalsIgnoreCase(appointmentID)) return record;
        }
        return null;
    }
}
