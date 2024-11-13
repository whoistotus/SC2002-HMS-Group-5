package SC2002_Assignment;

import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.ArrayList;
import javax.swing.plaf.synth.SynthSpinnerUI;


public class AdminView {
    private InventoryController inventoryController;
    private AppointmentManager appointmentManager;
    private Medication medication;
    private List<HospitalStaff> staffs = new ArrayList<>();
    private AdminController adminController;
    private HospitalStaff hospitalStaff;
    

    Scanner sc = new Scanner(System.in);

    public AdminView(InventoryController inventoryController)
    {
        this.inventoryController = inventoryController;
        this.appointmentManager = appointmentManager;
        this.medication = medication;
    }



    public void showMenu()
    {
        System.out.println("======================");
        System.out.println("Administrator Menu: ");
        System.out.println("1 - Show Medication Inventory");
        System.out.println("2 - Manage Medication");
        System.out.println("3 - Show Hospital Staff");
        System.out.println("4 - Manage Staff");
        System.out.println("5 - Approve Replenishment Requests");
        System.out.println("6 - Appointment Details");
        System.out.println("7 - Logout");
        int choice = sc.nextInt();
        sc.nextLine();
        
        switch (choice)
        {
            case 1:
                showInventory();
            
            case 2:
                manageMedicationInventory();
            
            case 3:
                //needs to be able to show staff with filtering

            case 4:
                manageStaffList();

            case 5:
                ManageReplenishmentReq();
            case 7:
                //viewAppointments();

            default:
                System.out.println("Please input a valid choice");
                


            

                

        }
    }


    

    public void showInventory()
    {
        inventoryController.displayInventory();
    }

