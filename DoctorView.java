

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DoctorView {

    private DoctorModel doctorModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Scanner scanner = new Scanner(System.in);

    public DoctorView(DoctorModel doctorModel) {
        this.doctorModel = doctorModel;
    }

    public void DoctorMenu() {
        while (true) {
            System.out.println("Doctor Menu for " + doctorModel.getName());
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. Set Availability");
            System.out.println("4. View Personal Scheduled Appointments");
            System.out.println("5. Accept Appointment");
            System.out.println("6. Decline Appointment");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. View Doctor Availability");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> setAvailability();
                case 4 -> viewScheduledAppointments();
                case 5 -> acceptAppointment();
                case 6 -> declineAppointment();
                case 7 -> recordAppointmentOutcome();
                case 8 -> viewDoctorAvailability();
                case 9 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void viewScheduledAppointments() {
        System.out.println("\n=== View Scheduled Appointments ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");
    
        // Load appointments directly from the CSV
        List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments();
    
        // Filter for accepted appointments for the current doctor
        List<Appointment> acceptedAppointments = appointments.stream()
            .filter(appointment -> appointment.getDoctorID().equals(doctorModel.getHospitalID()) &&
                                   appointment.getStatus() == Appointment.AppointmentStatus.ACCEPTED)
            .distinct()
            .collect(Collectors.toList());
    
        if (acceptedAppointments.isEmpty()) {
            System.out.println("No upcoming accepted appointments.");
        } else {
            System.out.println("+---------------------------------------------------------------+");
            System.out.println("| Appointment ID | Patient ID | Date       | Time  | Status     |");
            System.out.println("+---------------------------------------------------------------+");
    
            for (Appointment appointment : acceptedAppointments) {
                System.out.printf("| %-14s | %-10s | %-10s | %-5s | %-10s |\n",
                                  appointment.getAppointmentID(),
                                  appointment.getPatientID(),
                                  appointment.getAppointmentDate(),
                                  appointment.getAppointmentTime(),
                                  appointment.getStatus());
            }
    
            System.out.println("+---------------------------------------------------------------+");
        }
    }
    
    
    

    public void viewPatientMedicalRecords() {
        System.out.print("Enter Patient ID to view record: ");
        String patientID = scanner.nextLine();
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
    
        if (record != null) {
            record.viewMedicalRecord();  // Use the viewMedicalRecord method to display the details
        } else {
            PatientModel patient = PatientListCsvHelper.getPatientById(patientID);
            if (patient != null) {
                System.out.println("Patient Information: " + patient);
            } else {
                System.out.println("No information found for patient ID: " + patientID);
            }
        }
    }
    

    public void updatePatientMedicalRecords() {
        System.out.print("Enter Patient ID to update record: ");
        String patientID = scanner.nextLine();
        
        // Retrieve the medical record using the patient ID
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
        if (record != null) {
            System.out.print("Enter new diagnosis: ");
            String diagnosis = scanner.nextLine();
            System.out.print("Enter new prescription: ");
            String prescription = scanner.nextLine();
            System.out.print("Enter new treatment plan: ");
            String treatment = scanner.nextLine();
    
            // Update the medical record directly
            record.addNewDiagnosis(diagnosis);
            record.addNewPrescription(prescription);
            record.addNewTreatment(treatment);
    
            // Save the updated record back to the CSV
            MedicalRecordsCsvHelper.saveMedicalRecord(record);
            System.out.println("Medical record updated successfully.");
        } else {
            System.out.println("No record found for Patient ID: " + patientID);
        }
    }
    
    public void setAvailability() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter start time (HH:MM): ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end time (HH:MM): ");
        String endTime = scanner.nextLine();

        DoctorAvailability availability = new DoctorAvailability(doctorModel.getHospitalID(), date, startTime, endTime);
        List<DoctorAvailability> availabilityList = new ArrayList<>(doctorModel.getAvailability());
        availabilityList.add(availability);
        doctorModel.setAvailability(availabilityList);
        DoctorAvailabilityCsvHelper.saveDoctorAvailability(availabilityList);
        System.out.println("Availability set successfully.");
    }

    public void acceptAppointment() {
        System.out.println("\n=== Accept Appointment Request ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");
        System.out.println("Here are your pending appointments:");
    
        List<Appointment> pendingAppointments = AppointmentsCsvHelper.getPendingAppointmentsForDoctor(doctorModel.getHospitalID());
    
        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments.");
        } else {
            // Display pending appointments
            System.out.println("+-------------------------------------------+");
            System.out.println("| Appointment ID | Patient ID | Time  | Status |");
            System.out.println("+-------------------------------------------+");
    
            for (Appointment appointment : pendingAppointments) {
                System.out.printf("| %-14s | %-10s | %-5s | %-7s |\n",
                    appointment.getAppointmentID(),
                    appointment.getPatient().getHospitalID(),
                    appointment.getAppointmentTime(),
                    appointment.getStatus());
            }
    
            System.out.println("+-------------------------------------------+");
    
            // Prompt the user to enter an appointment ID to accept
            System.out.print("Enter Appointment ID to accept: ");
            String appointmentIDToAccept = scanner.nextLine();
            Appointment appointmentToAccept = AppointmentsCsvHelper.getAppointmentById(appointmentIDToAccept);
    
            if (appointmentToAccept != null && appointmentToAccept.getStatus() == Appointment.AppointmentStatus.PENDING) {
                appointmentToAccept.setStatus(Appointment.AppointmentStatus.ACCEPTED);
                AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToAccept, Appointment.AppointmentStatus.ACCEPTED);
                doctorModel.addAppointment(appointmentToAccept); // Add to personal schedule
    
                // Block off the time in availability
                blockTimeInAvailability(appointmentToAccept);
                System.out.println("Appointment accepted: " + appointmentToAccept.getAppointmentID());
            } else {
                System.out.println("Appointment not found or already processed.");
            }
        }
    }
    private void blockTimeInAvailability(Appointment appointment) {
        List<DoctorAvailability> availabilityList = doctorModel.getAvailability();
        List<DoctorAvailability> updatedAvailability = new ArrayList<>();
    
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime appointmentStartTime = LocalTime.parse(appointment.getAppointmentTime(), timeFormatter);
        LocalTime appointmentEndTime = appointmentStartTime.plusHours(1); // Assuming 1-hour appointment
    
        for (DoctorAvailability availability : availabilityList) {
            if (!availability.getDate().equals(appointment.getAppointmentDate())) {
                updatedAvailability.add(availability); // Different date, keep as is
                continue;
            }
    
            LocalTime availableStart = LocalTime.parse(availability.getStartTime(), timeFormatter);
            LocalTime availableEnd = LocalTime.parse(availability.getEndTime(), timeFormatter);
    
            // Adjust availability based on overlap
            if (availableStart.isBefore(appointmentStartTime)) {
                updatedAvailability.add(new DoctorAvailability(
                    availability.getDoctorID(),
                    availability.getDate(),
                    availability.getStartTime(),
                    appointment.getAppointmentTime() // end time adjusted to appointment start
                ));
            }
            if (availableEnd.isAfter(appointmentEndTime)) {
                updatedAvailability.add(new DoctorAvailability(
                    availability.getDoctorID(),
                    availability.getDate(),
                    appointmentEndTime.format(timeFormatter),
                    availability.getEndTime()
                ));
            }
        }
    }
    
    
    public void declineAppointment() {
        System.out.println("\n=== Decline Appointment Request ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");
        System.out.println("Here are your pending appointments:");
    
        List<Appointment> pendingAppointments = AppointmentsCsvHelper.getPendingAppointmentsForDoctor(doctorModel.getHospitalID());
    
        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments.");
        } else {
            // Display appointments in a table format
            System.out.println("+-------------------------------------------+");
            System.out.println("| Appointment ID | Patient ID | Time  | Status |");
            System.out.println("+-------------------------------------------+");
    
            for (Appointment appointment : pendingAppointments) {
                System.out.printf("| %-14s | %-10s | %-5s | %-7s |\n",
                    appointment.getAppointmentID(),
                    appointment.getPatient().getHospitalID(),
                    appointment.getAppointmentTime(),
                    appointment.getStatus());
            }
    
            System.out.println("+-------------------------------------------+");
    
            // Prompt the user to enter an appointment ID to decline
            System.out.print("Enter Appointment ID to decline: ");
            String appointmentIDToDecline = scanner.nextLine();
            Appointment appointmentToDecline = AppointmentsCsvHelper.getAppointmentById(appointmentIDToDecline);
    
            if (appointmentToDecline != null && appointmentToDecline.getStatus() == Appointment.AppointmentStatus.PENDING) {
                appointmentToDecline.setStatus(Appointment.AppointmentStatus.DECLINED);
                AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToDecline, Appointment.AppointmentStatus.DECLINED);
                System.out.println("Appointment declined: " + appointmentToDecline.getAppointmentID());
            } else {
                System.out.println("Appointment not found or already processed.");
            }
        }
    }
    

    public void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        String appointmentIDToRecord = scanner.nextLine();
        
        // Attempt to retrieve the appointment by ID
        Appointment appointmentToRecord = AppointmentsCsvHelper.getAppointmentById(appointmentIDToRecord);
        
        // Check if appointment exists and is valid
        if (appointmentToRecord == null) {
            System.out.println("Error: Appointment not found. Exiting this case.");
            return; // Exit the method if the appointment is not found
        }
        
        // Ensure the appointment status is ACCEPTED before marking it as COMPLETED
        if (appointmentToRecord.getStatus() != Appointment.AppointmentStatus.ACCEPTED) {
            System.out.println("Error: Appointment must be in ACCEPTED status to mark it as COMPLETED.");
            return; // Exit if the status is not ACCEPTED
        }
    
        // Update status to COMPLETED
        appointmentToRecord.setStatus(Appointment.AppointmentStatus.COMPLETED);
        System.out.println("Appointment status updated to COMPLETED.");
        
        // Attempt to retrieve the patient's medical record
        String patientIDToRecord = appointmentToRecord.getPatient().getHospitalID();
        MedicalRecord appointmentRecord = MedicalRecordsCsvHelper.getMedicalRecordById(patientIDToRecord);
        
        // Ensure the medical record for the patient exists
        if (appointmentRecord == null) {
            System.out.println("Error: No medical record found for patient ID: " + patientIDToRecord);
            return; // Exit if the medical record is not found
        }
        
        System.out.print("Enter service type (CONSULTATION, XRAY, BLOOD_TEST): ");
        String serviceTypeInput = scanner.nextLine().trim().toUpperCase();
        
        try {
            AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(serviceTypeInput);
            System.out.print("Enter consultation notes: ");
            String notes = scanner.nextLine();
        
            // Initialize the medications map
            HashMap<String, Integer> medications = new HashMap<>();
            
            while (true) {
                System.out.print("Enter medication name (or 'done' to finish): ");
                String medName = scanner.nextLine();
                
                if (medName.equalsIgnoreCase("done")) {
                    break;
                }
                
                System.out.print("Enter quantity for " + medName + ": ");
                int quantity = Integer.parseInt(scanner.nextLine());
                medications.put(medName, quantity);
            }
        
            // Create the outcome record and set it to 'Pending' by default
            AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                patientIDToRecord,
                appointmentToRecord.getDoctor().getHospitalID(),
                appointmentIDToRecord,
                appointmentToRecord.getAppointmentDate(),
                notes,
                serviceType
            );
        
            outcomeRecord.setMedications(medications); // Set medications
            outcomeRecord.setStatusOfPrescription(AppointmentOutcomeRecord.StatusOfPrescription.PENDING); // Default status
            
            // Save the outcome record
            AppointmentOutcomeRecordsCsvHelper.saveAppointmentOutcome(outcomeRecord);
            System.out.println("Appointment outcome recorded successfully with status 'Pending'.");
            
            // Update the status of the appointment in the CSV file
            AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToRecord, Appointment.AppointmentStatus.COMPLETED);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid service type entered.");
        } catch (Exception e) {
            System.out.println("An error occurred while recording the appointment outcome: " + e.getMessage());
        }
    }
    

    public void viewDoctorAvailability() {
        System.out.println("\n=== View Doctor Availability ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");
    
        // Step 1: Display Scheduled Appointments
        System.out.println("\n--- Scheduled Appointments ---");
        List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments()
                .stream()
                .filter(appointment -> appointment.getDoctorID().equals(doctorModel.getHospitalID()) &&
                                       appointment.getStatus() == Appointment.AppointmentStatus.ACCEPTED)
                .distinct()  // Remove duplicates
                .collect(Collectors.toList());
    
        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            System.out.println("+---------------------------------------------------------------+");
            System.out.println("| Appointment ID | Patient ID | Date       | Time  | Status     |");
            System.out.println("+---------------------------------------------------------------+");
    
            for (Appointment appointment : appointments) {
                System.out.printf("| %-14s | %-10s | %-10s | %-5s | %-10s |\n",
                                  appointment.getAppointmentID(),
                                  appointment.getPatientID(),
                                  appointment.getAppointmentDate(),
                                  appointment.getAppointmentTime(),
                                  appointment.getStatus());
            }
    
            System.out.println("+---------------------------------------------------------------+");
        }
    
        // Step 2: Display Available Slots
        System.out.println("\n--- Available Slots ---");
        List<DoctorAvailability> availabilityList = DoctorAvailabilityCsvHelper.loadDoctorAvailability()
                .stream()
                .filter(availability -> availability.getDoctorID().equals(doctorModel.getHospitalID()))
                .collect(Collectors.toList());
    
        if (availabilityList.isEmpty()) {
            System.out.println("No availability set.");
        } else {
            // Sort and merge the availability slots by date and time
            List<DoctorAvailability> mergedAvailability = mergeAvailabilitySlots(availabilityList);
    
            // Display the merged availability
            System.out.println("+-------------------------------------------+");
            System.out.println("|   Date       | Start Time | End Time      |");
            System.out.println("+-------------------------------------------+");
    
            for (DoctorAvailability avail : mergedAvailability) {
                System.out.printf("| %s |   %s   |   %s   |\n",
                    avail.getDate(), avail.getStartTime(), avail.getEndTime());
            }
    
            System.out.println("+-------------------------------------------+");
        }
    
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine(); // Wait for the user to press Enter
    }
    
    
    
    

        // Helper method to merge overlapping or adjacent time slots
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
        

// Helper method to find the later of two end times
private String maxTime(String time1, String time2) {
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime t1 = LocalTime.parse(time1, timeFormatter);
    LocalTime t2 = LocalTime.parse(time2, timeFormatter);
    return t1.isAfter(t2) ? time1 : time2;
}
}
