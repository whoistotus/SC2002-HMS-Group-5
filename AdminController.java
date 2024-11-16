import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    private AdminModel model;
    private AdminView view;
    private static final String STAFF_CSV_PATH = "data/StaffList.csv";

    public AdminController(AdminModel model, AdminView view) {
        this.model = model;
        this.view = view;
    }

    // Staff Management Methods
    public void addStaff(String hospitalID, String password, String userRole, 
                        String name, String gender, int age) {
        // Validate input
        if (hospitalID == null || hospitalID.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty()) {
            System.out.println("Error: Required fields cannot be empty");
            return;
        }

        try {
            // Create new staff member
            HospitalStaff newStaff = new HospitalStaff(hospitalID, password, userRole, name, gender, age);
            
            // Check if staff already exists
            if (model.addStaffMember(newStaff)) {
                // Write to CSV file
                try (FileWriter fw = new FileWriter(STAFF_CSV_PATH, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    
                    // Format: hospitalID,password,userRole,name,gender,age
                    out.println(String.format("%s,%s,%s,%s,%d,%s",
                        hospitalID,
                        name,
                        userRole,
                        gender,
                        age,
                        password));
                        
                    System.out.println("Staff member added successfully to StaffList.csv");
                }
            } else {
                System.out.println("Error: Staff with ID " + hospitalID + " already exists");
            }
        } catch (IOException e) {
            System.out.println("Error writing to StaffList.csv: " + e.getMessage());
        }
    }

    public void removeStaff(String staffId) {
        try {
            List<String[]> allStaff = new ArrayList<>();
            boolean found = false;
            
            // Read and filter staff
            try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_CSV_PATH))) {
                String line;
                allStaff.add(reader.readLine().split(",")); // Keep header
                
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (!data[0].equals(staffId)) {
                        allStaff.add(data);
                    } else {
                        found = true;
                    }
                }
            }
            
            if (!found) {
                System.out.println("Staff member with ID " + staffId + " not found.");
                return;
            }
            
            // Write updated list
            try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_CSV_PATH))) {
                for (String[] staff : allStaff) {
                    writer.println(String.join(",", staff));
                }
                System.out.println("Staff member removed successfully.");
            }
            
        } catch (IOException e) {
            System.out.println("Error updating staff file: " + e.getMessage());
        }
    }

    public void updateStaffInfo(String staffId, String name, String role, String gender, int age, String password) {
        try {
            List<String[]> allStaff = new ArrayList<>();
            boolean found = false;
            
            // Read existing staff
            try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_CSV_PATH))) {
                String line;
                allStaff.add(reader.readLine().split(",")); // Keep header
                
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (!data[0].equals(staffId)) {
                        allStaff.add(data);
                    } else {
                        // Update staff info
                        allStaff.add(new String[]{
                            staffId, name, role, gender, String.valueOf(age), password
                        });
                        found = true;
                    }
                }
            }
            
            if (!found) {
                System.out.println("Staff member with ID " + staffId + " not found.");
                return;
            }
            
            // Write updated list
            try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_CSV_PATH))) {
                for (String[] staff : allStaff) {
                    writer.println(String.join(",", staff));
                }
                System.out.println("Staff information updated successfully.");
            }
            
        } catch (IOException e) {
            System.out.println("Error updating staff file: " + e.getMessage());
        }
    }

    // View Management Methods
    public void viewHospitalStaffs() {
        view.viewHospitalStaffs(model.getAllStaff());
    }

    // Inventory Management Methods
    public void addMedication(String name, int quantity, int threshold) {
        model.addMedication(name, quantity, threshold);
    }

    public void removeMedication(String name, int quantity) {
        model.removeMedication(name, quantity);
    }

    public void updateMedicationStock(String name, int quantity) {
        model.updateStockLevel(name, quantity);
    }

    public void approveReplenishmentRequest(String requestId) {
        if (model.approveReplenishmentRequest(requestId)) {
            System.out.println("Replenishment request approved successfully.");
        } else {
            System.out.println("Failed to approve replenishment request.");
        }
    }

    // Report Generation Methods
    public void generateStaffReport() {
        view.displayStaffReport(model.getAllStaff());
    }

    public void generateInventoryReport() {
        view.displayInventory(model.inventoryController);
    }

    // System Initialization
    public void initializeSystem(List<HospitalStaff> initialStaff, List<Medication> initialMedications) {
        model.initializeSystem(initialStaff, initialMedications);
        System.out.println("System initialized with default data.");
    }
}