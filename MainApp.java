import java.util.Scanner;
import java.io.IOException;

public class MainApp {
    private static LoginManager loginManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        initialize();

        while (true) {
            System.out.println("\n=== Hospital Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.println("Thank you for using HMS. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void initialize() {
        loginManager = new LoginManager();
        scanner = new Scanner(System.in);

        try {
            loginManager.loadCredentials("SC2002_Assignment/data/StaffList.csv", "SC2002_Assignment/data/PatientList.csv");
        } catch (IOException e) {
            System.out.println("Error loading credentials: " + e.getMessage());
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            System.exit(1);
        }
    }

    private static void handleLogin() {
        System.out.print("Enter Hospital ID: ");
        String hospitalId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (loginManager.login(hospitalId, password)) {
            System.out.println("Login successful!");

            if (password.equals("password")) {
                handlePasswordChange();
            }

            // Print current user info
            User currentUser = loginManager.getCurrentUser();
            System.out.println("Logged in as: " + currentUser.getHospitalID());
            System.out.println("Role: " + currentUser.getUserRole());

            showLoggedInMenu();
        } else {
            System.out.println("Login failed.");
        }
    }

    private static void handlePasswordChange() {
        while (true) {
            System.out.println("\nFirst-time login - You must change your password.");
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            // Add password complexity validation
            if (newPassword.length() < 8) {
                System.out.println("Password must be at least 8 characters long. Please try again.");
                continue;
            }

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }

            if (loginManager.changePassword("password", newPassword)) {
                System.out.println("Password changed successfully!");
                break;
            } else {
                System.out.println("Failed to change password. Please try again.");
            }
        }
    }

    private static void showLoggedInMenu() {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. View Current User Info");
            System.out.println("2. Change Password");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayCurrentUserInfo();
                    break;
                case "2":
                    changeUserPassword();
                    break;
                case "3":
                    loginManager.logout();
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayCurrentUserInfo() {
        User currentUser = loginManager.getCurrentUser();
        if (currentUser != null) {
            System.out.println("Hospital ID: " + currentUser.getHospitalID());
            System.out.println("Role: " + currentUser.getUserRole());
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private static void changeUserPassword() {
        System.out.print("Enter current password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        // Add password complexity validation
        if (newPassword.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        if (loginManager.changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Incorrect current password.");
        }
    }
}
