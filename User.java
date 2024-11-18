public class User implements UserInterface {
    private String hospitalID;
    private String password;
    private String userRole;

    // Constructor
    public User(String hospitalID, String password, String userRole) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.userRole = userRole;
    }

    // Getter for hospital ID
    @Override
    public String getHospitalID() {
        return hospitalID;
    }

    // Getter for password
    @Override
    public String getPassword() {
        return password;
    }

    // Getter for user role
    @Override
    public String getUserRole() {
        return userRole;
    }

    // Setter for password
    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
