import java.util.List;
import java.util.HashMap;

public class PharmacistController extends User {
    
    private static int requestCounter = 1;  // Counter to track the ID generation
    private String hospitalID;

    public PharmacistController(String hospitalID, String password, String userRole) {
        super(hospitalID, password, userRole); 
        System.out.println("Hello " + hospitalID);
    }

    // Update the status of the prescription in the appointment record
    public String updateStatusOfPrescription(String appointmentID, AppointmentOutcomeRecord.StatusOfPrescription newStatus, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                record.setStatusOfPrescription(newStatus);
                return "Status updated successfully";
            }
        }
        return "Appointment ID not found";
    }

    // Submit a replenishment request for medicines
    public void submitReplenishmentRequest(String medicineName, int quantity) {
        String requestID = String.format("%04d", requestCounter);
        requestCounter++;
        
        ReplenishmentRequestModel request = new ReplenishmentRequestModel(requestID, medicineName, quantity);
        
        // Save the request to the CSV file
        request.writeToCSV();
        
        System.out.println("Replenishment request submitted with ID: " + requestID);
    }


    // Prescribe medicine based on quantities already set by the doctor
    public String prescribeMed(String appointmentID, List<AppointmentOutcomeRecord> records) {
        // Iterate through the list of appointment outcome records to find the specific appointment
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                // Check if prescribed medicines are available
                HashMap<String, Integer> medications = record.getPrescribedMedicines();
                if (medications == null || medications.isEmpty()) {
                    throw new IllegalStateException("No prescribed medicines found for appointment ID: " + appointmentID);
                }

                // Process the prescribed medicines (e.g., update their status, quantity, etc.)
                for (String medicine : medications.keySet()) {
                    int quantity = medications.get(medicine);
                    
                    // Check for invalid or zero quantity
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Invalid quantity for medicine: " + medicine);
                    }

                    // Logic to process the prescribed medicine, e.g., update inventory or create a record
                   
                    medicine.subtractQuantity(quantity);
                    
                    System.out.println("Processing medicine: " + medicine + " with quantity: " + quantity);
                }
                
                updateStatusOfPrescription(appointmentID, AppointmentOutcomeRecord.StatusOfPrescription.DISPENSED, record);

                return "Medicines processed successfully for appointment ID: " + appointmentID;
            }
        }

        // If appointment ID is not found
        throw new IllegalArgumentException("Appointment ID not found: " + appointmentID);
    }
    
    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID, List<AppointmentOutcomeRecord> records) {
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(appointmentID)) {
                return record;
            }
        }
        return null;  // Return null if the record is not found
    }
}
