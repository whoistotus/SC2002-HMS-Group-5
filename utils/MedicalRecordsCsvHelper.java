package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.MedicalRecord;

//import SC2002_Assignment.MedicalRecord.Gender;

public class MedicalRecordsCsvHelper {
    private static final String FILE_PATH = "data/MedicalRecords.csv";

    // Enum for gender with added NULL handling
    public enum Gender {
        MALE, FEMALE, OTHER, NULL
    }

    // Method to parse gender string safely, handling NULL and invalid cases
    public static MedicalRecord.Gender parseGender(String genderString) {
        if (genderString == null || genderString.trim().isEmpty() || genderString.equalsIgnoreCase("NULL")) {
            return MedicalRecord.Gender.OTHER; // Default if gender is NULL
        }
        try {
            return MedicalRecord.Gender.valueOf(genderString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Invalid gender value '" + genderString + "'. Defaulting to OTHER.");
            return MedicalRecord.Gender.OTHER;
        }
    }

    // Method to load all medical records from the CSV
    public static List<MedicalRecord> loadMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                // Check if the row has the minimum required columns
                if (values.length < 7) {
                    System.out.println("Warning: Skipping malformed row: " + line);
                    continue;
                }
                
                String patientID = values[0];
                String name = values[1];
                String dob = values[2];
                MedicalRecord.Gender gender = parseGender(values[3]);
                String contactNumber = values[4];
                String bloodType = values[5];
                String email = values[6];
    
                // Use helper method to parse optional fields
                List<String> pastDiagnoses = values.length > 7 ? parseListField(values[7]) : new ArrayList<>();
                List<String> pastTreatments = values.length > 8 ? parseListField(values[8]) : new ArrayList<>();
                List<String> currentDiagnoses = values.length > 9 ? parseListField(values[9]) : new ArrayList<>();
                List<String> currentTreatments = values.length > 10 ? parseListField(values[10]) : new ArrayList<>();
                List<String> prescriptions = values.length > 11 ? parseListField(values[11]) : new ArrayList<>();
    
                // Create a MedicalRecord object
                MedicalRecord record = new MedicalRecord(
                    patientID, name, dob, gender, contactNumber, bloodType, email,
                    pastDiagnoses, pastTreatments, currentDiagnoses, currentTreatments, prescriptions
                );
    
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
    

    // Helper method to parse semicolon-separated fields into a list
    private static List<String> parseListField(String field) {
        List<String> list = new ArrayList<>();
        if (field != null && !field.isEmpty()) {
            String[] items = field.split(";");
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }

    // Retrieve a specific medical record by patient ID
    public static MedicalRecord getMedicalRecordById(String patientID) {
        List<MedicalRecord> records = loadMedicalRecords();
        for (MedicalRecord record : records) {
            if (record.getPatientID().equals(patientID)) {
                return record;
            }
        }
        return null;
    }

    // Save or update a specific medical record in the CSV file
    public static void saveMedicalRecord(MedicalRecord record) {
        List<MedicalRecord> records = loadMedicalRecords();
        boolean found = false;
    
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPatientID().equals(record.getPatientID())) {
                records.set(i, record);
                found = true;
                break;
            }
        }
    
        if (!found) {
            records.add(record);
        }
    
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/MedicalRecords.csv"))) {
            writer.println("PatientID,Name,DOB,Gender,ContactNumber,BloodType,Email,PastDiagnoses,PastTreatments,CurrentDiagnoses,CurrentTreatments,Prescriptions");
            for (MedicalRecord rec : records) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    rec.getPatientID(),
                    rec.getName(),
                    rec.getDOB(),
                    rec.getGender(),
                    rec.getContactNumber(),
                    rec.getBloodType(),
                    rec.getEmail(),
                    String.join(";", rec.getPastDiagnoses()),
                    String.join(";", rec.getPastTreatments()),
                    String.join(";", rec.getCurrentDiagnoses()),
                    String.join(";", rec.getCurrentTreatments()),
                    String.join(";", rec.getPrescriptions())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Save the list of medical records back to the CSV file
    public static void saveAllMedicalRecords(List<MedicalRecord> medicalRecords) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("PatientID,Name,DOB,Gender,ContactNumber,BloodType,Email,PastDiagnoses,PastTreatments,CurrentDiagnoses,CurrentTreatments,Prescriptions");
            for (MedicalRecord record : medicalRecords) {
                writer.println(recordToCsv(record));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePatientContactInfo(String patientID, String contactNumber, String email) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(patientID)) {
                    if (contactNumber != null) values[4] = contactNumber; // Update contact number
                    if (email != null) values[6] = email; // Update email
                }
                records.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] record : records) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Convert a MedicalRecord object to CSV string
    private static String recordToCsv(MedicalRecord record) {
        return String.join(",",
            record.getPatientID(),
            record.getName(),
            record.getDOB(),
            record.getGender().name(),
            record.getContactNumber(),
            record.getBloodType(),
            record.getEmail(),
            listToCsv(record.getPastDiagnoses()),
            listToCsv(record.getPastTreatments()),
            listToCsv(record.getCurrentDiagnoses()),
            listToCsv(record.getCurrentTreatments()),
            listToCsv(record.getPrescriptions())
        );
    }

    // Helper method to convert list fields to a semicolon-separated string
    private static String listToCsv(List<String> list) {
        return String.join(";", list);
    }
}
