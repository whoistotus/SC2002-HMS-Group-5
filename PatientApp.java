import java.util.Scanner;

public class PatientApp{

    public static void main(String[] args) {
        // Initialize necessary components for testing
        PatientModel patient = new PatientModel("P1001", "password", "Patient", "Alice", "1990-01-01", "Female", "1234567890", "alice@example.com", "O+");
        boolean success = PatientListCsvHelper.addPatientToCsv(patient);

        if (success) {
            System.out.println("New patient added successfully!");
        } else {
            System.out.println("Failed to add the patient to the system.");
        }
        PatientView view = new PatientView();
        AppointmentManager appointmentManager = new AppointmentManager();
        PatientController controller = new PatientController(patient, view, appointmentManager);

        // Simulate the menu for testing
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Test Cases Menu ---");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. Exit");
            System.out.print("Enter the test case number: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Test Case 1: View Medical Record
                    controller.viewMedicalRecord(patient.getHospitalID());
                    break;

                case 2:
                    // Test Case 2: Update Personal Information
                    controller.updatePersonalInformation();
                    break;

                case 3:
                    // Test Case 3: View Available Appointment Slots
                    //System.out.print("Enter Doctor ID: ");
                    //String doctorId = scanner.nextLine();
                    //System.out.print("Enter Date (YYYY-MM-DD): ");
                    //String date = scanner.nextLine();
                    controller.viewAvailableSlots();
                    break;

                case 4:
                    // Test Case 4: Schedule an Appointment
                    System.out.print("Enter Doctor ID: ");
                    String docId = scanner.nextLine();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String appDate = scanner.nextLine();
                    System.out.print("Enter Time (HH:MM): ");
                    String appTime = scanner.nextLine();
                    controller.scheduleAppointment(docId, appDate, appTime);
                    break;

                case 5:
                    // Test Case 5: Reschedule an Appointment
                    System.out.print("Enter Appointment ID to reschedule: ");
                    String appointmentId = scanner.nextLine();
                    System.out.print("Enter new Date (YYYY-MM-DD): ");
                    String newDate = scanner.nextLine();
                    System.out.print("Enter new Time (HH:MM): ");
                    String newTime = scanner.nextLine();
                    controller.rescheduleAppointment(appointmentId, newDate, newTime);
                    break;

                case 6:
                    // Test Case 6: Cancel an Appointment
                    System.out.print("Enter Appointment ID to cancel: ");
                    String cancelAppId = scanner.nextLine();
                    controller.cancelAppointment(cancelAppId);
                    break;

                case 7:
                    // Test Case 7: View Scheduled Appointments
                    controller.viewScheduledAppointments();
                    break;

                case 8:
                    System.out.println("Exiting test cases...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}