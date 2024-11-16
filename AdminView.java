import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminView {
    private static final String STAFF_DATA_PATH = "data/StaffList.csv";
    private InventoryController inventoryController;
    private AppointmentManager appointmentManager;
    private Medication medication;
    private List<HospitalStaff> staffs = new ArrayList<>();
    private AdminController adminController;
    private HospitalStaff hospitalStaff;
    private AdminCSVReader admincsvReader;
    private List<ReplenishmentRequest> requests = new ArrayList<>();
    private MedicationCSVReader medicationReader;


    Scanner sc = new Scanner(System.in);

    //Constructor
    public AdminView(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
        this.appointmentManager = appointmentManager;
        this.medication = medication;
        AdminModel model = new AdminModel();
        this.adminController = new AdminController(model, this);
        
        try {
            this.admincsvReader = new AdminCSVReader(STAFF_DATA_PATH);
            this.medicationReader = new MedicationCSVReader();
        } catch (Exception e) {
            System.out.println("Error initializing readers: " + e.getMessage());
        }
    }


    public void showMenu() {
        while (true) {  
            System.out.println("======================");
            System.out.println("Administrator Menu: ");
            System.out.println("1 - Show Medication Inventory");
            System.out.println("2 - Manage Medication");
            System.out.println("3 - Show Hospital Staff");
            System.out.println("4 - Manage Staff");
            System.out.println("5 - Manage Replenishment Requests");
            System.out.println("6 - Appointment Details");
            System.out.println("7 - Logout");
            System.out.println("======================");
            System.out.println("Please enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    showInventory(); //csv file
                    break;
                
                case 2:
                    manageMedicationInventory(); //csv
                    break;
                
                case 3:
                    filterAndDisplayStaff(); //csv
                    break;
    
                case 4:
                    manageStaffList(); //csv
                    break;
    
                case 5:
                    ManageReplenishmentReq();
                    break;
    
                case 6:
                    showAppointmentDetails();
                    break;
                
                case 7: 
                    System.out.println("Logging out....");
                    return;  
    
                default:
                    System.out.println("Please input a valid choice");
            }
        }
    }


    

    public void showInventory() {
        try {
            List<Medication> medications = medicationReader.getAllMedications();
            
            if (medications.isEmpty()) {
                System.out.println("No medications found in inventory.");
                return;
            }
    
            System.out.println("\n================= Medication Inventory =================");
            System.out.printf("%-20s %-10s %-40s %-15s%n", 
                "Name", "Stock", "Description", "Threshold");
            System.out.println("=".repeat(85));
    
            for (Medication med : medications) {
                System.out.printf("%-20s %-10d %-40s %-15d%n",
                    med.getName(),
                    med.getQuantity(),
                    med.getDescription(),
                    med.getThreshold());
    
                // Show warning if stock is below threshold
                if (med.getQuantity() <= med.getThreshold()) {
                    System.out.printf("WARNING: %s is below or at threshold level!%n", med.getName());
                }
            }
            System.out.println("=".repeat(85));
            
            // Pause for user to read
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Error displaying inventory: " + e.getMessage());
        }
    }
    

    public void manageMedicationInventory() {
        while (true) {
            System.out.println("\n===== Manage Medications =====");
            System.out.println("1 - Add Medication");
            System.out.println("2 - Remove Medication");
            System.out.println("3 - Update Stock Level");
            System.out.println("4 - Set Low Stock Alert Amount");
            System.out.println("5 - Return to Admin Menu");
            System.out.println("Please enter your choice: ");
    
            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
    
                switch (choice) {
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
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    //medication methods start  
    private void addMedication() {
        try {
            System.out.println("Enter Medication Name: ");
            String name = sc.nextLine().trim();
    
            if (name.isEmpty()) {
                System.out.println("Medication name cannot be empty.");
                return;
            }
    
            System.out.println("Enter Initial Stock Level: ");
            int stock = Integer.parseInt(sc.nextLine().trim());
    
            System.out.println("Enter Medication Description: ");
            String description = sc.nextLine().trim();
    
            System.out.println("Enter Low Stock Threshold: ");
            int threshold = Integer.parseInt(sc.nextLine().trim());
    
            medicationReader.addMedication(name, stock, description, threshold);
            System.out.println("Medication added successfully!");
    
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error adding medication: " + e.getMessage());
        }
    }
    
    private void removeMedication() {
        try {
            System.out.println("Enter Medication Name to remove: ");
            String name = sc.nextLine().trim();
    
            if (name.isEmpty()) {
                System.out.println("Medication name cannot be empty.");
                return;
            }
    
            System.out.println("Are you sure you want to remove " + name + "? (Y/N)");
            String confirm = sc.nextLine().trim().toLowerCase();
    
            if (confirm.equals("y")) {
                medicationReader.removeMedication(name);
                System.out.println("Medication removed successfully!");
            } else {
                System.out.println("Removal cancelled.");
            }
    
        } catch (Exception e) {
            System.out.println("Error removing medication: " + e.getMessage());
        }
    }

    private void updateStockLevel() {
        try {
            System.out.println("Enter Medication Name: ");
            String name = sc.nextLine().trim();
    
            if (name.isEmpty()) {
                System.out.println("Medication name cannot be empty.");
                return;
            }
    
            System.out.println("Enter New Stock Level: ");
            int newStock = Integer.parseInt(sc.nextLine().trim());
    
            if (newStock < 0) {
                System.out.println("Stock level cannot be negative.");
                return;
            }
    
            medicationReader.updateMedication(name, newStock);
            System.out.println("Stock level updated successfully!");
    
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error updating stock level: " + e.getMessage());
        }
    }

    private void setThreshold() {
        try {
            System.out.println("Enter Medication Name: ");
            String name = sc.nextLine().trim();
    
            if (name.isEmpty()) {
                System.out.println("Medication name cannot be empty.");
                return;
            }
    
            System.out.println("Enter New Threshold Level: ");
            int newThreshold = Integer.parseInt(sc.nextLine().trim());
    
            if (newThreshold < 0) {
                System.out.println("Threshold cannot be negative.");
                return;
            }
    
            medicationReader.updateThreshold(name, newThreshold);
            System.out.println("Threshold updated successfully!");
    
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error updating threshold: " + e.getMessage());
        }
    }
    //medication methods end

    //Appointment methods start
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
    //Appointment methods end

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
        System.out.println("====================================");

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
            System.out.println("\n=== Remove Staff ===");
            System.out.println("Enter Hospital ID (or 'exit' to return): ");
            String hospitalID = sc.nextLine().trim();

            if (hospitalID.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Confirm removal
                System.out.println("\nAre you sure you want to remove staff with Hospital ID " + hospitalID + "? (Y/N): ");
                String confirmation = sc.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    adminController.removeStaff(hospitalID);
                } else {
                    System.out.println("Removal cancelled.");
                }
            } catch (Exception e) {
                System.out.println("Error removing staff: " + e.getMessage());
            }

            System.out.println("\nRemove another staff member? (Y/N): ");
            if (!sc.nextLine().trim().toLowerCase().equals("y")) {
                continueRemoving = false;
            }
        }
    }

    private void updateStaffDetails() {
        boolean continueUpdating = true;
    
        while (continueUpdating) {
            try {
                System.out.println("\nCurrent Staff List:");
                List<HospitalStaff> staffList = admincsvReader.getAllStaff();
                displayFilteredStaff(staffList);
    
                System.out.println("\nEnter 1 to exit or");
                System.out.println("Please enter the Hospital ID of the staff to update: ");
                String hospitalID = sc.nextLine().trim();
    
                if (hospitalID.equals("1")) return;
                
                // Validate ID exists
                List<HospitalStaff> foundStaff = admincsvReader.filterByStaffId(hospitalID);
                if (foundStaff.isEmpty()) {
                    System.out.println("Staff member not found.");
                    continue;
                }
    
                System.out.println("Enter new name (or press Enter to keep current): ");
                String name = sc.nextLine().trim();
                if (name.isEmpty()) name = foundStaff.get(0).getName();
    
                System.out.println("Enter new role (Doctor/Pharmacist) (or press Enter to keep current): ");
                String role = sc.nextLine().trim();
                if (role.isEmpty()) {
                    role = foundStaff.get(0).getuserRole();
                } else if (!role.equalsIgnoreCase("doctor") && !role.equalsIgnoreCase("pharmacist")) {
                    System.out.println("Invalid role. Please enter Doctor or Pharmacist.");
                    continue;
                }
    
                System.out.println("Enter new gender (Male/Female) (or press Enter to keep current): ");
                String gender = sc.nextLine().trim();
                if (gender.isEmpty()) {
                    gender = foundStaff.get(0).getGender();
                } else if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
                    System.out.println("Invalid gender. Please enter Male or Female.");
                    continue;
                }
    
                int age = foundStaff.get(0).getAge();
                System.out.println("Enter new age (or press Enter to keep current): ");
                String ageInput = sc.nextLine().trim();
                if (!ageInput.isEmpty()) {
                    try {
                        age = Integer.parseInt(ageInput);
                        if (age < 18 || age > 100) {
                            System.out.println("Age must be between 18 and 100.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid age format.");
                        continue;
                    }
                }
    
                adminController.updateStaffInfo(hospitalID, name, role, gender, age, "password");
    
                System.out.println("\nUpdate another staff member? (Y/N): ");
                continueUpdating = sc.nextLine().trim().toLowerCase().equals("y");
    
            } catch (Exception e) {
                System.out.println("Error updating staff details: " + e.getMessage());
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

                        // Load requests from CSV file
                        List<ReplenishmentRequest> requests = ReplenishmentRequestCsvHelper.loadReplenishmentRequests();

                        if (requests == null || requests.isEmpty()) {
                            System.out.println("No replenishment requests found.");
                        } else {
                            displayRequestsTable(requests); // Display the requests in a table format
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

    private void displayFilteredStaff(List<HospitalStaff> staffList) {
        if (staffList == null || staffList.isEmpty()) {
            System.out.println("\nNo staff members found matching the criteria.");
            return;
        }
    
        System.out.println("\n========== Staff List ==========");
        System.out.printf("%-10s %-20s %-15s %-8s %-5s%n", 
            "Staff ID", "Name", "Role", "Gender", "Age");
        System.out.println("=".repeat(65));
    
        for (HospitalStaff staff : staffList) {
            System.out.printf("%-10s %-20s %-15s %-8s %-5d%n",
                staff.getHospitalID(),
                staff.getName(),
                staff.getuserRole(),
                staff.getGender(),
                staff.getAge());
        }
        System.out.println("=".repeat(65));
        System.out.println("Total staff members found: " + staffList.size());
        
        // Pause for user to read the results
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void filterAndDisplayStaff() {
        AdminCSVReader csvReader;
        try {
            csvReader = new AdminCSVReader(STAFF_DATA_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find staff data file.");
            System.out.println("Error details: " + e.getMessage());
            return;
        }
      
        while (true) {
            System.out.println("\n===== Filter Staff List =====");
            System.out.println("1. Filter by Staff ID (Format: [D/P/A]XXX)");
            System.out.println("2. Filter by Name");
            System.out.println("3. Filter by Role (Doctor/Pharmacist/Administrator)");
            System.out.println("4. Filter by Gender");
            System.out.println("5. Filter by Age");
            System.out.println("6. Show All Staff");
            System.out.println("7. Return to Main Menu");
            System.out.print("Enter your choice: ");
    
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
    
            List<HospitalStaff> filteredList;
            
            switch (choice) {
                case 1:
                    System.out.println("Enter Staff ID to search (Format: [D/P/A]XXX where X is a number)");
                    System.out.println("D for Doctor, P for Pharmacist, A for Administrator");
                    String staffId = sc.nextLine().trim().toUpperCase();
                    if (!staffId.matches("[DPA]\\d{3}")) {
                        System.out.println("Invalid staff ID format. Please use [D/P/A]XXX format.");
                        continue;
                    }
                    filteredList = admincsvReader.filterByStaffId(staffId);
                    displayFilteredStaff(filteredList);
                    break;
    
                case 2:
                    System.out.print("Enter Name to search: ");
                    String name = sc.nextLine().trim();
                    filteredList = admincsvReader.filterByName(name);
                    displayFilteredStaff(filteredList);
                    break;
    
                case 3:
                    System.out.println("Choose role to filter:");
                    System.out.println("1. Doctor");
                    System.out.println("2. Pharmacist");
                    System.out.println("3. Administrator");
                    System.out.print("Enter your choice (1-3): ");
                    
                    try {
                        int roleChoice = Integer.parseInt(sc.nextLine().trim());
                        String role;
                        switch (roleChoice) {
                            case 1:
                                role = "Doctor";
                                break;
                            case 2:
                                role = "Pharmacist";
                                break;
                            case 3:
                                role = "Administrator";
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                continue;
                        }
                        filteredList = admincsvReader.filterByRole(role);
                        displayFilteredStaff(filteredList);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }
                    break;
    
                case 4:
                    System.out.print("Enter Gender to filter (Male/Female): ");
                    String gender = sc.nextLine().trim();
                    if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
                        System.out.println("Invalid gender. Please enter Male or Female.");
                        continue;
                    }
                    filteredList = admincsvReader.filterByGender(gender);
                    displayFilteredStaff(filteredList);
                    break;
    
                case 5:
                    System.out.println("1. Search by specific age");
                    System.out.println("2. Search by age range");
                    System.out.print("Enter your choice: ");
                    
                    try {
                        int ageChoice = Integer.parseInt(sc.nextLine().trim());
                        if (ageChoice == 1) {
                            System.out.print("Enter age: ");
                            int age = Integer.parseInt(sc.nextLine().trim());
                            if (age < 18 || age > 100) {
                                System.out.println("Please enter a valid age between 18 and 100.");
                                continue;
                            }
                            filteredList = admincsvReader.filterByAge(age);
                        } else if (ageChoice == 2) {
                            System.out.print("Enter minimum age (18-100): ");
                            int minAge = Integer.parseInt(sc.nextLine().trim());
                            System.out.print("Enter maximum age (18-100): ");
                            int maxAge = Integer.parseInt(sc.nextLine().trim());
                            if (minAge < 18 || maxAge > 100 || minAge > maxAge) {
                                System.out.println("Please enter valid age range between 18 and 100.");
                                continue;
                            }
                            filteredList = admincsvReader.filterByAgeRange(minAge, maxAge);
                        } else {
                            System.out.println("Invalid choice.");
                            continue;
                        }
                        displayFilteredStaff(filteredList);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid age input.");
                        continue;
                    }
                    break;
    
                case 6:
                    filteredList = admincsvReader.getAllStaff();
                    displayFilteredStaff(filteredList);
                    break;
    
                case 7:
                    return;
    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void showAppointmentDetails() {
        System.out.println("====== Appointment List ===========");
        
        // Load all appointments from the CSV file
        List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments();
        List<AppointmentOutcomeRecord> appointmentOutcomes = AppointmentOutcomeRecordsCsvHelper.loadAppointmentOutcomes();
        
        // Check if appointments list is empty
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        
        // Create a map for fast lookup of outcomes by AppointmentID
        Map<String, AppointmentOutcomeRecord> outcomeMap = new HashMap<>();
        for (AppointmentOutcomeRecord outcome : appointmentOutcomes) {
            outcomeMap.put(outcome.getAppointmentID(), outcome);
        }
        
        // Display each appointment's details
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentID());
            System.out.println("Patient ID: " + appointment.getPatientID());
            System.out.println("Doctor ID: " + appointment.getDoctorID());
            System.out.println("Date and Time: " + appointment.getAppointmentDate()+ " , " + appointment.getAppointmentTime());
            System.out.println("Status: " + appointment.getStatus());
            
            // Check if the appointment is completed and display the outcome record if available
            if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
                AppointmentOutcomeRecord outcomeRecord = outcomeMap.get(appointment.getAppointmentID());
                if (outcomeRecord != null) {
                    System.out.println("Outcome Record:");
                    System.out.println(" - Service Type: " + outcomeRecord.getServiceType());
                    System.out.println(" - Consultation Notes: " + outcomeRecord.getConsultationNotes());
                    System.out.println(" - Medications:");
                    for (Map.Entry<String, Integer> med : outcomeRecord.getMedications().entrySet()) {
                        System.out.println("   * " + med.getKey() + ": " + med.getValue());
                    }
                    System.out.println(" - Status of Prescription: " + outcomeRecord.getStatusOfPrescription());
                } else {
                    System.out.println("No outcome record available for this appointment.");
                }
            }
            System.out.println("------------------------------");
        }
    }

    
}