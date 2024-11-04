package SC2002_Assignment;
import java.util.Scanner;


public class AdminMenu 
{
    Scanner sc = new Scanner(System.in);
    public void showMenu()
    {
        System.out.println("======================");
        System.out.println("Administrator Menu: ");
        System.out.println("1 - Show Inventory");
        System.out.println("2 - Show Low Stocks");
        System.out.println("3 - Add Inventory");
        System.out.println("4 - Remove Inventory");
        System.out.println("5 - Update Inventory");
        System.out.println("6 - Approve Replenishment Request");
        int choice = sc.nextInt();
        
        switch (choice)
        {
            case 1:
                displayInventory();
            
            case 2:
                

        }
    }


    public void displayInventory()
    {
     
    }
}
