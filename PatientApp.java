import java.util.Scanner;

public class PatientApp{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize necessary components for testing
        PatientModel model = new PatientModel("P1004", "Amy", "1990-01-01", "Female", "O+", "amy@example.com", "12345678", "Password", "Patient");
        PatientView view = new PatientView(model);
        AppointmentManager appointmentManager = new AppointmentManager();
        PatientController controller = new PatientController(model, view, appointmentManager);
        
        boolean success = PatientListCsvHelper.addPatientToCsv(model);

        if (success) {
            System.out.println("New patient added successfully!");
        } else {
            System.out.println("Failed to add the patient to the system.");
        }

        while (true) {
            System.out.println("\n--- Patient Role Test Cases ---");
            System.out.println("1. View Medical Records");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Record");
            System.out.println("9. Exit");
            System.out.print("Choose a test case to execute (1-9): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> testViewMedicalRecords(view);
                case 2 -> testUpdatePersonalInformation(controller);
                case 3 -> testViewAvailableSlots(view);
                case 4 -> testScheduleAppointment(view);
                case 5 -> testRescheduleAppointment(view);
                case 6 -> testCancelAppointment(view);
                case 7 -> testViewScheduledAppointments(view);
                //case 8 -> testViewPastAppointmentOutcomeRecord(view);
                case 9 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void testViewMedicalRecords(PatientView view) {
        System.out.println("\n--- Test Case 1: View Medical Records ---");
        view.viewMedicalRecord();
    }

    private static void testUpdatePersonalInformation(PatientController controller) {
        System.out.println("\n--- Test Case 2: Update Personal Information ---");
        controller.updatePersonalInformation();
    }

    private static void testViewAvailableSlots(PatientView view) {
        System.out.println("\n--- Test 3: View Available Slots ---");
        view.viewAvailableSlots();
    }

    private static void testScheduleAppointment(PatientView view)
    {
        System.out.println("\n--- Test 4: Schedule Appointment ---");
        view.scheduleAppointment();
    }

    private static void testRescheduleAppointment(PatientView view)
    {
        System.out.println("\n--- Test 5: Reschedule Appointment ---");
        view.rescheduleAppointment();
    }

    private static void testCancelAppointment(PatientView view)
    {
        System.out.println("\n--- Test 6: Cancel Appointment ---");
        view.cancelAppointment();
    }

    private static void testViewScheduledAppointments(PatientView view)
    {
        System.out.println("\n--- Test 7: View Scheduled Appointments ---");
        view.viewScheduledAppointments();
    }


}