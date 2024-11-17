
public class User {
    private String hospitalID;
    private String password;
    private String userRole;

    public User(String hospitalID, String password, String userRole) {
        this.hospitalID = hospitalID;
        this.userRole = userRole;
        this.password = password;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
