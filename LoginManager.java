package SC2002_Assignment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static final String DEFAULT_PASSWORD = "password";
    private static final String SALT = "HMS2024"; // Adding a salt for extra security
    private Map<String, UserCredentials> credentialsMap;
    private User currentUser;
    private static final String PASSWORD_FILE = "SC2002_Assignment/data/passwords.csv";
    
    public LoginManager() {
        this.credentialsMap = new HashMap<>();
    }
    
    /**
     * Hash a password using SHA-256
     * @param password The password to hash
     * @return The hashed password
     */
    private String hashPassword(String password) {
        try {
            // Add salt to password
            String saltedPassword = password + SALT;
            
            // Create message digest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Get the hash bytes
            byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            
            // Convert bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }
    
    public void loadCredentials(String staffFilePath, String patientFilePath) throws IOException {
        // First load all users with default passwords
        loadUsers(staffFilePath, patientFilePath);
        
        // Then load any saved passwords
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
                    continue;
                }
                String[] values = line.split(",");
                String hospitalId = values[0].trim();
                String role = values[2].trim().toUpperCase();
                credentialsMap.put(hospitalId, new UserCredentials(hospitalId, hashPassword(DEFAULT_PASSWORD), role));
            }
        }
        
        // Load patient credentials
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
                credentialsMap.put(hospitalId, new UserCredentials(hospitalId, hashPassword(DEFAULT_PASSWORD), "PATIENT"));
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
                    UserCredentials credentials = credentialsMap.get(hospitalId);
                    if (credentials != null) {
                        credentials.setPassword(hashedPassword);
                        // If password is not default, mark it as changed
                        if (!hashedPassword.equals(hashPassword(DEFAULT_PASSWORD))) {
                            credentials.setPasswordChanged();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No saved passwords found. Using defaults.");
        }
    }
    
    private void savePasswords() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
            for (Map.Entry<String, UserCredentials> entry : credentialsMap.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue().getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving passwords: " + e.getMessage());
        }
    }
    
    public LoginResult login(String hospitalId, String password) {
        UserCredentials credentials = credentialsMap.get(hospitalId);
        
        if (credentials == null) {
            return new LoginResult(false, "Invalid hospital ID");
        }
        
        String hashedInputPassword = hashPassword(password);
        if (!credentials.validatePassword(hashedInputPassword)) {
            return new LoginResult(false, "Incorrect password");
        }
        
        currentUser = new User(credentials.getHospitalId(), credentials.getRole());
        boolean isFirstLogin = credentials.isDefaultPassword();
        
        return new LoginResult(true, isFirstLogin ? "Please change your password" : "Login successful", 
                             currentUser, isFirstLogin);
    }
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        UserCredentials credentials = credentialsMap.get(currentUser.getHospitalId());
        if (credentials.validatePassword(hashPassword(oldPassword))) {
            credentials.setPassword(hashPassword(newPassword));
            savePasswords(); // Save passwords after successful change
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

class UserCredentials {
    private String hospitalId;
    private String hashedPassword;
    private String role;
    private boolean isDefaultPassword;
    
    public UserCredentials(String hospitalId, String hashedPassword, String role) {
        this.hospitalId = hospitalId;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.isDefaultPassword = true;
    }
    
    public boolean validatePassword(String hashedInputPassword) {
        return hashedPassword.equals(hashedInputPassword);
    }
    
    public void setPassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
        this.isDefaultPassword = false;
    }
    
    public String getPassword() {
        return hashedPassword;
    }
    
    public String getHospitalId() {
        return hospitalId;
    }
    
    public String getRole() {
        return role;
    }
    
    public boolean isDefaultPassword() {
        return isDefaultPassword;
    }
    
    public void setPasswordChanged() {
        this.isDefaultPassword = false;
    }
}