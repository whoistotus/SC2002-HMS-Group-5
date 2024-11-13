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
                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                    patientID, doctorID, appointmentID, date, consultationNotes, serviceType
                );
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
}
