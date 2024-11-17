import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCSVReader {
    private String filePath;
    private List<HospitalStaff> staffList;

    public AdminCSVReader(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.staffList = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Staff data file not found at: " + file.getAbsolutePath());
        }

        loadStaffData();
    }

    public void refreshStaffData() {
        staffList.clear(); // Clear existing data
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    processStaffLine(line);
                } catch (Exception e) {
                    System.out.println("Error at line " + lineNumber + ": " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff data file: " + e.getMessage());
            staffList = new ArrayList<>();
        }
    }

    public String generateNextHospitalID(String role) throws Exception {
        refreshStaffData(); // Refresh data before generating new ID
        String prefix = role.equalsIgnoreCase("doctor") ? "D" : "P";
        int maxNumber = 0;

        for (HospitalStaff staff : staffList) {
            String staffId = staff.getHospitalID();
            if (staffId.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(staffId.substring(1));
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        return String.format("%s%03d", prefix, maxNumber + 1);
    }

    private void loadStaffData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Skip header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Skip empty or whitespace-only lines
                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty line at line " + lineNumber);
                    continue;
                }

                try {
                    processStaffLine(line);
                } catch (Exception e) {
                    System.out.println("Error at line " + lineNumber + ": " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading staff data file: " + e.getMessage());
            staffList = new ArrayList<>(); // Initialize empty list on error
        }
    }

    private void processStaffLine(String line) {
        // Handle empty or null lines
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty or null line");
        }

        // Split with proper CSV handling
        String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Correct the error message to match actual expected values
        if (values.length != 6) {
            throw new IllegalArgumentException("Invalid line format. Expected 6 values, got " + values.length);
        }

        try {
            String staffId = values[0].trim();
            String name = values[1].trim();
            String role = values[2].trim();
            String gender = values[3].trim();
            int age = Integer.parseInt(values[4].trim());
            String password = values[5].trim();

            // Validate staff ID format
            if (!isValidStaffId(staffId, role)) {
                throw new IllegalArgumentException("Invalid staff ID format: " + staffId);
            }

            // Validate role
            if (!isValidRole(role)) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }

            // Validate gender
            if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                throw new IllegalArgumentException("Invalid gender: " + gender);
            }

            // Validate age
            if (age < 18 || age > 100) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }

            staffList.add(new HospitalStaff(staffId, password, role, name, gender, age));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid age format: " + values[4]);
        }
    }

    private boolean isValidStaffId(String staffId, String role) {
        if (staffId == null || staffId.length() != 4) {
            return false;
        }

        char prefix = staffId.charAt(0);
        String number = staffId.substring(1);

        if (!number.matches("\\d{3}")) {
            return false;
        }

        switch (role.toLowerCase()) {
            case "doctor":
                return prefix == 'D';
            case "pharmacist":
                return prefix == 'P';
            case "administrator":
                return prefix == 'A';
            default:
                return false;
        }
    }

    private boolean isValidRole(String role) {
        return role != null &&
                (role.equalsIgnoreCase("Doctor") ||
                        role.equalsIgnoreCase("Pharmacist") ||
                        role.equalsIgnoreCase("Administrator"));
    }

    // Filter methods
    public List<HospitalStaff> filterByStaffId(String staffId) {
        return staffList.stream()
                .filter(staff -> staff.getHospitalID().equalsIgnoreCase(staffId))
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> filterByName(String name) {
        return staffList.stream()
                .filter(staff -> staff.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> filterByRole(String role) {
        return staffList.stream()
                .filter(staff -> staff.getuserRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> filterByGender(String gender) {
        return staffList.stream()
                .filter(staff -> staff.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> filterByAge(int age) {
        return staffList.stream()
                .filter(staff -> staff.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> filterByAgeRange(int minAge, int maxAge) {
        return staffList.stream()
                .filter(staff -> staff.getAge() >= minAge && staff.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    public List<HospitalStaff> getAllStaff() {
        return new ArrayList<>(staffList);
    }
}