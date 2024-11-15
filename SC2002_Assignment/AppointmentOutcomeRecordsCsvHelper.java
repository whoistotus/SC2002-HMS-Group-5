package SC2002_Assignment;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppointmentOutcomeRecordsCsvHelper {
    private static final String FILE_PATH = "data/AppointmentOutcomeRecords.csv";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Load all appointment outcome records from CSV
    public static AppointmentOutcomeRecord loadAppointmentOutcome(String[] values) {
        String patientID = values[0];
        String doctorID = values[1];
        String appointmentID = values[2];
        String date = values[3]; // Date is now a String
        AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(values[4]);
        String consultationNotes = values[5];
        HashMap<String, Integer> medications = AppointmentOutcomeRecord.parseMedicationsFromCsv(values[6]);
        AppointmentOutcomeRecord.StatusOfPrescription status = AppointmentOutcomeRecord.StatusOfPrescription.valueOf(values[7]);
    
        AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(patientID, doctorID, appointmentID, date, consultationNotes, serviceType);
        record.setMedications(medications);
        record.setStatusOfPrescription(status);
    
        return record;
    }
    

    // Save all appointment outcome records to CSV
    public static void saveAppointmentOutcome(AppointmentOutcomeRecord record) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("AppointmentOutcomeRecord.csv", true))) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                record.getPatientID(),
                record.getDoctorID(),
                record.getAppointmentID(),
                record.getDate(), // Now saving date as a string
                record.getServiceType(),
                record.getConsultationNotes(),
                record.medicationsToCsv(),
                record.getStatusOfPrescription()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}