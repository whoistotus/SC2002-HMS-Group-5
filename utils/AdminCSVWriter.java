package utils;
import java.io.*;
import java.util.*;

import model.HospitalStaff;

public class AdminCSVWriter {
    private static final String CSV_PATH = "data/staff.csv";

    public static void addStaffToCSV(HospitalStaff staff) throws IOException {
        try (FileWriter fw = new FileWriter(CSV_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String csvLine = String.format("%s,%s,%s,%s,%d",
                staff.getHospitalID(),
                staff.getName(),
                staff.getuserRole(),
                staff.getGender(),
                staff.getAge());
            bw.write(csvLine);
            bw.newLine();
        }
    }

    public static void removeStaffFromCSV(String hospitalID) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // Read all lines except the one to remove
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            // Add header
            lines.add(br.readLine());
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].trim().equalsIgnoreCase(hospitalID)) {
                    lines.add(line);
                } else {
                    found = true;
                }
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Staff member not found: " + hospitalID);
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static void updateStaffInCSV(String hospitalID, String name, String role, 
                                      String gender, int age, String password) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            // Add header
            lines.add(br.readLine());
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].trim().equalsIgnoreCase(hospitalID)) {
                    found = true;
                    line = String.format("%s,%s,%s,%s,%d,%s",
                        hospitalID, name, role, gender, age, password);
                }
                lines.add(line);
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Staff member not found: " + hospitalID);
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}