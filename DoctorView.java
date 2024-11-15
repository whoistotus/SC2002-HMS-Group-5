package SC2002_Assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import SC2002_Assignment.Appointment;

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
                case 4 -> viewPersonalSchedule();
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

    public void viewPersonalSchedule() {
        System.out.println("Personal Scheduled Appointments:");
        List<Appointment> uniqueAppointments = doctorModel.getAppointments().stream().distinct().collect(Collectors.toList());
        if (uniqueAppointments.isEmpty()) {
            System.out.println("No upcoming appointments.");
        } else {
            for (Appointment appointment : uniqueAppointments) {
                if (appointment.getStatus() == Appointment.AppointmentStatus.ACCEPTED) {
                    System.out.println(appointment);
                }
            }
        }
    }

    public void viewPatientMedicalRecords() {
        System.out.print("Enter Patient ID to view record: ");
        String patientID = scanner.nextLine();
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
        if (record != null) {
            System.out.println("Medical Record: " + record);
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
        System.out.println("Pending Appointments:");
        List<Appointment> pendingAppointments = AppointmentsCsvHelper.getPendingAppointmentsForDoctor(doctorModel.getHospitalID());

        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments.");
            return;
        }

        for (Appointment app : pendingAppointments) {
            System.out.println(app);
        }

        System.out.print("Enter Appointment ID to accept: ");
        String appointmentIDToAccept = scanner.nextLine();
        Appointment appointmentToAccept = AppointmentsCsvHelper.getAppointmentById(appointmentIDToAccept);

        if (appointmentToAccept != null && appointmentToAccept.getStatus() == Appointment.AppointmentStatus.PENDING) {
            appointmentToAccept.setStatus(Appointment.AppointmentStatus.ACCEPTED);
            AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToAccept, Appointment.AppointmentStatus.ACCEPTED);
            System.out.println("Appointment accepted: " + appointmentToAccept);
        } else {
            System.out.println("Appointment not found or already accepted.");
        }
    }

    public void declineAppointment() {
        System.out.println("Pending Appointments:");
        List<Appointment> pendingAppointments = AppointmentsCsvHelper.getPendingAppointmentsForDoctor(doctorModel.getHospitalID());

        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments.");
            return;
        }

        for (Appointment app : pendingAppointments) {
            System.out.println(app);
        }

        System.out.print("Enter Appointment ID to decline: ");
        String appointmentIDToDecline = scanner.nextLine();
        Appointment appointmentToDecline = AppointmentsCsvHelper.getAppointmentById(appointmentIDToDecline);

        if (appointmentToDecline != null && appointmentToDecline.getStatus() == Appointment.AppointmentStatus.PENDING) {
            appointmentToDecline.setStatus(Appointment.AppointmentStatus.DECLINED);
            AppointmentsCsvHelper.updateAppointmentStatus(appointmentIDToDecline, Appointment.AppointmentStatus.DECLINED);
            System.out.println("Appointment declined: " + appointmentToDecline);
        } else {
            System.out.println("Appointment not found or already processed.");
        }
    }

    public void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        String appointmentIDToRecord = scanner.nextLine();
        Appointment appointmentToRecord = AppointmentsCsvHelper.getAppointmentById(appointmentIDToRecord);

        if (appointmentToRecord != null) {
            String patientIDToRecord = appointmentToRecord.getPatientID();
            MedicalRecord appointmentRecord = MedicalRecordsCsvHelper.getMedicalRecordById(patientIDToRecord);

            if (appointmentRecord == null) {
                System.out.println("No medical record found for patient ID: " + patientIDToRecord);
                return;
            }

            System.out.print("Enter service type (CONSULTATION, XRAY, BLOOD_TEST): ");
            String serviceTypeInput = scanner.nextLine().trim().toUpperCase();

            try {
                AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(serviceTypeInput);
                System.out.print("Enter consultation notes: ");
                String notes = scanner.nextLine();

                HashMap<String, Integer> medications = new HashMap<>();
                System.out.print("Enter medication name (or 'done' to finish): ");
                String medName;
                while (!(medName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    System.out.print("Enter quantity for " + medName + ": ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    medications.put(medName, quantity);
                    System.out.print("Enter next medication name (or 'done' to finish): ");
                }

                // Convert the Date to String format for compatibility with AppointmentOutcomeRecord
                String formattedDate = dateFormat.format(AppointmentOutcomeRecord.appointmentDate);

                AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                    patientIDToRecord,
                    appointmentToRecord.getDoctorID(),
                    appointmentIDToRecord,
                    formattedDate, // Pass the formatted String date
                    notes,
                    serviceType
                );

                outcomeRecord.setMedications(medications);

                AppointmentOutcomeRecordsCsvHelper.saveAppointmentOutcome(outcomeRecord);
                System.out.println("Appointment outcome recorded successfully.");
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    public void viewDoctorAvailability() {
        System.out.println("Doctor Availability:");
        if (doctorModel.getAvailability().isEmpty()) {
            System.out.println("No availability set.");
        } else {
            for (DoctorAvailability avail : doctorModel.getAvailability()) {
                System.out.println(avail);
            }
        }
    }
}
