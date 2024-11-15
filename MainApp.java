package SC2002_Assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;




public class MainApp
{
    private static Map<String, User> users = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        initialiseUsers();

        while (true)
        {
            System.out.println("===========================================");
            System.out.println("Welcome to the Hospital Management System!");
            System.out.println("1 - Login");
            System.out.println("2 - Quit");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1)
            {
                login();
            }
            else if (choice == 2)
            {
                System.out.println("System quiting...");
            }
            else
            {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void login()
    {
       System.out.println("Please enter your hospital ID."); 
       String hospitalID = sc.nextLine();
       System.out.println("Please enter your password.");
       String password = sc.nextLine();

       User user = users.get(hospitalID);

       if (user != null && user.getPassword().equals(password))
       {
        System.out.println("Login success!");
        if (password.equals("password")) //if password is still default, prompt to change
        {
            System.out.println("You are still using the default password. Please change your password.");
            changePassword(user);
        }
        //showMenu(user);
       }
       else //hospitalID or password is incorrect
       {
            System.out.println("The hospital ID or password is invalid. Please try again.");
       }
    }

    private static void changePassword(User user)
    {
        System.out.println("Enter new password: ");
        String newPassword = sc.nextLine();
        user.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }

    private static void initialiseUsers()
    {
        users.put("admin1", new User("admin1", "password", "Administrator"));
        users.put("doctor1", new User("doctor1", "password", "Doctor"));
        users.put("patient1", new User("patient1", "password", "Patient"));
        users.put("pharmacist1", new User("pharmacist1", "password", "Pharmacist"));
    }


    private static void showMenu() //brings the user to the login menu
    {
        
    }
} 