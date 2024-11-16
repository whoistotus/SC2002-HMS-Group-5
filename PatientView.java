
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
            System.out.println("+-------------------------------------------+");
            System.out.println("|      Doctor ID       |      Date       | Start Time | End Time      |");
            System.out.println("+-------------------------------------------+");
    
            for (DoctorAvailability avail : mergedAvailability) {
                System.out.printf("| %s | %s |   %s   |   %s   |\n",
                    avail.getDoctorID(), avail.getDate(), avail.getStartTime(), avail.getEndTime());
            }
    
            System.out.println("+-------------------------------------------+");
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
                System.out.println("+---------------------------------------------------------------+");
                System.out.println("| Appointment ID | Doctor | Date       | Time  | Status     |");
                System.out.println("+---------------------------------------------------------------+");
        
                for (Appointment appointment : patientAppointments) {
                    System.out.printf("| %-14s | %-10s | %-10s | %-5s | %-10s |\n",
                                      appointment.getAppointmentID(),
                                      appointment.getDoctor().getName(),
                                      appointment.getAppointmentDate(),
                                      appointment.getAppointmentTime(),
                                      appointment.getStatus());
                }
        
                System.out.println("+---------------------------------------------------------------+");
            }
        }

    
    public void viewAppointmentOutcome(AppointmentOutcomeRecord outcome) 
    {
        if (outcome == null) 
        {
            System.out.println("No outcome available for this appointment.");
        } 
        
        else 
        {
            System.out.println("Appointment Outcome:");
            System.out.println("Service: " + outcome.getServiceType());
            System.out.println("Outcome Description: " + outcome.getConsultationNotes());
            System.out.println("Prescription: " + outcome.getMedications());
        }
    }

    public void scheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine();
    
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
    
        System.out.print("Enter appointment time (HH:MM): ");
        String time = scanner.nextLine();
    
        DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
        if (doctor == null) {
            showMessage("Invalid Doctor ID!");
            return;
        }
    
        
        boolean success = appointmentManager.scheduleAppointment(model, doctor, date, time);
        if (success) {
            showMessage("Appointment scheduled successfully with status 'Pending'.");
        } else {
            showMessage("Failed to schedule appointment. The selected time slot is unavailable.");
        }
    }
    
    

    public void rescheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Appointment ID to reschedule: ");
        String appointmentID = scanner.nextLine();
    
        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();
    
        System.out.print("Enter new appointment time (HH:MM): ");
        String newTime = scanner.nextLine();
    
        Appointment appointment = AppointmentsCsvHelper.getAppointmentById(appointmentID);
        if (appointment == null) {
            showMessage("Appointment not found!");
            return;
        }
    
        // Attempt to reschedule
        boolean success = appointmentManager.rescheduleAppointment(appointmentID, newDate, newTime);
        if (success) {
            showMessage("Appointment rescheduled successfully!");
        } else {
            showMessage("Failed to reschedule. The new slot may be unavailable.");
        }
    }

    public void cancelAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();
    
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
    
        System.out.print("Enter appointment time (HH:MM): ");
        String time = scanner.nextLine();
    
        Appointment appointment = model.getAppointmentById(appointmentID);
        if (appointment == null) {
            showMessage("Appointment not found!");
            return;
        }
        
        boolean success = appointmentManager.cancelAppointment(appointmentID);
        if (success) 
        {
            showMessage("Appointment has been successfully canceled.");
        } 
        
        else 
        {
            showMessage("Failed to cancel the appointment. Appointment not found.");
        }
    }

    
    public void showMessage(String message)
    {
        System.out.println(message);
    }
}

