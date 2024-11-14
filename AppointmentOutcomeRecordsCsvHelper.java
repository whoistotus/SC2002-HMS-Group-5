package SC2002_Assignment;

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
                Date date = dateFormat.parse(values[3]);
                AppointmentOutcomeRecord.ServiceType serviceType = AppointmentOutcomeRecord.ServiceType.valueOf(values[4].toUpperCase());
                String consultationNotes = values[5];
                
                // Parse medications
                HashMap<String, Integer> medications = new HashMap<>();
                String[] medicationsArray = values[6].split(";");
                for (String medicationEntry : medicationsArray) {
                    String[] medParts = medicationEntry.split(":");
                    if (medParts.length == 2) {
                        String medName = medParts[0].trim();
                        int quantity = Integer.parseInt(medParts[1].trim());
                        medications.put(medName, quantity);
                    }
                }

                AppointmentOutcomeRecord.StatusOfPrescription prescriptionStatus = AppointmentOutcomeRecord.StatusOfPrescription.valueOf(values[7].toUpperCase());

                // Create AppointmentOutcomeRecord object
                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointmentID, date, consultationNotes, serviceType);
                outcome.setMedications(medications);
                outcome.setStatusOfPrescription(prescriptionStatus);

                outcomes.add(outcome);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return outcomes;
    }

    // Save all appointment outcome records to CSV
    public static void saveAppointmentOutcome(AppointmentOutcomeRecord outcome) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) { // Append mode
            writer.printf("%s,%s,%s,%s,%s,%s%n",
                outcome.getPatientID(),
                outcome.getDoctorID(),
                outcome.getAppointmentID(),
                outcome.getDate().toString(),
                outcome.getConsultationNotes(),
                outcome.getServiceType()
            );
            // Add medications if required
            for (Map.Entry<String, Integer> entry : outcome.getMedications().entrySet()) {
                writer.printf(",%s,%d", entry.getKey(), entry.getValue());
            }
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Update a specific appointment outcome record in the CSV file
    public static void updateAppointmentOutcomeRecord(AppointmentOutcomeRecord updatedRecord) {
        List<AppointmentOutcomeRecord> records = loadAppointmentOutcomes();  // Load all records from the file

        // Create a temporary list to hold the modified records
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean updated = false;
            
            // Read through the file and update the record if matched
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(updatedRecord.getAppointmentID())) {
                    // If the record matches, update it with the new status
                    String updatedLine = String.format("%s,%s,%s,%s,%s,%s,%s:%d;%s", 
                            updatedRecord.getAppointmentID(),
                            updatedRecord.getPatientID(),
                            updatedRecord.getDoctorID(),
                            dateFormat.format(updatedRecord.getDate()),
                            updatedRecord.getServiceType(),
                            updatedRecord.getConsultationNotes(),
                            formatMedications(updatedRecord.getMedications()),
                            updatedRecord.getStatusOfPrescription());
                    
                    lines.add(updatedLine);  // Add updated record to temporary list
                    updated = true;
                } else {
                    lines.add(line);  // Keep the original line for other records
                }
            }

            // If the record was updated, overwrite the file with the new content
            if (updated) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                    // Write all lines back to the file
                    for (String l : lines) {
                        writer.println(l);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to format medications
    private static String formatMedications(Map<String, Integer> medications) {
        StringBuilder formattedMedications = new StringBuilder();
        for (Map.Entry<String, Integer> entry : medications.entrySet()) {
            formattedMedications.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        return formattedMedications.toString();
    }

}
