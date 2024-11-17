import java.io.*;
import java.util.*;

public class AppointmentOutcomeRecordsCsvHelper {
    private static final String FILE_PATH = "data/AppointmentOutcomeRecords.csv";

    // Load all appointment outcome records from CSV
    public static List<AppointmentOutcomeRecord> loadAppointmentOutcomes() {
        List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String appointmentID = values[2];
                String patientID = values[0];
                String doctorID = values[1];
                String date = values[3];
                AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(values[5].toUpperCase());
                String consultationNotes = values[4];
                             
                // Parse medications
                HashMap<String, Integer> medications = AppointmentOutcomeRecord.parseMedicationsFromCsv(values[6]);
                
                AppointmentOutcomeRecord.StatusOfPrescription prescriptionStatus = AppointmentOutcomeRecord.StatusOfPrescription.valueOf(values[7].toUpperCase());

                // Create AppointmentOutcomeRecord object
                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointmentID, date, consultationNotes, serviceType);
                outcome.setMedications(medications);
                outcome.setStatusOfPrescription(prescriptionStatus);

                outcomes.add(outcome);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outcomes;
    }

    // Save an appointment outcome record to CSV
    public static void saveAppointmentOutcome(AppointmentOutcomeRecord outcome) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) { // Append mode
            String formattedMedications = formatMedications(outcome.getMedications());
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                outcome.getAppointmentID(),
                outcome.getPatientID(),
                outcome.getDoctorID(),
                outcome.getDate(),
                outcome.getServiceType(),
                outcome.getConsultationNotes(),
                formattedMedications,
                outcome.getStatusOfPrescription()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToCsv(String record) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // Append mode
        bw.write(record); // Write the record
        bw.newLine(); // Add a newline for the next record
        System.out.println("DEBUG: Successfully added to CSV -> " + record); // Debug output
    } catch (IOException e) {
        System.out.println("ERROR: Failed to write to CSV -> " + e.getMessage()); // Handle file I/O errors
    }
}
    


    // Update a specific appointment outcome record in the CSV file
    public static void updateAppointmentOutcomeRecord(AppointmentOutcomeRecord updatedRecord) {
        List<AppointmentOutcomeRecord> records = loadAppointmentOutcomes();
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(updatedRecord.getAppointmentID())) {
                String updatedLine = formatRecordToCsv(updatedRecord);
                lines.add(updatedLine);
                updated = true;
            } else {
                lines.add(formatRecordToCsv(record));
            }
        }

        if (updated) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                writer.println("appointmentID,patientID,doctorID,date,serviceType,consultationNotes,medications,statusOfPrescription");
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper to format a record as a CSV string
    protected static String formatRecordToCsv(AppointmentOutcomeRecord record) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                record.getAppointmentID(),
                record.getPatientID(),
                record.getDoctorID(),
                record.getDate(),
                record.getServiceType(),
                record.getConsultationNotes(),
                formatMedications(record.getMedications()),
                record.getStatusOfPrescription()
        );
    }

    // Helper to format medications as a CSV-compatible string
    private static String formatMedications(HashMap<String, Integer> medications) {
        StringBuilder formattedMedications = new StringBuilder();
        for (Map.Entry<String, Integer> entry : medications.entrySet()) {
            formattedMedications.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        if (formattedMedications.length() > 0) {
            formattedMedications.setLength(formattedMedications.length() - 1); // Remove trailing semicolon
        }
        return formattedMedications.toString();
    }
}
