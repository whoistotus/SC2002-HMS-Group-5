package SC2002_Assignment;

import java.util.List;

public class AdminView {
    public void displayMessage(String message) {
        System.out.println(message);
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
}