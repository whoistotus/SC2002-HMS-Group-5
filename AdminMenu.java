package SC2002_Assignment;
import java.util.InputMismatchException;
import java.util.Scanner;



public class AdminMenu 
{
    private InventoryController inventoryController;
    private AppointmentManager appointmentManager;

    Scanner sc = new Scanner(System.in);

    public AdminMenu(InventoryController inventoryController)
    {
        this.inventoryController = inventoryController;
        this.appointmentManager = appointmentManager;
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
        System.out.println("7 - Appointment Details");
        System.out.println("6 - Logout");
        int choice = sc.nextInt();
        sc.nextLine();
        
        switch (choice)
        {
            case 1:
                showInventory();
            
            case 2:
                manageMedicationInventory();

            case 7:
                //viewAppointments();


            

                

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
                    //setThreshold();
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
            System.out.println("Enter Medicine Name for updating: ");
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


    public void viewAppointments(String patientID)
    {
        System.out.println("===== Appointment Details ========");
    }
}
