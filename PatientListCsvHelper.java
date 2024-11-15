

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PatientListCsvHelper {
    private static final String FILE_PATH = "data/PatientList.csv";

    // Load all patients from the CSV file
    public static List<PatientModel> loadPatients() {
        List<PatientModel> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 6) {
                    String patientID = values[0];
                    String name = values[1];
                    String dob = values[2];
                    String gender = values[3];
                    String bloodType = values[4];
                    String email = values[5];
                    String contactNumber = values[6];
                    patients.add(new PatientModel(patientID, "password", "Patient", name, dob, gender, contactNumber, email, bloodType));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public static boolean addPatientToCsv(PatientModel patient) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/PatientList.csv", true))) {
            // Write patient details as a new line in the CSV
            bw.write(patient.toCsv());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    // Get a specific patient by patient ID
    public static PatientModel getPatientById(String patientID) {
        List<PatientModel> patients = loadPatients();
        for (PatientModel patient : patients) {
            if (patient.getHospitalID().equals(patientID)) {
                return patient;
            }
        }
        return null;
    }


    public static List<String> getMedicalRecord(String patientId)
    {
        List<String> medicalRecords = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH)))
        {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(patientId)) {
                    // assuming medical records start at index 6
                    for (int i = 6; i < data.length; i++) {
                        medicalRecords.add(data[i]);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicalRecords;
    }

    public static void updatePatientContactInfo(String patientID, String contactNumber, String email) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(patientID)) {
                    if (contactNumber != null) values[6] = contactNumber; // Update contact number
                    if (email != null) values[5] = email; // Update email
                }
                rows.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
