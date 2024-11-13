import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PharmacistApp {
    private PharmacistController pharmacistController;
    private PharmacistView pharmacistView;
    private Scanner scanner;

    public PharmacistApp(String hospitalID) {
        this.pharmacistController = new PharmacistController(hospitalID);
        this.pharmacistView = new PharmacistView();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.print("Enter Hospital ID: ");
        Scanner inputScanner = new Scanner(System.in);
        String hospitalID = inputScanner.nextLine();
        PharmacistApp app = new PharmacistApp(hospitalID);
        app.start();
        inputScanner.close();
    }

    public void start() {
        System.out.println("Welcome, " + pharmacistController.getHospitalID() + ".");

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
                    viewAppointmentOutcome();
                    break;
                case 2:
                    monitorInventory();
                    break;
                case 3:
                	prescribeMedicine();
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

    private void viewAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
        pharmacistView.viewAppointmentOutcomeRecord(appointmentID);
    }

    private void monitorInventory() {
        pharmacistView.displayInventory();
    }
    
    private void prescribeMedicine() {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();

        // Retrieve appointment outcome records
        pharmacistView.viewAppointmentOutcomeRecord(appointmentID);
        AppointmentOutcomeRecord records = pharmacistController.getAppointmentOutcomeRecord(appointmentID, records);
        
        try {
            String result = pharmacistController.prescribeMed(appointmentID, records);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void replenishMedicine ()
    {
    	System.out.println("Enter Medicine name:");
    	String medicineName = scanner.nextLine();
    	System.out.println("Enter quantity:");
    	int quantity = scanner.nextInt();
    	
    	pharmacistController.submitReplenishmentRequest(medicineName, quantity);
    }

  
}
