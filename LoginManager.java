package SC2002_Assignment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static final String DEFAULT_PASSWORD = "password";
    private static final String SALT = "HMS2024";
    private Map<String, User> credentialsMap;
    private User currentUser;
    private static final String PASSWORD_FILE = "SC2002_Assignment/data/passwords.csv";

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
        try (BufferedReader br = new BufferedReader(new FileReader(staffFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                String hospitalId = values[0].trim();
                String role = values[2].trim().toUpperCase();
                User user = new User(hospitalId, hashPassword(DEFAULT_PASSWORD), role);
                credentialsMap.put(hospitalId, user);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(patientFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
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
            currentUser.setPassword(hashPassword(newPassword));
            savePasswords();
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
