import java.io.*;
import java.util.*;

public class MedicationCSVReader {
    private static final String CSV_PATH = "data/MedicationList.csv";
    
    private List<Medication> medicationList;

    public MedicationCSVReader() throws FileNotFoundException {
        this.medicationList = new ArrayList<>();
        loadMedicationData();
    }

    private void loadMedicationData() throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    processMedicationLine(line);
                } catch (Exception e) {
                    System.out.println("Error processing medication line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading medication data: " + e.getMessage());
        }
    }

    private void processMedicationLine(String line) {
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

    public List<Medication> getAllMedications() {
        return new ArrayList<>(medicationList);
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

        // Reload data
        medicationList.clear();
        loadMedicationData();
    }

    public void addMedication(String name, int stock, String description, int threshold) throws IOException {
        // Check if medication already exists
        if (medicationList.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Medication already exists: " + name);
        }

        // Append to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, true))) {
            String newLine = String.format("%s,%d,%s,%d", name, stock, description, threshold);
            bw.write(newLine);
            bw.newLine();
        }

        // Reload data
        medicationList.clear();
        loadMedicationData();
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

        // Reload data
        medicationList.clear();
        loadMedicationData();
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

        // Reload data
        medicationList.clear();
        loadMedicationData();
    }
}