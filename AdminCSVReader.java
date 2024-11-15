

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

    private void loadStaffData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header line
                }
                
                try {
                    processStaffLine(line);
                } catch (Exception e) {
                    System.out.println("Error processing line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error reading staff data file: " + e.getMessage());
        }
    }

    private void processStaffLine(String line) {
        String[] values = line.split(",");
        if (values.length != 5) {
            throw new IllegalArgumentException("Invalid line format. Expected 5 values, got " + values.length);
        }

        String staffId = values[0].trim();
        String name = values[1].trim();
        String role = values[2].trim();
        String gender = values[3].trim();
        int age;

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

        // Parse and validate age
        try {
            age = Integer.parseInt(values[4].trim());
            if (age < 18 || age > 100) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid age format: " + values[4]);
        }

        staffList.add(new HospitalStaff(staffId, "password", role, name, gender, age));
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