package controller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.User;

public class LoginManager {
    private static final String DEFAULT_PASSWORD = "password";
    private static final String SALT = "HMS2024";
    private Map<String, User> credentialsMap;
    private User currentUser;
    private static final String PASSWORD_FILE = "data/passwords.csv";

    public LoginManager() {
        this.credentialsMap = new HashMap<>();
    }

    private String hashPassword(String password) {
        try {
            String saltedPassword = password + SALT;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    public void loadCredentials(String staffFilePath, String patientFilePath) throws IOException {
        loadUsers(staffFilePath, patientFilePath);
        loadPasswords();
    }

    private void loadUsers(String staffFilePath, String patientFilePath) throws IOException {
        // Load staff credentials
        try (BufferedReader br = new BufferedReader(new FileReader(staffFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                if (values.length < 6) {
                    continue;
                }

                String hospitalId = values[0].trim();
                String role = values[2].trim().toUpperCase();
                User user = new User(hospitalId, hashPassword(DEFAULT_PASSWORD), role);
                credentialsMap.put(hospitalId, user);
            }
        }

        // Load patient credentials
        try (BufferedReader br = new BufferedReader(new FileReader(patientFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                if (values.length < 8) {
                    continue;
                }

                String hospitalId = values[0].trim();
                User user = new User(hospitalId, hashPassword(DEFAULT_PASSWORD), "PATIENT");
                credentialsMap.put(hospitalId, user);
            }
        }
    }

    private void loadPasswords() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String hospitalId = values[0].trim();
                    String hashedPassword = values[1].trim();
                    User user = credentialsMap.get(hospitalId);
                    if (user != null) {
                        user.setPassword(hashedPassword);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No saved passwords found. Using defaults.");
        }
    }

    private void saveRawPasswordToCsv(String filePath, String hospitalId, String rawPassword) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values[0].trim().equals(hospitalId)) {
                        values[values.length - 1] = rawPassword; // Update the password column
                    }
                    lines.add(String.join(",", values));
                }
            }

            // Write the updated lines back to the file
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating raw password in " + filePath + ": " + e.getMessage());
        }
    }

    private void saveRawPassword(String hospitalId, String rawPassword) {
        if (credentialsMap.containsKey(hospitalId)) {
            User user = credentialsMap.get(hospitalId);
            if (user.getUserRole().equalsIgnoreCase("PATIENT")) {
                saveRawPasswordToCsv("data/PatientList.csv", hospitalId, rawPassword);
            } else {
                saveRawPasswordToCsv("data/StaffList.csv", hospitalId, rawPassword);
            }
        }
    }

    private void savePasswords() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
            for (User user : credentialsMap.values()) {
                pw.println(user.getHospitalID() + "," + user.getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving passwords: " + e.getMessage());
        }
    }

    public boolean login(String hospitalId, String password) {
        User user = credentialsMap.get(hospitalId);
        if (user == null) {
            System.out.println("Invalid hospital ID");
            return false;
        }
        String hashedInputPassword = hashPassword(password);
        if (!user.getPassword().equals(hashedInputPassword)) {
            System.out.println("Incorrect password");
            return false;
        }
        currentUser = user;
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) return false;
        String hashedOldPassword = hashPassword(oldPassword);
        if (currentUser.getPassword().equals(hashedOldPassword)) {
            String hashedNewPassword = hashPassword(newPassword);
            currentUser.setPassword(hashedNewPassword);
            savePasswords(); // Save hashed password to password.csv
            saveRawPassword(currentUser.getHospitalID(), newPassword); // Save raw password to respective CSV
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
