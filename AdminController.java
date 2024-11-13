package SC2002_Assignment;
import java.util.List;

public class AdminController {
    private AdminModel model;
    private AdminView view;

    public AdminController(AdminModel model, AdminView view) {
        this.model = model;
        this.view = view;
    }

    // Staff Management Methods
    public void addStaff(String hospitalID, String password, String userRole, String name, String gender, int age) {
        HospitalStaff newStaff;
        switch (userRole.toLowerCase()) {
            case "doctor":
                newStaff = new HospitalStaff(hospitalID, password, "Doctor", name, gender, age);
                break;
            case "pharmacist":
                newStaff = new HospitalStaff(hospitalID, password, "Pharmacist", name, gender, age);
                break;
            default:
                System.out.println("Invalid user role specified.");
                return;
        }

        if (model.addStaffMember(newStaff)) {
            System.out.println("Staff member added successfully.");
        } else {
            System.out.println("Staff with ID " + hospitalID + " already exists.");
        }
    }

    public void removeStaff(String hospitalID) {
        if (model.removeStaffMember(hospitalID)) {
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member with ID " + hospitalID + " not found.");
        }
    }

    public void updateStaffInfo(String hospitalID, String name, String userRole, String gender, int age) {
        if (model.updateStaffMember(hospitalID, name, userRole, gender, age)) {
            System.out.println("Staff information updated successfully.");
        } else {
            System.out.println("Staff member with ID " + hospitalID + " not found.");
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

    public void approveReplenishmentRequest(int requestId) {
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