    public void manageMedicationInventory()
    {
        while (true)
        {
            System.out.println("=====Manage Medications=====");
            System.out.println("1 - Add Medication");
            System.out.println("2 - Remove Medication");
            System.out.println("3 - Update Stock Level");
            System.out.println("4 - Set Low Stock Alert Amount");
            System.out.println("5 - Return to Admin Menu");
            System.out.println("Please enter your choice: ");


            int choice;

            try
            {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input was not valid. Please enter a number 1-5.");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            switch (choice)
            {
                case 1: 
                    addMedication();
                    break;
                
                case 2:
                    removeMedication();
                    break;

                case 3:
                    updateStockLevel();
                    break;

                case 4: 
                    setThreshold();
                    break;

                case 5:
                    return;
                
                default:
                    System.out.println("Please input a valid choice");
                    break;
            }
        }
    }

    private void addMedication()
    {
        try 
        {
            System.out.println("Enter Medication Name to add: ");
            String name = sc.nextLine().trim().toLowerCase();

            if (name.isEmpty())
            {
                System.out.println("Medication name is empty.");
                return;
            }

            System.out.println("Initial Quantity to add: ");
            int quantity = Integer.parseInt(sc.nextLine().trim());

            if (quantity <= 0)
            {
                System.out.println("Please enter a valid quantity of medication.");
                return;
            }

            System.out.println("Set the low stock threshold: ");
            int threshold = Integer.parseInt(sc.nextLine().trim());

            if (threshold <= 0)
            {
                System.out.println("Please enter a valid low stock threshold.");
                return;
            }

            inventoryController.addMedication(name, quantity, threshold);
            System.out.println("Medication added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Input is invalid. Please input an integer quantity.");
        } catch (Exception e) {
            System.out.println("Error occured when adding medication: " + e.getMessage());
        }
    }


    private void removeMedication()
    {
        try
        {
            System.out.println("Enter Medication Name to remove: ");
            String name = sc.nextLine().trim().toLowerCase();

            if (name.isEmpty())
            {
                System.out.println("Medication name is empty.");
                return;
            }

            System.out.println("Quantity to remove: ");
            int quantity = Integer.parseInt(sc.nextLine().trim());

            if (quantity <= 0)
            {
                System.out.println("Please enter a valid quantity of medication.");
                return;
            }
            
            inventoryController.removeMedication(name, quantity);
            System.out.println("Medication removed sucessfully!");
        } catch (NumberFormatException e){
            System.out.println("Input is invalid. Please input an integer quantity");
        } catch (Exception e){
            System.out.println("Error occured while removing medication: " + e.getMessage());
        }
    }

    private void updateStockLevel()
    {
        try
        {
            System.out.println("Enter Medicine Name for Stock updating: ");
            String name = sc.nextLine().trim().toLowerCase();

            if (name.isEmpty())
            {
                System.out.println("Medication name is empty.");
                return;
            }

            System.out.println("Please enter quantity to add: ");
            int quantity = Integer.parseInt(sc.nextLine().trim());

            inventoryController.updateStockLevel(name, quantity);
            System.out.println("Stock updated!");
        } catch (NumberFormatException e){
            System.out.println("Input is invalid. Please input an integer quantity.");
        } catch (Exception e) {
            System.out.println("Error occured while updating quantity: " + e.getMessage());
        }
    }

    public void setThreshold()
    {
        try
        {
            System.out.println("Enter Medicine Name for Threshold updating: ");
            String name = sc.nextLine().trim().toLowerCase();

            if (name.isEmpty())
            {
                System.out.println("Medication name is empty.");
                return;
            }

            System.out.println("Please enter the new threshold to implement: ");
            int newthreshold = Integer.parseInt(sc.nextLine().trim());

            inventoryController.updateThreshold(name, newthreshold);
        }catch (NumberFormatException e){
            System.out.println("Input is invalid. Please input an integer quantity.");
        } catch (Exception e) {
            System.out.println("Error occured while updating quantity: " + e.getMessage());
        } 
    }


    public void viewAppointments(String patientID)
    {
        System.out.println("===== Appointment Details ========");
    }

    public void viewHospitalStaffs(List<HospitalStaff> staffList) {
        System.out.println("\n=== Hospital Staff List ===");
        if (staffList.isEmpty()) {
            System.out.println("No staff members registered in the system.");
            return;
        }
        for (HospitalStaff staff : staffList) {
            System.out.printf("ID: %s | Name: %s | Role: %s | Gender: %s | Age: %d%n",
                staff.getHospitalID(), staff.getName(), staff.getuserRole(),
                staff.getGender(), staff.getAge());
        }
    }

    public void viewScheduledAppointments(List<Appointment> appointments) {
        System.out.println("\n=== Scheduled Appointments ===");
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        for (Appointment appointment : appointments) {
            System.out.println(appointment.toString());
        }
    }

    public void viewReplenishmentRequests(List<ReplenishmentRequest> requests) {
        System.out.println("\n=== Replenishment Requests ===");
        if (requests.isEmpty()) {
            System.out.println("No pending replenishment requests.");
            return;
        }
        for (ReplenishmentRequest request : requests) {
            request.printRequest();
        }
    }

    public void displayStaffReport(List<HospitalStaff> staffList) {
        System.out.println("\n=== Hospital Staff Report ===");
        System.out.println("Total Staff Count: " + staffList.size());
        long doctorCount = staffList.stream()
                .filter(staff -> staff.getuserRole().equalsIgnoreCase("Doctor"))
                .count();
        long pharmacistCount = staffList.stream()
                .filter(staff -> staff.getuserRole().equalsIgnoreCase("Pharmacist"))
                .count();
        
        System.out.println("Doctors: " + doctorCount);
        System.out.println("Pharmacists: " + pharmacistCount);
        System.out.println("===========================");
    }

    public void displayInventory(InventoryController inventoryController) {
        System.out.println("\n=== Inventory Report ===");
        inventoryController.displayInventory();
        System.out.println("===========================");
    }

    //check if hospital ID is exisiting 
    public boolean existingHospitalID(String hospitalID)
    {
        for (HospitalStaff staff : staffs)
        {
            if (staff.getHospitalID().equalsIgnoreCase(hospitalID))
            {
                return true;
            }
        }
        return false;
    }

    public void manageStaffList() {
        System.out.println("=======Staff List Management=======");
        System.out.println("1 - Add Staff");
        System.out.println("2 - Remove Staff");
        System.out.println("3 - Update Staff's Details");
        System.out.println("4 - Return to main menu.");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                addStaff();
                break;
            case 2:
                removeStaff();
                break;
            case 3:
                updateStaffDetails();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                manageStaffList();
                break;
        }
    }

