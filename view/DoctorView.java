package view;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.DoctorAvailability;
import model.DoctorModel;
import model.MedicalRecord;
import utils.MedicalRecordsCsvHelper;
import model.PatientModel;
import utils.AppointmentOutcomeRecordsCsvHelper;
import utils.AppointmentsCsvHelper;
import utils.DoctorAvailabilityCsvHelper;
import utils.PatientListCsvHelper;

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
            System.out.println("8. View Personal Schedule");
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
    
        List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments();
    
        // Filter for the current doctor's confirmed appointments
        List<Appointment> acceptedAppointments = appointments.stream()
                .filter(appointment -> appointment.getDoctorID().equals(doctorModel.getHospitalID()) &&
                                       appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
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
    
            // Fetch and display patient details below the table
            System.out.println("\n=== Patient Details ===");
            for (Appointment appointment : acceptedAppointments) {
                String patientID = appointment.getPatientID();
                MedicalRecord medicalRecord = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
    
                // Display patient ID and medical record details
                System.out.println("Patient ID: " + patientID);
                if (medicalRecord != null) {
                    medicalRecord.viewMedicalRecord(); // Assuming this method prints the record
                } else {
                    System.out.println("No medical record found for Patient ID: " + patientID);
                }
                System.out.println(); // Add space between patient details
            }
        }
    }
    

    public void viewPatientMedicalRecords() {
        System.out.print("Enter Patient ID to view record: ");
        String patientID = scanner.nextLine();
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);

        if (record != null) {
            record.viewMedicalRecord();
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
    
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
        if (record != null) {
            // Get input for updates
            System.out.print("Enter new diagnosis (leave empty if no update): ");
            String diagnosis = scanner.nextLine();
            System.out.print("Enter new treatment plan (leave empty if no update): ");
            String treatment = scanner.nextLine();
            System.out.print("Enter new prescription (leave empty if no update): ");
            String prescription = scanner.nextLine();
    
            // Handle CurrentDiagnoses -> PastDiagnoses
            if (diagnosis != null && !diagnosis.trim().isEmpty()) {
                if (!record.getCurrentDiagnoses().isEmpty() && !record.getCurrentDiagnoses().get(0).equalsIgnoreCase("None")) {
                    // Move current diagnoses to past
                    record.getPastDiagnoses().addAll(record.getCurrentDiagnoses());
                }
                // Set the new diagnosis as the only current diagnosis
                record.getCurrentDiagnoses().clear();
                record.addNewDiagnosis(diagnosis);
            }
    
            // Handle CurrentTreatments -> PastTreatments
            if (treatment != null && !treatment.trim().isEmpty()) {
                if (!record.getCurrentTreatments().isEmpty() && !record.getCurrentTreatments().get(0).equalsIgnoreCase("None")) {
                    // Move current treatments to past
                    record.getPastTreatments().addAll(record.getCurrentTreatments());
                }
                // Set the new treatment as the only current treatment
                record.getCurrentTreatments().clear();
                record.addNewTreatment(treatment);
            }
    
            // Handle Prescriptions (no need to move to past)
            if (prescription != null && !prescription.trim().isEmpty()) {
                record.getPrescriptions().removeIf(value -> value.equalsIgnoreCase("None"));
                record.addNewPrescription(prescription);
            }
    
            // Save the updated record
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

        DoctorAvailability availability = new DoctorAvailability(doctorModel, date, startTime, endTime);
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

            System.out.print("Enter Appointment ID to accept: ");
            String appointmentIDToAccept = scanner.nextLine();
            Appointment appointmentToAccept = AppointmentsCsvHelper.getAppointmentById(appointmentIDToAccept);

            if (appointmentToAccept != null && appointmentToAccept.getStatus() == Appointment.AppointmentStatus.PENDING) {
                appointmentToAccept.setStatus(Appointment.AppointmentStatus.CONFIRMED);
                AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToAccept, Appointment.AppointmentStatus.CONFIRMED);
                doctorModel.addAppointment(appointmentToAccept);

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
        LocalTime appointmentEndTime = appointmentStartTime.plusHours(1);

        for (DoctorAvailability availability : availabilityList) {
            if (!availability.getDate().equals(appointment.getAppointmentDate())) {
                updatedAvailability.add(availability);
                continue;
            }

            LocalTime availableStart = LocalTime.parse(availability.getStartTime(), timeFormatter);
            LocalTime availableEnd = LocalTime.parse(availability.getEndTime(), timeFormatter);

            if (availableStart.isBefore(appointmentStartTime)) {
                updatedAvailability.add(new DoctorAvailability(
                    availability.getDoctor(),
                    availability.getDate(),
                    availability.getStartTime(),
                    appointment.getAppointmentTime()
                ));
            }
            if (availableEnd.isAfter(appointmentEndTime)) {
                updatedAvailability.add(new DoctorAvailability(
                    availability.getDoctor(),
                    availability.getDate(),
                    appointmentEndTime.format(timeFormatter),
                    availability.getEndTime()
                ));
            }
        }
        DoctorAvailabilityCsvHelper.saveDoctorAvailability(updatedAvailability);
    }

    public void declineAppointment() {
        System.out.println("\n=== Decline Appointment Request ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");
        System.out.println("Here are your pending appointments:");

        List<Appointment> pendingAppointments = AppointmentsCsvHelper.getPendingAppointmentsForDoctor(doctorModel.getHospitalID());

        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments.");
        } else {
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

            System.out.print("Enter Appointment ID to decline: ");
            String appointmentIDToDecline = scanner.nextLine();
            Appointment appointmentToDecline = AppointmentsCsvHelper.getAppointmentById(appointmentIDToDecline);

            if (appointmentToDecline != null && appointmentToDecline.getStatus() == Appointment.AppointmentStatus.PENDING) {
                appointmentToDecline.setStatus(Appointment.AppointmentStatus.CANCELLED);
                AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToDecline, Appointment.AppointmentStatus.CANCELLED);
                System.out.println("Appointment declined: " + appointmentToDecline.getAppointmentID());
            } else {
                System.out.println("Appointment not found or already processed.");
            }
        }
    }

    public void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        String appointmentIDToRecord = scanner.nextLine();
        Appointment appointmentToRecord = AppointmentsCsvHelper.getAppointmentById(appointmentIDToRecord);
    
        if (appointmentToRecord == null) {
            System.out.println("Error: Appointment not found. Exiting this case.");
            return;
        }
    
        if (appointmentToRecord.getStatus() != Appointment.AppointmentStatus.CONFIRMED) {
            System.out.println("Error: Appointment must be in CONFIRMED status to mark it as COMPLETED.");
            return;
        }
    
        appointmentToRecord.setStatus(Appointment.AppointmentStatus.COMPLETED);
        System.out.println("Appointment status updated to COMPLETED.");
    
        try {
            System.out.print("Enter service type (CONSULTATION, XRAY, BLOOD_TEST): ");
            String serviceTypeInput = scanner.nextLine().trim().toUpperCase();
            AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(serviceTypeInput);
    
            System.out.print("Enter consultation notes: ");
            String notes = scanner.nextLine();
    
            HashMap<String, Integer> medications = new HashMap<>();
            while (true) {
                System.out.print("Enter medication name (or 'done' to finish): ");
                String medName = scanner.nextLine();
                if (medName.equalsIgnoreCase("done")) {
                    break;
                }
                System.out.print("Enter quantity for " + medName + ": ");
                try {
                    int quantity = Integer.parseInt(scanner.nextLine());
                    medications.put(medName, quantity);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a number.");
                }
            }
    
            // Create the record string for CSV
            StringBuilder csvRecord = new StringBuilder();
            csvRecord.append(appointmentToRecord.getPatient().getHospitalID()).append(",");
            csvRecord.append(appointmentToRecord.getDoctor().getHospitalID()).append(",");
            csvRecord.append(appointmentIDToRecord).append(",");
            csvRecord.append(appointmentToRecord.getAppointmentDate()).append(",");
            csvRecord.append(notes).append(",");
            csvRecord.append(serviceType).append(",");
    
            // Append medications in "medication1:quantity1|medication2:quantity2" format
            if (!medications.isEmpty()) {
                medications.forEach((med, qty) -> csvRecord.append(med).append(":").append(qty).append("|"));
                csvRecord.deleteCharAt(csvRecord.length() - 1); // Remove last "|"
            } else {
                csvRecord.append("N/A");
            }
    
            csvRecord.append(",Pending"); // Append statusOfPrescription as Pending
    
            // Debug the constructed CSV record
            System.out.println("Constructed CSV record: " + csvRecord);
    
            // Write to the CSV file
            AppointmentOutcomeRecordsCsvHelper.writeToCsv(csvRecord.toString());
    
            // Update appointment status in Appointments CSV
            AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToRecord, Appointment.AppointmentStatus.COMPLETED);
    
            System.out.println("Appointment outcome recorded successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid service type entered.");
        } catch (Exception e) {
            System.out.println("An error occurred while recording the appointment outcome: " + e.getMessage());
        }
    }
    
    
    

    public void viewDoctorAvailability() {
        System.out.println("\n=== View Doctor Availability ===");
        System.out.println("Doctor: " + doctorModel.getName() + " (ID: " + doctorModel.getHospitalID() + ")");

        System.out.println("\n--- Scheduled Appointments ---");
        List<Appointment> appointments = AppointmentsCsvHelper.loadAppointments()
                .stream()
                .filter(appointment -> appointment.getDoctorID().equals(doctorModel.getHospitalID()) &&
                                       appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
                .distinct()
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

        System.out.println("\n--- Available Slots ---");
        List<DoctorAvailability> availabilityList = DoctorAvailabilityCsvHelper.loadDoctorAvailability()
                .stream()
                .filter(availability -> availability.getDoctorID().equals(doctorModel.getHospitalID()))
                .collect(Collectors.toList());

        if (availabilityList.isEmpty()) {
            System.out.println("No availability set.");
        } else {
            List<DoctorAvailability> mergedAvailability = mergeAvailabilitySlots(availabilityList);

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
        scanner.nextLine();
    }

    private List<DoctorAvailability> mergeAvailabilitySlots(List<DoctorAvailability> availabilityList) {
        List<DoctorAvailability> mergedList = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        DoctorAvailability current = availabilityList.get(0);

        for (int i = 1; i < availabilityList.size(); i++) {
            DoctorAvailability next = availabilityList.get(i);

            if (current.getDate().equals(next.getDate()) &&
                (LocalTime.parse(current.getEndTime(), timeFormatter).isAfter(LocalTime.parse(next.getStartTime(), timeFormatter)) ||
                LocalTime.parse(current.getEndTime(), timeFormatter).equals(LocalTime.parse(next.getStartTime(), timeFormatter)))) {

                current.setEndTime(maxTime(current.getEndTime(), next.getEndTime()));
            } else {
                mergedList.add(current);
                current = next;
            }
        }
        mergedList.add(current);

        return mergedList;
    }

    private String maxTime(String time1, String time2) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1 = LocalTime.parse(time1, timeFormatter);
        LocalTime t2 = LocalTime.parse(time2, timeFormatter);
        return t1.isAfter(t2) ? time1 : time2;
    }
}
