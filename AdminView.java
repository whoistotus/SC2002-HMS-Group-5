package SC2002_Assignment;

import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.ArrayList;


public class AdminView {
    private InventoryController inventoryController;
    private AppointmentManager appointmentManager;
    private Medication medication;
    private List<HospitalStaff> staffs = new ArrayList<>();

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
        System.out.println("5 - Approve Replenishment Request");
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
        for (HospitalStaff hospitalStaff : staffs)
        {
            if (hospitalStaff.getHospitalID().equalsIgnoreCase(hospitalID))
            {
                return true;
            }
        }
        return false;
    }

    public void manageStaffList()
    {
        System.out.println("=======Staff List Management=======");
        System.out.println("1 - Add Staff");
        System.out.println("2 - Remove Staff");
        System.out.println("3 - Update Staff's Details");
        System.out.println("4 - Return to main menu.");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice)
        {
            case 1:
                try
                {
                    System.out.println("Please enter your Hospital ID: ");
                    String hospitalID = sc.nextLine();

                    if (HospitalStaff.)
                }
        }
    }
}