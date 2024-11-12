package SC2002_Assignment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DoctorApp {

    private static List<Doctor> doctors = new ArrayList<>();
    private static List<Patient> patients = new ArrayList<>();
    private static List<Appointment> appointments = new ArrayList<>();
    private static Doctor currentDoctor;

    public static void main(String[] args) {
        // Initialize test data
        initializeData();

        Scanner scanner = new Scanner(System.in);

        // Login simulation
        System.out.println("Welcome to the Hospital Management System.");
        System.out.print("Enter your Doctor ID: ");
        String doctorId = scanner.nextLine();

        System.out.print("Enter your Password: ");
        String password = scanner.nextLine(); // Note: For simplicity, password check is simulated

        if (login(doctorId, password)) {
            System.out.println("Login successful! Welcome Dr. " + currentDoctor.getName());
            boolean exit = false;
            while (!exit) {
                System.out.println("\nDoctor Menu:");
                System.out.println("1. View Personal Schedule");
                System.out.println("2. Set Availability");
                System.out.println("3. Accept Appointment");
                System.out.println("4. Decline Appointment");
                System.out.println("5. Record Appointment Outcome");
                System.out.println("6. Logout");

                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        currentDoctor.viewPersonalSchedule();
                        break;
                    case 2:
                        System.out.print("Enter availability date (yyyy-MM-dd): ");
                        String dateInput = scanner.nextLine();
                        try {
                            // Parse the input as LocalDate
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.parse(dateInput, formatter);
                            
                            // Convert LocalDate to java.util.Date
                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            
                            currentDoctor.setAvailability(date);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter Appointment ID to accept: ");
                        String acceptId = scanner.nextLine();
                        Appointment toAccept = findAppointmentById(acceptId);
                        if (toAccept != null) {
                            currentDoctor.acceptAppointment(toAccept);
                        } else {
                            System.out.println("Appointment not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter Appointment ID to decline: ");
                        String declineId = scanner.nextLine();
                        Appointment toDecline = findAppointmentById(declineId);
                        if (toDecline != null) {
                            currentDoctor.declineAppointment(toDecline);
                        } else {
                            System.out.println("Appointment not found.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter Appointment ID to record outcome: ");
                        String outcomeId = scanner.nextLine();
                        Appointment toRecord = findAppointmentById(outcomeId);
                        if (toRecord != null) {
                            System.out.print("Enter Service Type: ");
                            String serviceType = scanner.nextLine();
                            List<Medication> medicines = new ArrayList<>();
                            System.out.print("Enter Medicine name (type 'done' to finish): ");
                            while (true) {
                                String medName = scanner.nextLine();
                                if ("done".equalsIgnoreCase(medName)) break;
                                System.out.print("Enter Medicine status: ");
                                String medStatus = scanner.nextLine();
                                medicines.add(new Medication(medName, medStatus));
                                System.out.print("Enter another Medicine name (or 'done' to finish): ");
                            }
                            System.out.print("Enter Consultation Notes: ");
                            String notes = scanner.nextLine();
                            currentDoctor.recordAppointmentOutcome(toRecord, serviceType, medicines, notes);
                        } else {
                            System.out.println("Appointment not found.");
                        }
                        break;
                    case 6:
                        System.out.println("Logging out...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid Doctor ID or Password.");
        }

        scanner.close();
    }

    private static boolean login(String doctorId, String password) {
        // For simplicity, we're just checking the doctor ID and a default password
        for (Doctor doctor : doctors) {
            if (doctor.getUserId().equals(doctorId) && "password".equals(password)) {
                currentDoctor = doctor;
                return true;
            }
        }
        return false;
    }

    private static Appointment findAppointmentById(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }    

    private static void initializeData() {
        // Create sample doctor
        Doctor doctor = new Doctor("D001", "Alice Smith");
        doctors.add(doctor);

        // Create sample patients
        Patient patient1 = new Patient("P001", "John Doe", "O+");
        Patient patient2 = new Patient("P002", "Jane Roe", "A+");
        patients.add(patient1);
        patients.add(patient2);

        // Create sample appointments
        Appointment appointment1 = new Appointment("A001", doctor, patient1, new Date());
        Appointment appointment2 = new Appointment("A002", doctor, patient2, new Date());
        appointments.add(appointment1);
        appointments.add(appointment2);

        // Add appointments to doctor's schedule using the new method
        doctor.addAppointment(appointment1);
        doctor.addAppointment(appointment2);
    }
}
