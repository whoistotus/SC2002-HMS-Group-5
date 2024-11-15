import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppointmentOutcomeRecordsCsvHelper {
    private static final String FILE_PATH = "data/AppointmentOutcomeRecords.csv";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Load all appointment outcome records from CSV
    public static List<AppointmentOutcomeRecord> loadAppointmentOutcomes() {
        List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String appointmentID = values[0];
                String patientID = values[1];
                String doctorID = values[2];
                String date = values[3];
                AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(values[4].toUpperCase());
                String consultationNotes = values[5];
                
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
    // Save all appointment outcome records to CSV
    public static void saveAppointmentOutcome(AppointmentOutcomeRecord outcome) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) { // Append mode
            // Format medications as "medName1:quantity1;medName2:quantity2"
            String formattedMedications = formatMedications(outcome.getMedications());

            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                outcome.getPatientID(),
                outcome.getDoctorID(),
                outcome.getAppointmentID(),
                outcome.getDate(),
                outcome.getConsultationNotes(),
                outcome.getServiceType(),
                formattedMedications,
                "Pending" // Set status to Pending by default
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Update a specific appointment outcome record in the CSV file
    public static void updateAppointmentOutcomeRecord(AppointmentOutcomeRecord updatedRecord) {
        List<AppointmentOutcomeRecord> records = loadAppointmentOutcomes();  // Load all records from the file

        List<String> lines = new ArrayList<>();
        boolean updated = false;

        // Go through each record and update if it matches the appointment ID
        for (AppointmentOutcomeRecord record : records) {
            if (record.getAppointmentID().equals(updatedRecord.getAppointmentID())) {
                // Replace with updated record
                String updatedLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        updatedRecord.getPatientID(),
                        updatedRecord.getDoctorID(),
                        updatedRecord.getAppointmentID(),
                        updatedRecord.getDate(),
                        updatedRecord.getConsultationNotes(),
                        updatedRecord.getServiceType(),
                        formatMedications(updatedRecord.getMedications()),
                        updatedRecord.getStatusOfPrescription());
                lines.add(updatedLine);
                updated = true;
            } else {
                // Keep the original line for other records
                lines.add(formatRecordToCsv(record));
            }
        }

        // If the record was updated, overwrite the file with the new content
        if (updated) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                writer.println("patientID,doctorID,appointmentID,date,consultationNotes,serviceType,medications,statusOfPrescription");
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatRecordToCsv(AppointmentOutcomeRecord record) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                record.getPatientID(),
                record.getDoctorID(),
                record.getAppointmentID(),
                record.getDate(),
                record.getConsultationNotes(),
                record.getServiceType(),
                formatMedications(record.getMedications()),
                record.getStatusOfPrescription()
        );
    }

    // Helper method to format medications
    private static String formatMedications(HashMap<String, Integer> medications) {
        StringBuilder formattedMedications = new StringBuilder();
        for (Map.Entry<String, Integer> entry : medications.entrySet()) {
            formattedMedications.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        if (formattedMedications.length() > 0) {
            formattedMedications.setLength(formattedMedications.length() - 1); // Remove the trailing semicolon
        }
        return formattedMedications.toString();
    }

}