package SC2002_Assignment;

import java.io.*;
import java.util.*;

public class PatientCsvHelper {
    private static final String PATIENT_FILE_PATH = "data/PatientList.csv";

    // Method to update a patient's information
    public static boolean updatePatientInfo(String patientId, String field, String newValue) {
        List<String[]> allPatients = new ArrayList<>();
        boolean updated = false;

        // Read all patient data
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_FILE_PATH))) {
            String line = br.readLine(); // Read header
            allPatients.add(line.split(","));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(patientId)) {
                    // Update the field
                    switch (field.toLowerCase()) {
                        case "name": data[1] = newValue; break;
                        case "email": data[3] = newValue; break;
                        // Add cases for other fields
                    }
                    updated = true;
                }
                allPatients.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write updated data back to CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENT_FILE_PATH))) {
            for (String[] patient : allPatients) {
                bw.write(String.join(",", patient));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return updated;
    }
}

