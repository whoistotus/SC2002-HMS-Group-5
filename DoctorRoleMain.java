import java.util.Scanner;

public class DoctorRoleMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize the model, view, and controller
        DoctorModel doctorModel = new DoctorModel("D001", "password", "doctor", "Dr. Smith", "Cardiology");
        DoctorView doctorView = new DoctorView(doctorModel);
        AppointmentManager appointmentManager = new AppointmentManager();
        DoctorController doctorController = new DoctorController(doctorModel, doctorView, appointmentManager);

        // Menu for test cases
        while (true) {
            System.out.println("\n--- Doctor Role Test Cases ---");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. Set Availability for Appointments");
            System.out.println("4. View Personal Schedule");
            System.out.println("5. Accept Appointment Request");
            System.out.println("6. Decline Appointment Request");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. View Doctor Availability");
            System.out.println("9. Exit");
            System.out.print("Choose a test case to execute (1-9): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> testViewPatientMedicalRecords(doctorView);
                case 2 -> testUpdatePatientMedicalRecords(doctorView);
                case 3 -> testSetAvailability(doctorController, scanner);
                case 4 -> testViewPersonalSchedule(doctorView);
                case 5 -> testAcceptAppointment(doctorView);
                case 6 -> testDeclineAppointment(doctorView);
                case 7 -> testRecordAppointmentOutcome(doctorView);
                case 8 -> testViewDoctorAvailability(doctorView);
                case 9 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void testViewPatientMedicalRecords(DoctorView doctorView) {
        System.out.println("\n--- Test Case 9: View Patient Medical Records ---");
        doctorView.viewPatientMedicalRecords();
    }

    private static void testUpdatePatientMedicalRecords(DoctorView doctorView) {
        System.out.println("\n--- Test Case 10: Update Patient Medical Records ---");
        doctorView.updatePatientMedicalRecords();
    }

    private static void testSetAvailability(DoctorController doctorController, Scanner scanner) {
        System.out.println("\n--- Test Case 12: Set Availability for Appointments ---");
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter start time (HH:MM): ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end time (HH:MM): ");
        String endTime = scanner.nextLine();

        doctorController.setAvailability(date, startTime, endTime);
    }

    private static void testViewPersonalSchedule(DoctorView doctorView) {
        System.out.println("\n--- Test Case 11: View Personal Schedule ---");
        doctorView.viewPersonalSchedule();
    }

    private static void testAcceptAppointment(DoctorView doctorView) {
        System.out.println("\n--- Test Case 13: Accept Appointment Request ---");
        doctorView.acceptAppointment();
    }

    private static void testDeclineAppointment(DoctorView doctorView) {
        System.out.println("\n--- Test Case 13: Decline Appointment Request ---");
        doctorView.declineAppointment();
    }

    private static void testRecordAppointmentOutcome(DoctorView doctorView) {
        System.out.println("\n--- Test Case 15: Record Appointment Outcome ---");
        doctorView.recordAppointmentOutcome();
    }

    private static void testViewDoctorAvailability(DoctorView doctorView) {
        System.out.println("\n--- Additional Test: View Doctor Availability ---");
        doctorView.viewDoctorAvailability();
    }
}
