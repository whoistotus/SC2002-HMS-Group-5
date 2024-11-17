import java.util.List;

public class PharmacistController {
    private String hospitalID;
    private PharmacistReplenishRequestManager replenishRequestManager;
    private PharmacistPrescribe pharmacistPrescribe;

    public PharmacistController(String hospitalID) {
        this.hospitalID = hospitalID;
        this.replenishRequestManager = new PharmacistReplenishRequestManager();
        this.pharmacistPrescribe = new PharmacistPrescribe();
    }

    public void submitReplenishmentRequest(String medicineName, int quantity) {
        replenishRequestManager.submitReplenishmentRequest(medicineName, quantity);
    }

    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        return pharmacistPrescribe.prescribeMed(appointmentID, records, inventoryController);
    }

    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equalsIgnoreCase(appointmentID)) return record;
        }
        return null;
    }
}
