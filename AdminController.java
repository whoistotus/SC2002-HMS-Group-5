
import java.io.IOException;
import java.util.List;

public class AdminController {
    private AdminModel model;
    private AdminView view;

    public AdminController(AdminModel model, AdminView view) {
        this.model = model;
        this.view = view;
    }

    // Staff Management Methods
    public void addStaff(String hospitalID, String password, String userRole, 
                        String name, String gender, int age) {
        try {
            HospitalStaff newStaff = new HospitalStaff(hospitalID, password, userRole, name, gender, age);
            if (model.addStaffMember(newStaff)) {
                AdminCSVWriter.addStaffToCSV(newStaff);
                System.out.println("Staff member added successfully.");
            } else {
                System.out.println("Staff with ID " + hospitalID + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error writing to staff file: " + e.getMessage());
        }
    }

    public void removeStaff(String hospitalID) {
        try {
            if (model.removeStaffMember(hospitalID)) {
                AdminCSVWriter.removeStaffFromCSV(hospitalID);
                System.out.println("Staff member removed successfully.");
            } else {
                System.out.println("Staff member with ID " + hospitalID + " not found.");
            }
        } catch (IOException e) {
            System.out.println("Error updating staff file: " + e.getMessage());
        }
    }

    public void updateStaffInfo(String hospitalID, String name, String userRole, 
                            String gender, int age) {
        try {
            if (model.updateStaffMember(hospitalID, name, userRole, gender, age)) {
                AdminCSVWriter.updateStaffInCSV(hospitalID, name, userRole, gender, age);
                System.out.println("Staff information updated successfully.");
            } else {
                System.out.println("Staff member with ID " + hospitalID + " not found.");
            }
        } catch (IOException e) {
            System.out.println("Error updating staff file: " + e.getMessage());
        }
    }

    // View Management Methods
    public void viewHospitalStaffs() {
        view.viewHospitalStaffs(model.getAllStaff());
    }

    public void viewScheduledAppointments(String patientID) {
        view.viewScheduledAppointments(model.getAppointmentsForPatient(patientID));
    }

    public void viewAllAppointments() {
        view.viewScheduledAppointments(model.getAllAppointments());
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

    public void viewReplenishmentRequests() {
        view.viewReplenishmentRequests(model.getReplenishmentRequests());
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