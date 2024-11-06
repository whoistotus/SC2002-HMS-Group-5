import java.util.Scanner;

public class PharmacistApp {
    private Pharmacist pharmacist;
    private Scanner scanner;

    public PharmacistApp(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        Pharmacist pharmacist = new Pharmacist(Pharmacist.getHospitalID(), Pharmacist.getName());
        PharmacistApp app = new PharmacistApp(pharmacist);
        app.start();
    }

    public void start() {
    	
    	System.out.println("Welcome, " + Pharmacist.getHospitalID() + ".");
    	
        while (true) {
            System.out.println("Pharmacist Menu:");
            System.out.println("1. View Patient Appointment Outcome Record");
            System.out.println("2. Monitor Inventory");
            System.out.println("3. Prescribe Medicine");
            System.out.println("4. Submit Replenishment Request");
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
                    submitReplenishmentRequest();
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    return; // Exit the loop
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAppointmentOutcome() {
        System.out.println("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        System.out.println("Enter Appointment Number: ");
        String ApptNum = scanner.nextLine();
        pharmacist.viewAppointmentOutcomeRecord(patientID, ApptNum);
    }

    private void monitorInventory() {
        pharmacist.displayInventory();
    }

    private void prescribeMedicine() {
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        System.out.print("Enter Appointment Number: ");
        String ApptNum = scanner.nextLine();
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        pharmacist.prescribeMed(patientID, ApptNum, medicineName, amount);
    }

    private void submitReplenishmentRequest() {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        InventoryManager inventoryManager = new InventoryManager();
        pharmacist.submitReplenishmentRequest(medicineName, quantity, inventoryManager);
    }


}
