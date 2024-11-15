import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminModel {
    private List<HospitalStaff> staffList;
    protected InventoryController inventoryController;
    private AppointmentManager appointmentManager;

    public AdminModel() {
        this.staffList = new ArrayList<>();
        this.inventoryController = new InventoryController();
        this.appointmentManager = new AppointmentManager();
    }

    // Staff Management Methods
    public boolean addStaffMember(HospitalStaff staff) {
        if (findStaffById(staff.getHospitalID()) != null) {
            return false;
        }
        return staffList.add(staff);
    }

    public boolean removeStaffMember(String hospitalID) {
        return staffList.removeIf(staff -> staff.getHospitalID().equals(hospitalID));
    }

    public boolean updateStaffMember(String hospitalID, String name, String userRole, String gender, int age) {
        HospitalStaff staff = findStaffById(hospitalID);
        if (staff != null) {
            staff.setName(name);
            staff.setuserRole(userRole);
            staff.setGender(gender);
            staff.setAge(age);
            return true;
        }
        return false;
    }

    // Getters for staff information
    public List<HospitalStaff> getAllStaff() {
        return new ArrayList<>(staffList);
    }

    public List<HospitalStaff> getStaffByRole(String role) {
        return staffList.stream()
                .filter(staff -> staff.getuserRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    public HospitalStaff findStaffById(String hospitalID) {
        return staffList.stream()
                .filter(staff -> staff.getHospitalID().equals(hospitalID))
                .findFirst()
                .orElse(null);
    }

    // Inventory Management Methods
    public void addMedication(String name, int quantity, int threshold) {
        inventoryController.addMedication(name, quantity, threshold);
    }

    public void removeMedication(String name, int quantity) {
        inventoryController.removeMedication(name, quantity);
    }

    public void updateStockLevel(String name, int quantity) {
        inventoryController.updateStockLevel(name, quantity);
    }

    public boolean approveReplenishmentRequest(String requestId) {
        return inventoryController.approveReplenishmentRequest(requestId);
    }

    // System Initialization
    public void initializeSystem(List<HospitalStaff> initialStaff, List<Medication> initialMedications) {
        for (HospitalStaff staff : initialStaff) {
            staffList.add(staff);
        }

        for (Medication med : initialMedications) {
            inventoryController.addMedication(med.getName(), med.getQuantity(), med.getThreshold());
        }
    }
}