import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class PharmacistController {
    private static int requestCounter = 1;
    private String hospitalID;

    public PharmacistController(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    // Update the status of the prescription in the appointment record
    public String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                record.setStatusOfPrescription(newStatus);
                AppointmentOutcomeRecordsCsvHelper.updateAppointmentOutcomeRecord(record); // Update in CSV
                return "Status updated successfully";
            }
        }
        return "Appointment ID not found";
    }

    // Submit a replenishment request for medicines
    public void submitReplenishmentRequest(String medicineName, int quantity) {
        String requestID = getNextRequestID();
        ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicineName, quantity);

        List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();
        requests.add(request);
        ReplenishmentRequestCsvHelper.saveAllReplenishmentRequests(requests);

        System.out.println("Replenishment request submitted with ID: " + requestID);
    }

    // Helper to get the next request ID
    private String getNextRequestID() {
        List<String> requestIDs = ReplenishmentRequestCsvHelper.loadRequestIDsFromCSV();
        if (requestIDs.isEmpty()) return "R001";

        String lastRequestID = requestIDs.get(requestIDs.size() - 1);
        int lastNumber = Integer.parseInt(lastRequestID.substring(1));
        return String.format("R%03d", lastNumber + 1);
    }

    // Prescribe medicines based on the quantities already set by the doctor
    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                HashMap<String, Integer> prescribedMedicines = record.getMedications();
                if (prescribedMedicines == null || prescribedMedicines.isEmpty()) {
                    throw new IllegalStateException("No prescribed medicines found for appointment ID: " + appointmentID);
                }

                for (String medicineName : prescribedMedicines.keySet()) {
                    int quantity = prescribedMedicines.get(medicineName);
                    if (quantity <= 0) throw new IllegalArgumentException("Invalid quantity for medicine: " + medicineName);
                    inventoryController.removeMedication(medicineName, quantity);
                    System.out.println("Processed medicine: " + medicineName + " with quantity: " + quantity);
                }

                updateStatusOfPrescription(appointmentID, AppointmentOutcomeRecord.StatusOfPrescription.DISPENSED, records);
                return "Medicines processed successfully for appointment ID: " + appointmentID;
            }
        }
        throw new IllegalArgumentException("Appointment ID not found: " + appointmentID);
    }

    // Get an appointment outcome record by appointment ID
    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equalsIgnoreCase(appointmentID)) return record;
        }
        return null;
    }
}
