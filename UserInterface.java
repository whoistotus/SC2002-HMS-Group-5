public interface UserInterface {
    String getHospitalID();  // Retrieve the user's hospital ID
    String getPassword();    // Retrieve the user's password
    String getUserRole();    // Retrieve the user's role
    void setPassword(String password); // Update the user's password
}