    private void addStaff() {
        boolean continueAdding = true;
    
        while (continueAdding) {
            try {
                // Hospital ID input
                String hospitalID;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the Hospital ID: ");
                    hospitalID = sc.nextLine().trim();
                    
                    if (hospitalID.equals("1")) {
                        return;
                    }
                    if (hospitalID.isEmpty()) {
                        System.out.println("Hospital ID cannot be empty. Please try again.");
                        continue;
                    }
                    if (existingHospitalID(hospitalID)) {
                        System.out.println("Hospital ID already exists. Please try again.");
                        continue;
                    }
                    break;
                }
    
                // Staff Name input
                String staffName;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the Staff Name: ");
                    staffName = sc.nextLine().trim();
                    
                    if (staffName.equals("1")) {
                        return;
                    }
                    if (staffName.isEmpty()) {
                        System.out.println("Staff Name cannot be empty. Please try again.");
                        continue;
                    }
                    break;
                }
    
                // Staff Role input
                String staffRole;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the Staff Role (Doctor/Pharmacist): ");
                    staffRole = sc.nextLine().trim().toLowerCase();
                    
                    if (staffRole.equals("1")) {
                        return;
                    }
                    if (!staffRole.equals("doctor") && !staffRole.equals("pharmacist")) {
                        System.out.println("Invalid role. Please enter either 'Doctor' or 'Pharmacist'.");
                        continue;
                    }
                    break;
                }
    
                // Gender input
                String gender;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the gender (Male/Female): ");
                    gender = sc.nextLine().trim().toLowerCase();
                    
