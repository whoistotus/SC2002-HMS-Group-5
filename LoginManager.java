package SC2002_Assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static final String DEFAULT_PASSWORD = "password";
    private Map<String, UserCredentials> credentialsMap;
    private User currentUser;
    
    public LoginManager() {
        this.credentialsMap = new HashMap<>();
    }
    
    /**
     * Loads user credentials from staff and patient CSV files
     * @param staffFilePath Path to staff CSV file
     * @param patientFilePath Path to patient CSV file
     * @throws IOException If there's an error reading the files
     */
    public void loadCredentials(String staffFilePath, String patientFilePath) throws IOException {
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
                String role = values[2].trim().toUpperCase(); // Assuming role is in 3rd column
                credentialsMap.put(hospitalId, new UserCredentials(hospitalId, DEFAULT_PASSWORD, role));
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
                credentialsMap.put(hospitalId, new UserCredentials(hospitalId, DEFAULT_PASSWORD, "PATIENT"));
            }
        }
    }
    
    /**
     * Attempts to log in a user
     * @param hospitalId User's hospital ID
     * @param password User's password
     * @return LoginResult indicating success/failure and any relevant messages
     */
    public LoginResult login(String hospitalId, String password) {
        UserCredentials credentials = credentialsMap.get(hospitalId);
        
        if (credentials == null) {
            return new LoginResult(false, "Invalid hospital ID");
        }
        
        if (!credentials.validatePassword(password)) {
            return new LoginResult(false, "Incorrect password");
        }
        
        currentUser = new User(credentials.getHospitalId(), credentials.getRole());
        boolean isFirstLogin = credentials.isDefaultPassword();
        
        return new LoginResult(true, isFirstLogin ? "Please change your password" : "Login successful", 
                             currentUser, isFirstLogin);
    }
    
    /**
     * Changes the password for the current user
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password was changed successfully
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        UserCredentials credentials = credentialsMap.get(currentUser.getHospitalId());
        if (credentials.validatePassword(oldPassword)) {
            credentials.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    /**
     * Gets the currently logged-in user
     * @return Current User object or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        currentUser = null;
    }
}

class UserCredentials {
    private String hospitalId;
    private String password;
    private String role;
    private boolean isDefaultPassword;
    
    public UserCredentials(String hospitalId, String password, String role) {
        this.hospitalId = hospitalId;
        this.password = password;
        this.role = role;
        this.isDefaultPassword = true;
    }
    
    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }
    
    public void setPassword(String newPassword) {
        this.password = newPassword;
        this.isDefaultPassword = false;
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
}

class LoginResult {
    private boolean success;
    private String message;
    private User user;
    private boolean isFirstLogin;
    
    public LoginResult(boolean success, String message) {
        this(success, message, null, false);
    }
    
    public LoginResult(boolean success, String message, User user, boolean isFirstLogin) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.isFirstLogin = isFirstLogin;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public User getUser() {
        return user;
    }
    
    public boolean isFirstLogin() {
        return isFirstLogin;
    }
}

class User {
    private String hospitalId;
    private String role;
    
    public User(String hospitalId, String role) {
        this.hospitalId = hospitalId;
        this.role = role;
    }
    
    public String getHospitalId() {
        return hospitalId;
    }
    
    public String getRole() {
        return role;
    }
}