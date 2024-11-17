import java.util.HashMap;
import java.util.List;

public class PharmacistPrescribe {

    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                // Check if the prescription is already dispensed
                if (record.getStatusOfPrescription() != null &&
                    record.getStatusOfPrescription().name().equalsIgnoreCase("DISPENSED")) {
                    throw new IllegalStateException("Prescription already dispensed for Appointment ID: " + appointmentID);
                }

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

    private void updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                record.setStatusOfPrescription(newStatus);
                AppointmentOutcomeRecordsCsvHelper.updateAppointmentOutcomeRecord(record); // Update in CSV
                return;
            }
        }
        throw new IllegalArgumentException("Appointment ID not found for updating status: " + appointmentID);
    }
}
