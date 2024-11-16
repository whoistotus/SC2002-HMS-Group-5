import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PharmacistApp {
    private PharmacistController pharmacistController;
    private PharmacistView pharmacistView;
    private Scanner scanner;

    public PharmacistApp(String hospitalID, String password, String userRole) {
        this.pharmacistController = new PharmacistController(hospitalID, password, userRole);
        this.pharmacistView = new PharmacistView();
        this.scanner = new Scanner(System.in);
    }

public static void main(String[] args) {
    Scanner inputScanner = new Scanner(System.in);
    
    // Initialize necessary variables outside the try-catch block
    List<Medication> medications = new ArrayList<>();
    
    InventoryController inventoryController = new InventoryController();
    List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecordsCsvHelper.loadAppointmentOutcomes();
    
    try {
        // Initialize medicationCSVReader inside try-catch to handle exceptions
        MedicationCSVReader medicationCSVReader = new MedicationCSVReader();
        
        // Get all medications from the CSV
        medications = medicationCSVReader.getAllMedications();
        
    } catch (FileNotFoundException e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }

    // Authentication inputs
    System.out.print("Enter Hospital ID: ");
    String hospitalID = inputScanner.nextLine();
    
    System.out.print("Enter Password: ");
    String password = inputScanner.nextLine();
    System.out.print("Enter User Role: ");
    String userRole = inputScanner.nextLine();
    
    // Initialize the PharmacistApp
    PharmacistApp app = new PharmacistApp(hospitalID, password, userRole);
    
    // Start the application and test all methods
    app.start(records, medications, inventoryController);
    
    inputScanner.close();
}


    public void start(List<AppointmentOutcomeRecord> records, List<Medication> medications, InventoryController inventoryController) {
        
            while (true) {
                System.out.println("\nPharmacist Menu:");
                System.out.println("1. View Patient Appointment Outcome Record");
                System.out.println("2. Monitor Inventory");
                System.out.println("3. Prescribe Medicine");
                System.out.println("4. Replenish Medicine");
                System.out.println("5. Exit");
                System.out.print("Select an option: ");
        
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                switch (choice) {
                case 1:
                        viewAppointmentOutcome(records);
                        break;
                case 2:
                        monitorInventory(medications);
                    break;
                case 3:
                    prescribeMedicine(records, inventoryController);
                    break;
                case 4:
                    replenishMedicine();
                    ReplenishmentRequestCsvHelper.printAllRequests();
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAppointmentOutcome(List<AppointmentOutcomeRecord> records) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
        
        // Fetch the appointment outcome record
        AppointmentOutcomeRecord record = pharmacistController.getAppointmentOutcomeRecord(appointmentID, records);
        
        // View the appointment outcome record if found
        if (record != null) {
            pharmacistView.viewAppointmentOutcomeRecord(record);
        } else {
            System.out.println("Appointment outcome record not found for ID: " + appointmentID);
        }
    }
    

    private void monitorInventory(List<Medication> medications) {
        // Display the current inventory of medications
        pharmacistView.displayInventory(medications);
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
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Submit the replenishment request for the medicine
            pharmacistController.submitReplenishmentRequest(medicineName, quantity);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