                    if (gender.equals("1")) {
                        return;
                    }
                    if (!gender.equals("male") && !gender.equals("female")) {
                        System.out.println("Invalid gender. Please enter either 'Male' or 'Female'.");
                        continue;
                    }
                    break;
                }
    
                // Age input
                int age;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the age: ");
                    String ageInput = sc.nextLine().trim();
                    
                    if (ageInput.equals("1")) {
                        return;
                    }
                    try {
                        age = Integer.parseInt(ageInput);
                        if (age < 18 || age > 100) {
                            System.out.println("Age must be between 18 and 100. Please try again.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid age format. Please enter a number.");
                    }
                }
    
                // Add the staff member
                adminController.addStaff(hospitalID, "password", staffRole, staffName, gender, age);
                System.out.println("\nStaff added successfully!");
    
                // Ask if user wants to add another staff member
                while (true) {
                    System.out.println("\nDo you want to add another staff member? (Y/N): ");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("n")) {
                        continueAdding = false;
                        break;
                    } else if (response.equals("y")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter Y or N.");
                    }
                }
    
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private void removeStaff() {
        boolean continueRemoving = true;
    
        while (continueRemoving) {
            try {
                // Display current staff list before removal
                System.out.println("\nCurrent Staff List:");
                adminController.viewHospitalStaffs();  
                // Hospital ID input
                String hospitalID;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the Hospital ID of the staff to remove: ");
                    hospitalID = sc.nextLine().trim();
    
                    if (hospitalID.equals("1")) {
                        return;
                    }
                    if (hospitalID.isEmpty()) {
                        System.out.println("Hospital ID cannot be empty. Please try again.");
                        continue;
                    }
                    if (!existingHospitalID(hospitalID)) {
                        System.out.println("Staff with Hospital ID " + hospitalID + " does not exist.");
                        System.out.println("Please check the ID and try again.");
                        continue;
                    }
    
                    // Confirm removal
                    System.out.println("\nAre you sure you want to remove staff with Hospital ID " + hospitalID + "? (Y/N): ");
                    String confirmation = sc.nextLine().trim().toLowerCase();
                    if (!confirmation.equals("y")) {
                        System.out.println("Removal cancelled.");
                        break;
                    }
    
                    // Remove the staff member
                    adminController.removeStaff(hospitalID);
                    System.out.println("\nStaff removed successfully!");
                    break;
                }
    
                // Ask if user wants to remove another staff member
                while (true) {
                    System.out.println("\nDo you want to remove another staff member? (Y/N): ");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("n")) {
                        continueRemoving = false;
                        break;
                    } else if (response.equals("y")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter Y or N.");
                    }
                }
    
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                
                // Ask if user wants to try again after an error
                System.out.println("\nDo you want to try again? (Y/N): ");
                String response = sc.nextLine().trim().toLowerCase();
                if (!response.equals("y")) {
                    continueRemoving = false;
                }
            }
        }
    }

    private void updateStaffDetails() {
        boolean continueUpdating = true;
    
        while (continueUpdating) {
            try {
                // Display current staff list before update
                System.out.println("\nCurrent Staff List:");
                adminController.viewHospitalStaffs(); 
    
                // Hospital ID input
                String hospitalID;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the Hospital ID of the staff to update: ");
                    hospitalID = sc.nextLine().trim();
    
                    if (hospitalID.equals("1")) {
                        return;
                    }
                    if (hospitalID.isEmpty()) {
                        System.out.println("Hospital ID cannot be empty. Please try again.");
                        continue;
                    }
                    if (!existingHospitalID(hospitalID)) {
                        System.out.println("Staff with Hospital ID " + hospitalID + " does not exist.");
                        continue;
                    }              
                    break;
                }
    
                // Staff Name input
                String newStaffName;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the new Staff Name (press Enter to keep current name): ");
                    newStaffName = sc.nextLine().trim();
    
                    if (newStaffName.equals("1")) {
                        return;
                    }
                    if (newStaffName.isEmpty()) {
                        newStaffName = hospitalStaff.getName(); // Assuming this method exists
                    }
                    break;
                }
    
                // Staff Role input
                String newStaffRole;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the new Staff Role (Doctor/Pharmacist) (press Enter to keep current role): ");
                    newStaffRole = sc.nextLine().trim().toLowerCase();
    
                    if (newStaffRole.equals("1")) {
                        return;
                    }
                    if (newStaffRole.isEmpty()) {
                        newStaffRole = hospitalStaff.getuserRole(); // Assuming this method exists
                        break;
                    }
                    if (!newStaffRole.equals("doctor") && !newStaffRole.equals("pharmacist")) {
                        System.out.println("Invalid role. Please enter either 'Doctor' or 'Pharmacist'.");
                        continue;
                    }
                    break;
                }
    
                // Gender input
                String gender;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the new gender (Male/Female) (press Enter to keep current gender): ");
                    gender = sc.nextLine().trim().toLowerCase();
    
                    if (gender.equals("1")) {
                        return;
                    }
                    if (gender.isEmpty()) {
                        gender = hospitalStaff.getGender(); // Assuming this method exists
                        break;
                    }
                    if (!gender.equals("male") && !gender.equals("female")) {
                        System.out.println("Invalid gender. Please enter either 'Male' or 'Female'.");
                        continue;
                    }
                    break;
                }
    
                // Age input
                int age;
                while (true) {
                    System.out.println("\nEnter 1 to exit or");
                    System.out.println("Please enter the new age (press Enter to keep current age): ");
                    String ageInput = sc.nextLine().trim();
    
                    if (ageInput.equals("1")) {
                        return;
                    }
                    if (ageInput.isEmpty()) {
                        age = hospitalStaff.getAge(); // Assuming this method exists
                        break;
                    }
                    try {
                        age = Integer.parseInt(ageInput);
                        if (age < 18 || age > 100) {
                            System.out.println("Age must be between 18 and 100. Please try again.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid age format. Please enter a number.");
                    }
                }
    
                // Display summary of changes
                System.out.println("\nSummary of changes:");
                System.out.println("Name: " + newStaffName);
                System.out.println("Role: " + newStaffRole);
                System.out.println("Gender: " + gender);
                System.out.println("Age: " + age);
    
                // Confirm update
                System.out.println("\nDo you want to save these changes? (Y/N): ");
                String confirmation = sc.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    adminController.updateStaffInfo(hospitalID, newStaffName, newStaffRole, gender, age);
                    System.out.println("\nStaff details updated successfully!");
                } else {
                    System.out.println("\nUpdate cancelled.");
                }
    
                // Ask if user wants to update another staff member
                while (true) {
                    System.out.println("\nDo you want to update another staff member? (Y/N): ");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("n")) {
                        continueUpdating = false;
                        break;
                    } else if (response.equals("y")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter Y or N.");
                    }
                }
    
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                
                // Ask if user wants to try again after an error
                System.out.println("\nDo you want to try again? (Y/N): ");
                String response = sc.nextLine().trim().toLowerCase();
                if (!response.equals("y")) {
                    continueUpdating = false;
                }
            }
        }
    }

    private void ManageReplenishmentReq() {
        boolean continueManaging = true;
    
        while (continueManaging) {
            try {
                displayReplenishmentMenu();
                
                // Get user choice with input validation
                int choice;
                while (true) {
                    System.out.print("\nPlease enter your choice: ");
                    String input = sc.nextLine().trim();
    
                    try {
                        choice = Integer.parseInt(input);
                        if (choice >= 1 && choice <= 3) {
                            break;
                        } else {
                            System.out.println("Please enter a number between 1 and 3.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
    
                switch (choice) {
                    case 1: // View Replenishment Requests
                        System.out.println("\n===== Current Replenishment Requests =====");
                        //prints out the requests from csv file
                        
                        if (requests.isEmpty()) {
                            System.out.println("No replenishment requests found.");
                        } else {
                            displayRequestsTable(requests);
                        }
                        
                        // Pause before returning to menu
                        System.out.println("\nPress Enter to continue...");
                        sc.nextLine();
                        break;
    
                    case 2: // Approve Replenishment Requests
                        System.out.println("Enter request ID to approve: ");
                        String requestId = sc.nextLine().trim();
                        inventoryController.approveReplenishmentRequest(requestId); // Using the improved version from previous response
                        break;
    
                    case 3: // Return to Admin Menu
                        System.out.println("\nReturning to Admin Menu...");
                        continueManaging = false;
                        break;
    
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
    
            } catch (Exception e) {
                System.out.println("\nAn error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                
                // Clear scanner buffer
                sc.nextLine();
                
                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        }
    }
    
    // Helper method to display the menu
    private void displayReplenishmentMenu() {
        System.out.println("\n========================================");
        System.out.println("     Manage Replenishment Requests      ");
        System.out.println("========================================");
        System.out.println("1 - View Replenishment Requests");
        System.out.println("2 - Approve Replenishment Requests");
        System.out.println("3 - Return to Admin Menu");
        System.out.println("========================================");
    }
    
    // Helper method to display requests in a formatted table
    private void displayRequestsTable(List<ReplenishmentRequest> requests) {
        // Print table header
        System.out.println("\n" + String.format("%-15s %-20s %-12s %-10s", 
            "Request ID", "Medicine Name", "Quantity", "Status"));
        System.out.println("---------------------------------------------------");
    
        // Print each request
        for (ReplenishmentRequest request : requests) {
            System.out.println(String.format("%-15s %-20s %-12d %-10s",
                request.getRequestID(),
                request.getMedicineName(),
                request.getQuantity(),
                request.getStatus()));
        }
    }
    
    // Method to check if user wants to continue after an operation
    private boolean askToContinue(Scanner sc, String message) {
        while (true) {
            System.out.println("\n" + message + " (Y/N): ");
            String response = sc.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        }
    }
}