package utils;
import java.io.*;
import java.util.*;

import model.Medication;

public class MedicationCSVReader {
    private static final String CSV_PATH = "data/MedicationList.csv";

    public MedicationCSVReader() throws FileNotFoundException {
        // No need to store medication list as a field anymore
    }

    public List<Medication> getAllMedications() throws FileNotFoundException {
        List<Medication> medicationList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(CSV_PATH).getAbsoluteFile()))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    String[] values = line.split(",");
                    if (values.length == 4) {
                        String name = values[0].trim();
                        int stockLevel = Integer.parseInt(values[1].trim());
                        String description = values[2].trim();
                        int threshold = Integer.parseInt(values[3].trim());

                        medicationList.add(new Medication(name, stockLevel, description, threshold));
                    }
                } catch (Exception e) {
                    System.out.println("Error processing medication line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading medication data: " + e.getMessage());
        }
        
        return medicationList;
    }

    private void loadMedicationData(List<Medication> medicationList) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    processMedicationLine(line, medicationList);
                } catch (Exception e) {
                    System.out.println("Error processing medication line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading medication data: " + e.getMessage());
        }
    }

    private void processMedicationLine(String line, List<Medication> medicationList) {
        String[] values = line.split(",");
        if (values.length != 4) {
            throw new IllegalArgumentException("Invalid line format. Expected 4 values.");
        }

        String name = values[0].trim();
        int stockLevel = Integer.parseInt(values[1].trim());
        String description = values[2].trim();
        int threshold = Integer.parseInt(values[3].trim());

        medicationList.add(new Medication(name, stockLevel, description, threshold));
    }

    public void updateMedication(String name, int newStock) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            lines.add(br.readLine()); // Add header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].trim().equalsIgnoreCase(name)) {
                    found = true;
                    values[1] = String.valueOf(newStock);
                    line = String.join(",", values);
                }
                lines.add(line);
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Medication not found: " + name);
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public void addMedication(String name, int stock, String description, int threshold) throws IOException {
        // Check if medication already exists
        List<Medication> currentMedications = getAllMedications();
        if (currentMedications.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Medication already exists: " + name);
        }

        // Append to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, true))) {
            String newLine = String.format("%s,%d,%s,%d", name, stock, description, threshold);
            bw.write(newLine);
            bw.newLine();
        }
    }

    public void removeMedication(String name) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            lines.add(br.readLine()); // Add header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].trim().equalsIgnoreCase(name)) {
                    lines.add(line);
                } else {
                    found = true;
                }
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Medication not found: " + name);
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public void updateThreshold(String name, int newThreshold) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            lines.add(br.readLine()); // Add header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].trim().equalsIgnoreCase(name)) {
                    found = true;
                    values[3] = String.valueOf(newThreshold);
                    line = String.join(",", values);
                }
                lines.add(line);
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Medication not found: " + name);
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