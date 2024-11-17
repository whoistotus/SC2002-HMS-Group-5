import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static LoginManager loginManager;
    private static Scanner scanner;
    private static AdminView adminView;

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
            loginManager.loadCredentials("data/StaffList.csv", "data/PatientList.csv");
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
            User currentUser = loginManager.getCurrentUser();
            System.out.println("Login successful!");
            System.out.println("Welcome, " + currentUser.getHospitalID() + "!");
            System.out.println("Role: " + currentUser.getUserRole());

            if (password.equals("password")) {
                handlePasswordChange();
            }

            // Show role-specific menu
            showRoleMenu(currentUser, password); // Pass password for constructing DoctorModel
        } else {
            System.out.println("Login failed. Invalid Hospital ID or Password.");
        }
    }

    private static void handlePasswordChange() {
        while (true) {
            System.out.println("\nFirst-time login - You must change your password.");
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

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

    private static void showRoleMenu(User currentUser, String password) {
        String role = currentUser.getUserRole().toLowerCase();
        

        switch (role) {
            case "doctor":
                /*DoctorModel doctorModel = constructDoctorModel(currentUser.getHospitalID(), password);
                if (doctorModel != null) {
                    DoctorView doctorView = new DoctorView(doctorModel);
                    doctorView.DoctorMenu();
                } else {
                    System.out.println("Error: Could not find doctor details in the system.");
                }*/
                break;

            case "administrator":
                InventoryController inventoryController = new InventoryController();
                adminView = new AdminView(inventoryController);
                adminView.showMenu();
                break;

            case "pharmacist":
                PharmacistView pharmacistView = new PharmacistView(currentUser.getHospitalID());
                pharmacistView.start(); // Launch the pharmacist menu
                break;

            case "patient":
                PatientModel patientModel = constructPatientModel(currentUser.getHospitalID(), password);
                if (patientModel != null) {
                    PatientView patientView = new PatientView(patientModel);
                    patientView.PatientMenu();
                } else {
                    System.out.println("Error: Could not find patient details in the system.");
                }
                break;

            default:
                System.out.println("Role not supported yet. Please contact the administrator.");
        }
    }

    private static DoctorModel constructDoctorModel(String hospitalId, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/StaffList.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values[0].trim().equals(hospitalId) && values[2].trim().equalsIgnoreCase("Doctor")) {
                    String name = values[1].trim();
                    String specialization = values[3].trim();
                    return new DoctorModel(hospitalId, password, "Doctor", name, specialization);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff list: " + e.getMessage());
        }
        return null; // Return null if no match is found
    }

    private static PatientModel constructPatientModel(String hospitalId, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/PatientList.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values[0].trim().equals(hospitalId)) {
                    String name = values[1].trim();
                    String dob = values[2].trim();
                    String gender = values[3].trim();
                    String bloodType = values[4].trim();
                    String email = values[5].trim();
                    String contactNumber = values[6].trim();
                    return new PatientModel(hospitalId, name, dob, gender, bloodType, email, contactNumber, password, "Patient");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading patient list: " + e.getMessage());
        }
        return null; // Return null if no match is found
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
