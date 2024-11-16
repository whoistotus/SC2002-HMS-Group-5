import java.util.Map; // Add this import for Map.Entry
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PharmacistView {

    private PharmacistController pharmacistController;
    private Scanner scanner;

    public PharmacistView(String hospitalID) {
        this.pharmacistController = new PharmacistController(hospitalID);
        this.scanner = new Scanner(System.in);
    }

    // public static void main(String[] args) {
    //     Scanner inputScanner = new Scanner(System.in);

    //     // Initialize necessary variables
    //     List<Medication> medications = new ArrayList<>();
    //     InventoryController inventoryController = new InventoryController();
    //     List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecordsCsvHelper.loadAppointmentOutcomes();

    //     // Load medications from CSV
    //     try {
    //         MedicationCSVReader medicationCSVReader = new MedicationCSVReader();
    //         medications = medicationCSVReader.getAllMedications();
    //     } catch (FileNotFoundException e) {
    //         System.out.println("Error: Medication file not found.");
    //         e.printStackTrace();
    //     }

    //     // Authentication inputs
    //     System.out.print("Enter Hospital ID: ");
    //     String hospitalID = inputScanner.nextLine();

    //     System.out.print("Enter Password: ");
    //     String password = inputScanner.nextLine();

    //     System.out.print("Enter User Role: ");
    //     String userRole = inputScanner.nextLine();

    //     // Initialize the Pharmacist App
    //     PharmacistView app = new PharmacistView(hospitalID);

    //     // Start the application
    //     app.start(records, medications, inventoryController);

    //     inputScanner.close();
    // }

    public void pharmacistMenu() {
        System.out.println("\nPharmacist Menu:");
        System.out.println("1. View Patient Appointment Outcome Record");
        System.out.println("2. Monitor Inventory");
        System.out.println("3. Prescribe Medicine");
        System.out.println("4. Replenish Medicine");
        System.out.println("5. View All Replenishment Requests"); // New menu option
        System.out.println("6. Exit");
        System.out.print("Select an option: ");
    }

    public void start(List<AppointmentOutcomeRecord> records, List<Medication> medications, InventoryController inventoryController) {
        while (true) {
            pharmacistMenu();

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Handle non-integer inputs
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAppointmentOutcome(records);
                    break;
                case 2:
                    displayInventory(medications);
                    break;
                case 3:
                    prescribeMedicine(records, inventoryController);
                    break;
                case 4:
                    replenishMedicine();
                    break;
                case 5:
                    viewAllReplenishmentRequests(); // Call new method
                    break;
                case 6:
                    System.out.println("Exiting the application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllReplenishmentRequests() {
        // Retrieve all replenishment requests
        List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();

        if (requests.isEmpty()) {
            System.out.println("No replenishment requests found.");
            return;
        }

        System.out.println("\nAll Replenishment Requests:");
        for (ReplenishmentRequest request : requests) {
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Medicine Name: " + request.getMedicineName());
            System.out.println("Quantity: " + request.getQuantity());
            System.out.println("Status: " + request.getStatus());
            System.out.println("----------------------------");
        }
    }

    private void viewAppointmentOutcome(List<AppointmentOutcomeRecord> records) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();

        // Fetch the appointment outcome record
        AppointmentOutcomeRecord record = pharmacistController.getAppointmentOutcomeRecord(appointmentID, records);

        // View the appointment outcome record if found
        if (record != null) {
            viewAppointmentOutcomeRecord(record);
        } else {
            System.out.println("Appointment outcome record not found for ID: " + appointmentID);
        }
    }

    private void prescribeMedicine(List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();

        // Retrieve the appointment outcome record
        AppointmentOutcomeRecord record = pharmacistController.getAppointmentOutcomeRecord(appointmentID, records);

        if (record == null) {
            System.out.println("No appointment found with ID: " + appointmentID);
            return;
        }

        try {
            // Prescribe the medication and update the inventory
            String result = pharmacistController.prescribeMed(appointmentID, records, inventoryController);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void replenishMedicine() {
        System.out.print("Enter Medicine name: ");
        String medicineName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine()); // Handle invalid quantity inputs
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a valid number.");
            return;
        }

        try {
            // Submit the replenishment request for the medicine
            pharmacistController.submitReplenishmentRequest(medicineName, quantity);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewAppointmentOutcomeRecord(AppointmentOutcomeRecord record) {
        if (record == null) {
            System.out.println("Appointment record not found.");
            return;
        }

        // Displaying basic appointment information
        System.out.println("Appointment ID: " + record.getAppointmentID());
        System.out.println("Patient ID: " + record.getPatientID());
        System.out.println("Doctor ID: " + record.getDoctorID());
        System.out.println("Date: " + record.getDate());
        System.out.println("Consultation Notes: " + record.getConsultationNotes());
        System.out.println("Service Type: " + record.getServiceType());

        // Displaying the status of the prescription
        System.out.println("Prescription Status: " + (record.getStatusOfPrescription() != null ? record.getStatusOfPrescription() : "No status"));

        // Displaying medications (if any)
        if (record.getMedications().isEmpty()) {
            System.out.println("No medications prescribed.");
        } else {
            System.out.println("Prescribed Medications:");
            for (Map.Entry<String, Integer> entry : record.getMedications().entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void displayInventory(List<Medication> medications) {
        if (medications == null || medications.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }

        System.out.println("Inventory:");
        for (Medication medication : medications) {
            // Display the medication name and its stock quantity
            System.out.println(medication.getName() + ": " + medication.getQuantity() + " units available");
        }
    }
}
