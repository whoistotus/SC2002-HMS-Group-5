
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PatientView
{
    private PatientController controller;
    private PatientModel model;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Scanner scanner = new Scanner(System.in);
    private AppointmentManager appointmentManager = new AppointmentManager();

    public PatientView(PatientModel model)
    {
        this.model = model;
    }
    public void PatientMenu()
    {
        while (true) {
            System.out.println("Patient Menu for " + model.getName());
            System.out.println("1. View Medical Records");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Record");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewMedicalRecord();
                case 2 -> updatePersonalInformation();
                case 3 -> viewAvailableSlots();
                case 4 -> scheduleAppointment();
                case 5 -> rescheduleAppointment();
                case 6 -> cancelAppointment();
                case 7 -> viewScheduledAppointments();
                case 8 -> viewPastAppointmentOutcomeRecord();
                case 9 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public void viewMedicalRecord() {
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(model.getHospitalID());
        
        if (record != null) {
            record.viewMedicalRecord();  // Use the viewMedicalRecord method to display the details
        } else {
            PatientModel patient = PatientListCsvHelper.getPatientById(model.getHospitalID());
            if (patient != null) {
                System.out.println("Patient Information: " + patient);
            } else {
                System.out.println("No information found for patient ID: " + model.getHospitalID());
            }
        }
    }
    

    public void viewAvailableSlots() {
        List<DoctorAvailability> availabilityList = DoctorAvailabilityCsvHelper.loadDoctorAvailability()
                .stream()
                .collect(Collectors.toList());
    
        if (availabilityList.isEmpty()) {
            System.out.println("No available slots at the moment.");
        } else {
            // Sort and merge the availability slots by date and time
            List<DoctorAvailability> mergedAvailability = mergeAvailabilitySlots(availabilityList);
    
            // Display the merged availability
            System.out.println("+-----------------------------------------------+");
            System.out.println("| Doctor ID |    Date    |Start Time|  End Time |");
            System.out.println("+-----------------------------------------------+");
    
            for (DoctorAvailability avail : mergedAvailability) {
                System.out.printf("|    %s   | %s |   %s  |   %s   |\n",
                    avail.getDoctorID(), avail.getDate(), avail.getStartTime(), avail.getEndTime());
            }
    
            System.out.println("+-----------------------------------------------+");
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine(); // Wait for the user to press Enter
    }

    private List<DoctorAvailability> mergeAvailabilitySlots(List<DoctorAvailability> availabilityList) {
            List<DoctorAvailability> mergedList = new ArrayList<>();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            DoctorAvailability current = availabilityList.get(0);

            for (int i = 1; i < availabilityList.size(); i++) {
                DoctorAvailability next = availabilityList.get(i);

                // Check if current and next slots are on the same date and overlap or are adjacent
                if (current.getDate().equals(next.getDate()) &&
                    (LocalTime.parse(current.getEndTime(), timeFormatter).isAfter(LocalTime.parse(next.getStartTime(), timeFormatter)) ||
                    LocalTime.parse(current.getEndTime(), timeFormatter).equals(LocalTime.parse(next.getStartTime(), timeFormatter)))) {
                    
                    // Merge by extending the end time
                    current.setEndTime(maxTime(current.getEndTime(), next.getEndTime()));
                } else {
                    // No overlap, add the current slot to merged list and move to the next
                    mergedList.add(current);
                    current = next;
                }
            }
            // Add the last merged slot
            mergedList.add(current);

            return mergedList;
        }

        private String maxTime(String time1, String time2) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime t1 = LocalTime.parse(time1, timeFormatter);
            LocalTime t2 = LocalTime.parse(time2, timeFormatter);
            return t1.isAfter(t2) ? time1 : time2;
        }
    
        public void viewScheduledAppointments() {
            System.out.println("\n=== View Scheduled Appointments ===");
            System.out.println("Name: " + model.getName() + " (ID: " + model.getHospitalID() + ")");
        
            // Load appointments directly from the CSV
            List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments();
        
            // Filter for accepted appointments for the current doctor
            List<Appointment> patientAppointments = appointments.stream()
                .filter(appointment -> appointment.getPatientID().equals(model.getHospitalID()))
                .distinct()
                .collect(Collectors.toList());
        
            if (patientAppointments.isEmpty()) {
                System.out.println("No upcoming scheduled appointments.");
            } else {
                System.out.println("+----------------------------------------------------------------+");
                System.out.println("| Appointment ID | Doctor        | Date       | Time  |  Status  |");
                System.out.println("+----------------------------------------------------------------+");
        
                for (Appointment appointment : patientAppointments) {
                    System.out.printf("| %-14s | %-10s | %-10s | %-5s | %-8s |\n",
                                      appointment.getAppointmentID(),
                                      appointment.getDoctor().getName(),
                                      appointment.getAppointmentDate(),
                                      appointment.getAppointmentTime(),
                                      appointment.getStatus());
                }
        
                System.out.println("+----------------------------------------------------------------+");
            }
        }

    
        public void viewPastAppointmentOutcomeRecord() {
            System.out.println("\n=== View Past Appointment Outcome Record ===");
        
            // Load all appointment outcome records
            List<AppointmentOutcomeRecord> outcomes = AppointmentOutcomeRecordsCsvHelper.loadAppointmentOutcomes();
        
            // Filter outcomes for the current patient
            List<AppointmentOutcomeRecord> patientOutcomes = outcomes.stream()
                    .filter(outcome -> outcome.getPatientID().equals(model.getHospitalID()))
                    .collect(Collectors.toList());
        
            if (patientOutcomes.isEmpty()) {
                System.out.println("No past appointment outcome records found.");
                return;
            }
        
            // Display the outcomes in a table format
            System.out.println("+---------------------------------------------------------------------------------------------+");
            System.out.println("| Appointment ID | Doctor ID | Date       | Service Type   | Medications      | Notes         |");
            System.out.println("+---------------------------------------------------------------------------------------------+");
        
            for (AppointmentOutcomeRecord outcome : patientOutcomes) {
                String medications = outcome.getMedications().isEmpty() ? "None" : outcome.medicationsToCsv();
                System.out.printf("| %-14s | %-9s | %-10s | %-14s | %-15s | %-12s |\n",
                        outcome.getAppointmentID(),
                        outcome.getDoctorID(),
                        outcome.getDate(),
                        outcome.getServiceType(),
                        medications,
                        outcome.getConsultationNotes());
            }
        
            System.out.println("+---------------------------------------------------------------------------------------------+");
        }

        public void updateContactNumber(String newContactNumber) 
        {
            model.setContactNumber(newContactNumber);
            PatientListCsvHelper.updatePatientContactInfo(model.getHospitalID(), newContactNumber, model.getEmail());
            MedicalRecordsCsvHelper.updatePatientContactInfo(model.getHospitalID(), newContactNumber, model.getEmail());
            showMessage("Contact number updated successfully.");
        }
    
        public void updateEmail(String newEmail) {
            model.setEmail(newEmail);
            PatientListCsvHelper.updatePatientContactInfo(model.getHospitalID(), model.getContactNumber(), newEmail);
            MedicalRecordsCsvHelper.updatePatientContactInfo(model.getHospitalID(), model.getContactNumber(), newEmail);
            showMessage("Email updated successfully.");
        }
    
        public void updatePersonalInformation() {
            Scanner scanner = new Scanner(System.in);
        
            System.out.println("What would you like to update?");
            System.out.println("1. Contact Number");
            System.out.println("2. Email");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        
            switch (choice) {
                case 1:
                    System.out.print("Enter new contact number: ");
                    String newContactNumber = scanner.nextLine();
                    updateContactNumber(newContactNumber);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    updateEmail(newEmail);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        

        public void scheduleAppointment() {
            Scanner scanner = new Scanner(System.in);
        
            // Display doctor availability
            System.out.println("\n=== Doctor Availability ===");
            List<DoctorAvailability> availabilityList = DoctorAvailabilityCsvHelper.loadDoctorAvailability();
        
            if (availabilityList.isEmpty()) {
                System.out.println("No available slots for any doctors at the moment.");
                return;
            }
        
            // Display available slots in a table format
            System.out.println("+-----------------------------------------------------------+");
            System.out.println("| Doctor ID   | Date       | Start Time   | End Time        |");
            System.out.println("+-----------------------------------------------------------+");
            for (DoctorAvailability availability : availabilityList) {
                System.out.printf("| %-11s | %-10s | %-12s | %-14s |\n",
                        availability.getDoctorID(),
                        availability.getDate(),
                        availability.getStartTime(),
                        availability.getEndTime());
            }
            System.out.println("+-----------------------------------------------------------+");
        
            // Get user input for scheduling
            System.out.print("\nEnter Doctor ID: ");
            String doctorID = scanner.nextLine();
        
            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
        
            System.out.print("Enter appointment time (HH:MM): ");
            String time = scanner.nextLine();
        
            // Validate doctor ID and availability
            DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
            if (doctor == null) {
                showMessage("Invalid Doctor ID! Please try again.");
                return;
            }
        
            // Check availability for the selected doctor
            List<String> availableSlots = DoctorAvailabilityCsvHelper.getAvailableSlots(doctorID, date);
            if (!availableSlots.contains(time)) {
                showMessage("The selected time slot is unavailable. Please try again.");
                return;
            }
        
            // Schedule the appointment
            boolean success = appointmentManager.scheduleAppointment(model, doctor, date, time);
            if (success) {
                // Block the booked time slot and update the doctor's availability
                boolean updated = DoctorAvailabilityCsvHelper.removeSlot(doctorID, date, time);
                if (updated) {
                    showMessage("Appointment scheduled successfully with status 'Pending'. Doctor availability updated.");
                } else {
                    showMessage("Appointment scheduled successfully, but failed to update doctor availability in CSV.");
                }
            } else {
                showMessage("Failed to schedule the appointment. Please try again.");
            }
        }
        
    public void rescheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Appointment ID to reschedule: ");
        String appointmentID = scanner.nextLine();

        Appointment appointment = AppointmentsCsvHelper.getAppointmentById(appointmentID);
        if (appointment == null) {
            showMessage("Appointment not found!");
            return;
        }
    
        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();
    
        System.out.print("Enter new appointment time (HH:MM): ");
        String newTime = scanner.nextLine();

        String doctorID = appointment.getDoctorID();
        String oldDate = appointment.getAppointmentDate();
        String oldTime = appointment.getAppointmentTime();
    
        // Attempt to reschedule
        boolean success = appointmentManager.rescheduleAppointment(appointmentID, newDate, newTime);
        if (success) {
            // Add the original time slot back to availability
            boolean oldSlotAdded = DoctorAvailabilityCsvHelper.addSlot(doctorID, oldDate, oldTime);
            // Remove the new time slot from availability
            boolean newSlotRemoved = DoctorAvailabilityCsvHelper.removeSlot(doctorID, newDate, newTime);
    
            if (oldSlotAdded && newSlotRemoved) {
                showMessage("Appointment rescheduled successfully! Availability updated.");
            } else {
                showMessage("Appointment rescheduled successfully, but failed to update availability.");
            }
        } else {
            showMessage("Failed to reschedule. The new slot may be unavailable.");
        }
    }

    public void cancelAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
    
        Appointment appointment = model.getAppointmentById(appointmentID);
        if (appointment == null) {
            showMessage("Appointment not found!");
            return;
        }

        String doctorID = appointment.getDoctorID();
        String date = appointment.getAppointmentDate();
        String time = appointment.getAppointmentTime();
        
        boolean success = appointmentManager.cancelAppointment(appointmentID);
        if (success) {
            // Add the canceled time slot back to availability
            boolean slotAdded = DoctorAvailabilityCsvHelper.addSlot(doctorID, date, time);
    
            if (slotAdded) {
                showMessage("Appointment canceled successfully! Time slot added back to availability.");
            } else {
                showMessage("Appointment canceled successfully, but failed to update availability.");
            }
        } else {
            showMessage("Failed to cancel the appointment. Please try again.");
        }
    }

    
    public void showMessage(String message)
    {
        System.out.println(message);
    }
}

