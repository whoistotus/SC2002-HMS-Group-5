

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

    // public static void main(String[] args) {
    //     System.out.print("Enter Hospital ID: ");
    //     Scanner inputScanner = new Scanner(System.in);
    //     String hospitalID = inputScanner.nextLine();
    //     PharmacistApp app = new PharmacistApp(hospitalID, password, userRole);
    //     app.start();
    //     inputScanner.close();
    // }

    public void start(List<AppointmentOutcomeRecord> records, Map<Medication, Integer> medicationStock, InventoryController inventoryController) {
    
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
                    monitorInventory(medicationStock);
                    break;
                case 3:
                    prescribeMedicine(records, inventoryController);
                    break;
                case 4:
                    replenishMedicine();
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
    

    private void monitorInventory(Map<Medication, Integer> medicationStock) {
        // Display the current inventory of medications
        pharmacistView.displayInventory(medicationStock);
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
