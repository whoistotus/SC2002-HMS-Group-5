
public interface AppointmentOutcomeRecordInterface {
    void viewRecord(String patientID, String apptNum);
    String updateStatus(String prescriptionID, StatusOfPrescription newStatus);
}