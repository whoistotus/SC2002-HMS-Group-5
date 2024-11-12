package SC2002_Assignment;

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
                newStaff = new Doctor(hospitalID, password, name, gender, age, null);
                break;
            case "pharmacist":
                newStaff = new HospitalStaff(hospitalID, password, "Pharmacist", name, gender, age);
                break;
            default:
                view.displayMessage("Invalid user role specified.");
                return;
        }

        if (model.addStaffMember(newStaff)) {
            view.displayMessage("Staff member added successfully.");
        } else {
            view.displayMessage("Staff with ID " + hospitalID + " already exists.");
        }
    }

    public void removeStaff(String hospitalID) {
        if (model.removeStaffMember(hospitalID)) {
            view.displayMessage("Staff member removed successfully.");
        } else {
            view.displayMessage("Staff member with ID " + hospitalID + " not found.");
        }
    }

    public void updateStaffInfo(String hospitalID, String name, String userRole, String gender, int age) {
        if (model.updateStaffMember(hospitalID, name, userRole, gender, age)) {
            view.displayMessage("Staff information updated successfully.");
        } else {
            view.displayMessage("Staff member with ID " + hospitalID + " not found.");
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
            view.displayMessage("Replenishment request approved successfully.");
        } else {
            view.displayMessage("Failed to approve replenishment request.");
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
        view.displayMessage("System initialized with default data.");
    }
}