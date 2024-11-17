import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class PharmacistView {
    private PharmacistController pharmacistController;
    private InventoryController inventoryController;
    private Scanner scanner;

    public PharmacistView(String hospitalID) {
        this.pharmacistController = new PharmacistController(hospitalID);
        this.scanner = new Scanner(System.in);
    }

    // Main application loop
    public void start() {
        InventoryController inventoryController = new InventoryController();
        List<Medication> medications = loadMedications();
        List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecordsCsvHelper.loadAppointmentOutcomes();

        while (true) {
            displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> viewAppointmentOutcome(records);
                case 2 -> displayInventory();
                case 3 -> prescribeMedicine(records, inventoryController);
                case 4 -> replenishMedicine();
                case 5 -> viewAllReplenishmentRequests();
                case 6 -> {
                    System.out.println("Exiting the application.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Display the main menu
    private void displayMenu() {
        System.out.println("\nPharmacist Menu:");
        System.out.println("1. View Patient Appointment Outcome Record");
        System.out.println("2. Monitor Inventory");
        System.out.println("3. Prescribe Medicine");
        System.out.println("4. Replenish Medicine");
        System.out.println("5. View All Replenishment Requests");
        System.out.println("6. Exit");
        System.out.print("Select an option: ");
    }

    // Helper to get a valid user choice
    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    // Load medications from CSV
    private List<Medication> loadMedications() {
        try {
            return new MedicationCSVReader().getAllMedications();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Medication file not found.");
            return new ArrayList<>();
        }
    }

    // View appointment outcome details
    private void viewAppointmentOutcome(List<AppointmentOutcomeRecord> records) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
        AppointmentOutcomeRecord record = pharmacistController.getAppointmentOutcomeRecord(appointmentID, records);
        if (record != null) {
            displayAppointmentOutcomeRecord(record);
        } else {
            System.out.println("Appointment outcome record not found for ID: " + appointmentID);
        }
    }

    // Display appointment outcome record details
    private void displayAppointmentOutcomeRecord(AppointmentOutcomeRecord record) {
        System.out.println("Appointment ID: " + record.getAppointmentID());
        System.out.println("Patient ID: " + record.getPatientID());
        System.out.println("Doctor ID: " + record.getDoctorID());
        System.out.println("Date: " + record.getDate());
        System.out.println("Consultation Notes: " + record.getConsultationNotes());
        System.out.println("Service Type: " + record.getServiceType());
        System.out.println("Prescription Status: " + (record.getStatusOfPrescription() != null ? record.getStatusOfPrescription() : "No status"));
        if (record.getMedications().isEmpty()) {
            System.out.println("No medications prescribed.");
        } else {
            System.out.println("Prescribed Medications:");
            for (Map.Entry<String, Integer> entry : record.getMedications().entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    // Display inventory details
    private void displayInventory() {
        try {
            // Use MedicationCSVReader to fetch the list of medications from the CSV
            MedicationCSVReader medicationCSVReader = new MedicationCSVReader();
            List<Medication> medications = medicationCSVReader.getAllMedications();
    
            if (medications == null || medications.isEmpty()) {
                System.out.println("The inventory is empty.");
                return;
            }
    
            System.out.println("Inventory:");
            for (Medication medication : medications) {
                System.out.println(medication.getName() + ": " + medication.getQuantity() + " units available");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Medication file not found.");
        } catch (Exception e) {
            System.out.println("Error reading inventory: " + e.getMessage());
        }
    }
    

    // Prescribe medicine
    private void prescribeMedicine(List<AppointmentOutcomeRecord> records, InventoryController inventoryController) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
        try {
            String result = pharmacistController.prescribeMed(appointmentID, records, inventoryController);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Submit replenishment request
    private void replenishMedicine() {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a valid number.");
            return;
        }
        pharmacistController.submitReplenishmentRequest(medicineName, quantity);
    }

    // View all replenishment requests
    private void viewAllReplenishmentRequests() {
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
}
