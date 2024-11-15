package SC2002_Assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;


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
                    patients.add(new Patient(patientID, name, dob, gender, bloodType, email));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
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
}
